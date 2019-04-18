package fr.loria.orpailleur.revisor.engine.core.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;

/**
 * @author William Philbert
 */
public abstract class AbstractExpression<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> implements Expression<C, V> {
	
	// Fields :
	
	protected final List<String> nodeWarningMessages = new LinkedList<>();
	
	private SideNote<C> sideNote;
	
	// Constructors :
	
	protected AbstractExpression() {
		super();
	}
	
	// Getters :
	
	@Override
	public List<String> getNodeWarningMessages() {
		List<String> messages = new LinkedList<>();
		messages.addAll(this.nodeWarningMessages);
		return messages;
	}
	
	@Override
	public SideNote<C> getSideNote() {
		return this.sideNote;
	}
	
	// Setters :
	
	protected void setSideNote(final SideNote<C> sideNote) {
		this.sideNote = sideNote;
	}
	
	// Methods :
	
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		for(Expression<C, ?> child : this.getChildren()) {
			child.validate(console, newVars);
		}
	}
	
	@Override
	public V getOptimizedValue(final C console) {
		return this.getValue(console);
	}
	
	@Override
	public List<String> getWarningMessages() {
		List<String> messages = this.getNodeWarningMessages();
		
		for(Expression<C, ?> child : this.getChildren()) {
			messages.addAll(child.getWarningMessages());
		}
		
		return messages;
	}
	
	@Override
	public boolean hasWarningMessages() {
		if(this.hasNodeWarningMessages()) {
			return true;
		}
		
		for(Expression<C, ?> child : this.getChildren()) {
			if(child.hasWarningMessages()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean hasNodeWarningMessages() {
		return !this.nodeWarningMessages.isEmpty();
	}
	
	@Override
	public void addWarningMessage(final String message) {
		this.nodeWarningMessages.add(message);
	}
	
	@Override
	public Collection<SideNote<C>> getSideNotes() {
		final LinkedList<SideNote<C>> sideNotes = new LinkedList<>();
		
		if(this.sideNote != null) {
			sideNotes.add(this.sideNote);
		}
		
		for(Expression<C, ?> child : this.getChildren()) {
			sideNotes.addAll(child.getSideNotes());
		}
		
		return sideNotes;
	}
	
	@Override
	public final String toString() {
		return this.toString(false);
	}
	
	@Override
	public final String toLatex() {
		return this.toString(true);
	}
	
}
