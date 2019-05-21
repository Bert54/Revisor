package fr.loria.orpailleur.revisor.engine.revisorPLAK.console;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.exception.ConsoleInitializationException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;
import fr.loria.orpailleur.revisor.engine.core.console.storage.MacroStorage;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.RevisorEnginePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.config.PLAKConfig;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleIdentifier;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSet;

/**
 * @author William Philbert
 */
public abstract class AbstractRevisorConsolePLAK<C extends AbstractRevisorConsolePLAK<C, E, P, I>, E extends RevisorEnginePLAK, P extends PLAKConfig, I extends Instruction<C>> extends AbstractRevisorConsolePL<C, E, P, I> implements Observer {
	
	// Constants :
	
	private static final String UNDEFINED_RULE_MACRO = "Undefine rule macro : '%s'.";
	
	// Fields :
	
	private MacroStorage<PLAK_Rule<C>> rules;
	private MacroStorage<PLAK_RuleSet<C>> ruleSets;
	
	private PLAK_RuleSet<C> defaultRuleSet;
	private boolean useDefaultRuleSet = true;
	
	private double defaultRuleCost = 0.3;
	
	// Constructors :
	
	protected AbstractRevisorConsolePLAK() throws ConsoleInitializationException {
		super();
		this.init();
	}
	
	protected AbstractRevisorConsolePLAK(final E engine) throws ConsoleInitializationException {
		super(engine);
		this.init();
	}
	
	protected AbstractRevisorConsolePLAK(final P config) throws ConsoleInitializationException {
		super(config);
		this.init();
	}
	
	public AbstractRevisorConsolePLAK(final E engine, final P config) throws ConsoleInitializationException {
		super(engine, config);
		this.init();
	}
	
	// Getters :
	
	public MacroStorage<PLAK_Rule<C>> getRules() {
		return this.rules;
	}
	
	public MacroStorage<PLAK_RuleSet<C>> getRuleSets() {
		return this.ruleSets;
	}
	
	public PLAK_RuleSet<C> getDefaultRuleSet() {
		return this.defaultRuleSet;
	}
	
	public boolean isUseDefaultRuleSet() {
		return this.useDefaultRuleSet;
	}
	
	public double getDefaultRuleCost() {
		return this.defaultRuleCost;
	}
	
	// Setters :
	
	public void setUseDefaultRuleSet(boolean useDefaultRuleSet) {
		this.useDefaultRuleSet = useDefaultRuleSet;
	}
	
	public void setDefaultRuleCost(double defaultRuleCost) {
		if(defaultRuleCost > 0) {
			this.defaultRuleCost = defaultRuleCost;
		}
	}
	
	// Methods :
	
	private void init() {
		this.defaultRuleSet = new PLAK_RuleSet<>();
		this.ruleSets = new MacroStorage<>();
		this.rules = new MacroStorage<>();
		this.rules.addObserver(this);
	}
	
	@Override
	public void update(Observable obs, Object obj) {
		if(obs == this.rules) {
			String macro = this.rules.getLastAdded();
			
			if(macro != null) {
				try {
					this.defaultRuleSet.addRule(new PLAK_RuleIdentifier<C>(macro));
				}
				catch(InstructionExecutionException argh) {
					this.getLogger().logError(argh);
				}
			}
			else {
				this.getLogger().logError("Invalid update from observable '%s' with argument '%s'.", obs, obj);
			}
		}
		else {
			super.update(obs, obj);
		}
	}
	
	@Override
	public Set<String> getMacros() {
		final Set<String> macros = super.getMacros();
		macros.addAll(this.ruleSets.getMacros());
		macros.addAll(this.rules.getMacros());
		return macros;
	}
	
	@Override
	public void clear() {
		super.clear();
		this.init();
		this.resetFlipCosts();
	}
	
	public void resetFlipCosts() {
		this.getEngine().clearFlipCosts();
	}
	
	public void resetRuleCosts() {
		for(PLAK_Rule<C> rule : this.rules.getValues()) {
			rule.setCost(this.defaultRuleCost);
		}
	}
	
	public double getFlipCost(final String var) {
		return this.getEngine().getFlipCost(var);
	}
	
	public void setFlipCost(final String var, final double weight) {
		this.getEngine().setFlipCost(var, weight);
	}
	
	public double getRuleCost(final String var) throws InstructionExecutionException {
		PLAK_Rule<?> rule = this.rules.getValue(var);
		
		if(rule == null) {
			throw new InstructionExecutionException(String.format(UNDEFINED_RULE_MACRO, var));
		}
		
		return rule.getCost();
	}
	
	public void setRuleCost(final String var, final double cost) throws InstructionExecutionException {
		PLAK_Rule<?> rule = this.rules.getValue(var);
		
		if(rule == null) {
			throw new InstructionExecutionException(String.format(UNDEFINED_RULE_MACRO, var));
		}
		
		rule.setCost(cost);
	}
	
	@Override
	public PLFormula adapt(final PLFormula dk, final PLFormula source, final PLFormula target) {
		if(this.useDefaultRuleSet) {
			return this.adapt(dk, source, target, this.defaultRuleSet);
		}
		
		return super.adapt(dk, source, target);
	}
	
	@Override
	public PLFormula revise(final PLFormula psi, final PLFormula mu) {
		if(this.useDefaultRuleSet) {
			return this.revise(psi, mu, this.defaultRuleSet);
		}
		
		return super.revise(psi, mu);
	}
	
	public PLFormula adapt(final PLFormula dk, final PLFormula source, final PLFormula target, final PLAK_RuleSet<C> ruleSet) {
		return this.getEngine().adaptAK(source, target, dk, ruleSet.createRuleSet(self()));
	}
	
	public PLFormula revise(final PLFormula psi, final PLFormula mu, final PLAK_RuleSet<C> ruleSet) {
		return this.getEngine().adaptAK(psi, mu, ruleSet.createRuleSet(self()));
	}
	
}
