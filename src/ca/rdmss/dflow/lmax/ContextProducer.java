package ca.rdmss.dflow.lmax;

import ca.rdmss.dflow.Task;

public interface ContextProducer<T> {

	public void onData(T context);

	public void onData(T context, @SuppressWarnings("unchecked") Task<T>... tasks);
	
}
