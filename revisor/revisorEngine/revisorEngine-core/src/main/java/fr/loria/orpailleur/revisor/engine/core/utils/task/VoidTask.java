package fr.loria.orpailleur.revisor.engine.core.utils.task;

/**
 * @author William Philbert
 */
public abstract class VoidTask extends Task<Object> {
	
	// Methods :
	
	@Override
	protected final Object computeResult() throws Exception {
		this.execute();
		return null;
	}
	
	protected abstract void execute() throws Exception;
	
}
