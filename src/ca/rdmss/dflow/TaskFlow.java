package ca.rdmss.dflow;

import ca.rdmss.dflow.impl.ContextEvent;

final public class TaskFlow<T> extends TaskSync<T>{

	private Task<T>[] set;

	private ExceptionHandler<ContextEvent<T>> exceptionHandler;
	
	@SafeVarargs
	public TaskFlow(Task<T>... set) {
		this.set = set;
	}

	@SafeVarargs
	public TaskFlow(ExceptionHandler<ContextEvent<T>> exceptionHandler, Task<T>... set) {
		this.exceptionHandler = exceptionHandler;
		this.set = set;
	}

	public Task<T>[] getSet() {
		return set;
	}

	public void setSet(Task<T>[] set) {
		this.set = set;
	}

	@Override
	final public TaskTransition execute(T context) throws Throwable {
		throw new RuntimeException("execute() can't be applied to set");
	}

	@Override
	final public boolean isSet() {
		return true;
	}

	@Override
	public ExceptionHandler<ContextEvent<T>> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<ContextEvent<T>> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
