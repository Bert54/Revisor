package fr.loria.orpailleur.revisor.engine.revisorPL.console;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.loria.orpailleur.revisor.engine.core.console.AbstractRevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.storage.MacroStorage;
import fr.loria.orpailleur.revisor.engine.core.utils.task.Task;
import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorEnginePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.config.PLConfig;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.output.Substitution;

/**
 * @author William Philbert
 */
public abstract class AbstractRevisorConsolePL<C extends AbstractRevisorConsolePL<C, E, P, I>, E extends RevisorEnginePL, P extends PLConfig, I extends Instruction<C>> extends AbstractRevisorConsole<C, E, P, I> {
	
	// Fields :
	
	private MacroStorage<PLFormula> formulae;
	
	// Constructors :
	
	protected AbstractRevisorConsolePL() throws ConsoleInitializationException {
		super();
		this.init();
	}
	
	protected AbstractRevisorConsolePL(final E engine) throws ConsoleInitializationException {
		super(engine);
		this.init();
	}
	
	protected AbstractRevisorConsolePL(final P config) throws ConsoleInitializationException {
		super(config);
		this.init();
	}
	
	public AbstractRevisorConsolePL(final E engine, final P config) throws ConsoleInitializationException {
		super(engine, config);
		this.init();
	}
	
	// Getters :
	
	public MacroStorage<PLFormula> getFormulae() {
		return this.formulae;
	}
	
	// Methods :
	
	private void init() {
		this.formulae = new MacroStorage<>();
	}
	
	@Override
	public Set<String> getMacros() {
		Set<String> macros = this.newVariableSet();
		macros.addAll(this.formulae.getMacros());
		return macros;
	}
	
	@Override
	public boolean isVariable(final String name) {
		return "true".equals(name) || "false".equals(name) || super.isVariable(name);
	}
	
	@Override
	public void clear() {
		super.clear();
		this.init();
		this.resetWeights();
	}
	
	public LI getLi() {
		return this.getEngine().li;
	}
	
	public PL getPl() {
		return this.getEngine().pl;
	}
	
	public void resetWeights() {
		this.getEngine().resetWeights();
	}
	
	public void setWeight(final String var, final double weight) {
		this.getEngine().setWeight(var, weight);
	}
	
	public double getWeight(final String var) {
		return this.getEngine().getWeight(var);
	}
	
	public PLFormula adapt(final PLFormula dk, final PLFormula source, final PLFormula target) {
		return this.getEngine().adapt(source, target, dk);
	}
	
	public PLFormula revise(final PLFormula psi, final PLFormula mu) {
		return this.getEngine().revise(psi, mu);
	}
	
	public PLFormula simplifiedDNF(final PLFormula formula) {
		return this.getEngine().simplifiedDNF(formula).flatten();
	}
	
	public Substitution substitution(final PLFormula dk, final PLFormula source, final PLFormula target, final PLFormula result) throws Exception {
		int timeout = this.getConfig().substitutionTimeout.getValue();
		
		if(timeout > 0) {
			final Task<Substitution> task = new Task<Substitution>() {
				
				@Override
				protected Substitution computeResult() throws Exception {
					return AbstractRevisorConsolePL.this.getEngine().substitution(source, target, dk, result);
				}
				
			};
			
			return Task.executeTask(task, timeout, TimeUnit.SECONDS);
		}
		else {
			return this.getEngine().substitution(source, target, dk, result);
		}
	}
	
	public Substitution substitution(final PLFormula psi, final PLFormula mu, final PLFormula result) throws Exception {
		int timeout = this.getConfig().substitutionTimeout.getValue();
		
		if(timeout > 0) {
			final Task<Substitution> task = new Task<Substitution>() {
				
				@Override
				protected Substitution computeResult() throws Exception {
					return AbstractRevisorConsolePL.this.getEngine().substitution(psi, mu, result);
				}
				
			};
			
			return Task.executeTask(task, timeout, TimeUnit.SECONDS);
		}
		else {
			return this.getEngine().substitution(psi, mu, result);
		}
	}
	
}
