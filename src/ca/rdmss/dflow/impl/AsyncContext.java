package ca.rdmss.dflow.impl;

import ca.rdmss.dflow.Task;

public class AsyncContext<T> {

	final public T context;
	final public Task<T> task;
	
	public AsyncContext(T context, Task<T> task) {
		this.context = context;
		this.task = task;
	}
}
