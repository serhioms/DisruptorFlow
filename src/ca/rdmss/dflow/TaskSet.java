package ca.rdmss.dflow;

final public class TaskSet<T> extends TaskSync<T>{

	private Task<T>[] set;

	private ExceptionHandler<T> exceptionHandler;
	
	@SafeVarargs
	public TaskSet(Task<T>... set) {
		this.set = set;
	}

	@SafeVarargs
	public TaskSet(ExceptionHandler<T> exceptionHandler, Task<T>... set) {
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
	public ExceptionHandler<T> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<T> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
