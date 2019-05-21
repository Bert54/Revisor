package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.comparator;

import java.util.Comparator;

import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleIdentifier;

/**
 * @author William Philbert
 */
public class RuleComparator<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> implements Comparator<Expression<C, PLAK_Rule<C>>> {
	
	// Methods :
	
	@Override
	public int compare(Expression<C, PLAK_Rule<C>> a, Expression<C, PLAK_Rule<C>> b) {
		if(a instanceof PLAK_RuleIdentifier) {
			if(b instanceof PLAK_RuleIdentifier) {
				return ((PLAK_RuleIdentifier<C>) a).compareTo((PLAK_RuleIdentifier<C>) b);
			}
			
			return -1;
		}
		else if(a instanceof PLAK_Rule) {
			if(b instanceof PLAK_Rule) {
				return ((PLAK_Rule<C>) a).compareTo((PLAK_Rule<C>) b);
			}
			else if(b instanceof PLAK_RuleIdentifier) {
				return 1;
			}
			
			return -1;
		}
		else if(b instanceof PLAK_RuleIdentifier || b instanceof PLAK_Rule) {
			return 1;
		}
		
		return a.toString().compareTo(b.toString());
	}
	
}
