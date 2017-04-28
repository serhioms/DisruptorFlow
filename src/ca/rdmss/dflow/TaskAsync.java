package ca.rdmss.dflow;

import ca.rdmss.dflow.impl.AsyncContext;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

abstract public class TaskAsync<T> extends TaskSync<T>{
	
	private LMaxDisruptor<AsyncContext<T>> disruptor;

	public TaskAsync(LMaxDisruptor<AsyncContext<T>> disruptor) {
		this.disruptor = disruptor;
	}

	public TaskAsync() {
		// Special case - disrupter will be initialized on consumer side later
	}

	final public void setDisruptor(LMaxDisruptor<AsyncContext<T>> disruptor) {
		this.disruptor = disruptor;
	}

	@Override
	final public void publishAsync(T context) {
		disruptor.onData(new AsyncContext<T>(context, this));
	}

	@Override
	final public boolean isAsync() {
		return true;
	}
}
