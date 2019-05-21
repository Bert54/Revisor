package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.AbstractExpression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.comparator.RuleComparator;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.RuleSet;

/**
 * @author William Philbert
 */
public class PLAK_RuleSet<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractExpression<C, PLAK_RuleSet<C>> {
	
	// Constants :
	
	private static final String RULE_SET_ALREADY_CONTAINS_RULE = "This rule set already contains the rule '%s'.";
	private static final String RULE_SET_DOESNT_CONTAIN_RULE = "This rule set doesn't contain the rule '%s'.";
	
	// Fields :
	
	protected final Set<Expression<C, PLAK_Rule<C>>> rules;
	
	// Constructors :
	
	public PLAK_RuleSet() {
		this.rules = this.newSet();
	}
	
	public PLAK_RuleSet(final Set<Expression<C, PLAK_Rule<C>>> rules) {
		this.rules = this.newSet(rules);
	}
	
	// Methods :
	
	/**
	 * Creates a empty Set to be used to store the rules of this rule set.
	 * @return a empty Set.
	 */
	protected Set<Expression<C, PLAK_Rule<C>>> newSet() {
		return new TreeSet<>(new RuleComparator<C>());
	}
	
	/**
	 * Creates a Set containing the given rules.
	 * If the given rules are in a set of the proper form, this set is returned, else a set of the proper form is created and returned.
	 * @param rules - the rules to include in the set.
	 * @return a Set containing the given rules.
	 */
	protected Set<Expression<C, PLAK_Rule<C>>> newSet(final Set<Expression<C, PLAK_Rule<C>>> rules) {
		if(rules instanceof TreeSet) {
			return rules;
		}
		else {
			return new TreeSet<>(rules);
		}
	}
	
	/**
	 * Indicates whether this rule set contains the given rule.
	 * @param rule - the rule to find.
	 * @return true if this rule set contains the given rule, else false.
	 */
	public boolean contains(final Expression<C, PLAK_Rule<C>> rule) {
		return this.rules.contains(rule);
	}
	
	/**
	 * Adds the given rule to this rule set.
	 * @param rule - the rule to add.
	 * @param console - the console environment in which expressions must be evaluated.
	 * @throws InstructionExecutionException - if this rule set already contains the given rule.
	 */
	public void addRule(final Expression<C, PLAK_Rule<C>> rule) throws InstructionExecutionException {
		if(this.rules.contains(rule)) {
			throw new InstructionExecutionException(String.format(RULE_SET_ALREADY_CONTAINS_RULE, rule));
		}
		
		this.rules.add(rule);
	}
	
	/**
	 * Removes the given rule from this rule set.
	 * @param rule - the rule to remove.
	 * @param console - the console environment in which expressions must be evaluated.
	 * @throws InstructionExecutionException - if the rule set doesn't contain the given rule.
	 */
	public void removeRule(final Expression<C, PLAK_Rule<C>> rule) throws InstructionExecutionException {
		if(!this.rules.contains(rule)) {
			throw new InstructionExecutionException(String.format(RULE_SET_DOESNT_CONTAIN_RULE, rule));
		}
		
		this.rules.remove(rule);
	}
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		final Collection<Expression<C, ?>> children = new LinkedList<>();
		children.addAll(this.rules);
		return children;
	}
	
	@Override
	public PLAK_RuleSet<C> getValue(final C console) {
		return this;
	}
	
	public PLAK_RuleSet<C> copy() {
		final PLAK_RuleSet<C> copy = new PLAK_RuleSet<>();
		
		for(Expression<C, PLAK_Rule<C>> rule : this.rules) {
			copy.rules.add(rule);
		}
		
		return copy;
	}
	
	/**
	 * Creates a concrete RuleSet object based on the PLAK_Rule objects contained in this PLAK_RuleSet object.
	 * @param console - the console environment in which expressions must be evaluated.
	 * @return a RuleSet object based on this PLAK_RuleSet object.
	 */
	public RuleSet createRuleSet(final C console) {
		final RuleSet ruleSet = new RuleSet(console.getLi());
		
		for(Expression<C, PLAK_Rule<C>> expression : this.rules) {
			PLAK_Rule<C> rule = expression.getValue(console);
			ruleSet.addRule(rule.getContext().getValue(console), rule.getLeft().getValue(console), rule.getRight().getValue(console), rule.getCost());
		}
		
		return ruleSet;
	}
	
	@Override
	public String toString(final boolean latex) {
		return StringUtils.toString(this.rules, latex, StringUtils.symbol("{", latex), StringUtils.symbol(", ", latex), StringUtils.symbol("}", latex));
	}
	
}
