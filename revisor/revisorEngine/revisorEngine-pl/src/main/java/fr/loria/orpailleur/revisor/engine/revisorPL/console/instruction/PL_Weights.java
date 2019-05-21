package fr.loria.orpailleur.revisor.engine.revisorPL.console.instruction;

import java.util.Map;
import java.util.TreeMap;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Identifier;

/**
 * @author William Philbert
 */
public class PL_Weights<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected Map<PL_Identifier<C>, Double> result;
	
	// Constructors :
	
	public PL_Weights(final C console, final String inputText) {
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
		
		for(String var : this.console.getVariables()) {
			this.result.put(new PL_Identifier<C>(var), this.console.getWeight(var));
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
