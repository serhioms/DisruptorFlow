package ca.rdmss.dflow;

import ca.rdmss.dflow.impl.AsyncContext;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public interface Task<T> {

	// Task execution interface
	public TaskTransition execute(T context) throws Throwable;
	
	// Task can be 
	// sync  - wait for end of task before start next one 
	// async - do not wait for end of task and start next one immediately 
	public boolean isAsync();
	
	// Async task must be published once more before execution
	public void publishAsync(T context);
	
	// For async task async disruptor must be pre-set at runtime
	public void setDisruptor(LMaxDisruptor<AsyncContext<T>> disruptor);
	
	// Task can be simple (false) or set (true)
	public boolean isSet();
	
	// setter/getter for set
	public Task<T>[] getSet();
	public void setSet(Task<T>[] tasks);
	
	// Exception handler
	public ExceptionHandler<T> getExceptionHandler();
}
