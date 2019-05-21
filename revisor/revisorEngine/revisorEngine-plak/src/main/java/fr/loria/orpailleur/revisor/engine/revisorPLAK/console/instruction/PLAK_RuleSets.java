package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSetIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_RuleSets<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected Map<PLAK_RuleSetIdentifier<C>, PLAK_RuleSet<C>> result;
	
	// Constructors :
	
	public PLAK_RuleSets(final C console, final String inputText) {
		super(console, inputText);
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to check.
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.result = new TreeMap<>();
		
		for(Entry<String, PLAK_RuleSet<C>> entry : this.console.getRuleSets().getUnmodifiableMap().entrySet()) {
			this.result.put(new PLAK_RuleSetIdentifier<C>(entry.getKey()), entry.getValue());
		}
	}
	
	@Override
	protected void clearTmpResources() {
		this.result = null;
	}
	
	@Override
	public String createFormatedInputText() {
		return this.simplifiedString(this.inputText);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return StringUtils.toString(this.result, latex);
	}
	
}
