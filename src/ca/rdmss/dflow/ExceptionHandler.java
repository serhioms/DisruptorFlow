package ca.rdmss.dflow;

public interface ExceptionHandler<T> {
	public TaskTransition handleTaskException(T t, Throwable ex);
}
