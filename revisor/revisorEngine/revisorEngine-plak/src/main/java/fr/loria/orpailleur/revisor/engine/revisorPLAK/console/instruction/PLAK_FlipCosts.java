package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import java.util.Map;
import java.util.TreeMap;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Identifier;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Literal;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_FlipCosts<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected Map<PL_Literal<C>, Double> result;
	
	// Constructors :
	
	public PLAK_FlipCosts(final C console, final String inputText) {
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
			PL_Identifier<C> id = new PL_Identifier<>(var);
			
			this.result.put(new PL_Literal<>(id, false), this.console.getWeight(var));
			this.result.put(new PL_Literal<>(id, true), this.console.getWeight("!" + var));
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
