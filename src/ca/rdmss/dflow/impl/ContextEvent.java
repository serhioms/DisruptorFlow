package ca.rdmss.dflow.impl;

import ca.rdmss.dflow.Task;

public class ContextEvent<T> {

	private T context;
	private Task<T>[] tasks;
	
	public ContextEvent() {
	}

	@SafeVarargs
	public ContextEvent(T context, Task<T>... tasks) {
		this.context = context;
		this.tasks = tasks;
	}

	public T getContext() {
		return context;
	}
	
	public void setContext(T context) {
		this.context = context;
	}

	public Task<T>[] getTasks() {
		return tasks;
	}
	
	public void setTasks(Task<T>[] tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return String.format("tasks=%d", tasks==null?0:tasks.length);
	}
}
