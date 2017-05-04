package ca.rdmss.dflow.lmax;

import java.util.concurrent.atomic.AtomicReference;

import ca.rdmss.dflow.Task;

public class ContextEvent<T> {

	final private AtomicReference<T> ref = new AtomicReference<T>();
	
	private Task<T>[] tasks;
	
	public T getContext() {
		return ref.get();
	}
	
	public void setContext(T context) {
		ref.set(context);
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
