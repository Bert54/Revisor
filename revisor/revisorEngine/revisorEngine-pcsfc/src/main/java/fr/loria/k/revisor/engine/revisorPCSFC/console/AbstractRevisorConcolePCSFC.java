package fr.loria.k.revisor.engine.revisorPCSFC.console;

import java.util.Set;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorEnginePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.config.PCSFCConfig;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.orpailleur.revisor.engine.core.console.AbstractRevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.storage.MacroStorage;

public abstract class AbstractRevisorConcolePCSFC<C extends AbstractRevisorConcolePCSFC<C, E, P, I>, E extends RevisorEnginePCSFC, P extends PCSFCConfig, I extends Instruction<C>> extends AbstractRevisorConsole<C, E, P, I> {

	private MacroStorage<PCSFCFormula> macros;
	
	protected AbstractRevisorConcolePCSFC() throws ConsoleInitializationException {
		super();
		this.init();
	}
	
	protected AbstractRevisorConcolePCSFC(final E engine) throws ConsoleInitializationException {
		super(engine);
		this.init();
	}
	
	protected AbstractRevisorConcolePCSFC(final P config) throws ConsoleInitializationException {
		super(config);
		this.init();
	}
	
	public AbstractRevisorConcolePCSFC(final E engine, final P config) throws ConsoleInitializationException {
		super(engine, config);
		this.init();
	}

	private void init() {
		this.macros = new MacroStorage<>();
	}
	
	public MacroStorage<PCSFCFormula> getMacroList() {
		return this.macros;
	}
	
	@Override
	public Set<String> getMacros() {
		Set<String> macros = this.newVariableSet();
		macros.addAll(this.macros.getMacros());
		return macros;
	}

	public PCSFCFormula revise(PCSFCFormula psi, PCSFCFormula mu, double epsilon) {
		return this.getEngine().revise(psi, mu, epsilon);
	}
	
}
