package ca.rdmss.dflow;

public interface ExceptionHandler<T> {
	public TaskTransition handleTaskException(T context, Throwable ex);
}
