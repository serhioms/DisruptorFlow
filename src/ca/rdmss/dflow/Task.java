package ca.rdmss.dflow;

public interface Task<T> {

	// Task execution interface
	public TaskTransition execute(T context) throws Throwable;
	
	// Task can be sync or async:  
	// sync  - start in same thread and wait for the end then start next one. 
	// async - start in new thread and no wait for the end but start next one. 
	public boolean isAsync();
	
	// Task can be simple (false) or set of tasks (true)
	public boolean isSet();
	
	// setter/getter for the set
	public Task<T>[] getSet();
	public void setSet(Task<T>[] tasks);
	
	// Task/flow exception handler
	public ExceptionHandler<T> getExceptionHandler();
}
