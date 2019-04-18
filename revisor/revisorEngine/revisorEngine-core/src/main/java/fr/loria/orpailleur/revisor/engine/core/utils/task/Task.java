package fr.loria.orpailleur.revisor.engine.core.utils.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author William Philbert
 */
public abstract class Task<T extends Object> implements Callable<T> {
	
	// Fields :
	
	private Exception exception = null;
	
	// Getters :
	
	public Exception getException() {
		return this.exception;
	}
	
	// Methods :
	
	public boolean hasException() {
		return this.exception != null;
	}
	
	@Override
	public final T call() {
		try {
			return this.computeResult();
		}
		catch(Exception argh) {
			this.exception = argh;
			return null;
		}
	}
	
	protected abstract T computeResult() throws Exception;
	
	// Static methods :
	
	public static <T> T executeTask(final Task<T> task, final int delay, final TimeUnit unit) throws Exception {
		final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		final Future<T> handler = executor.submit(task);
		executor.shutdown();
		
		if(delay > 0) {
			try {
				handler.get(delay, unit);
			}
			catch(TimeoutException | InterruptedException | ExecutionException | CancellationException argh) {
				executor.shutdownNow();
			}
		}
		
		if(task.hasException()) {
			throw task.getException();
		}
		
		return handler.get();
	}
	
}
