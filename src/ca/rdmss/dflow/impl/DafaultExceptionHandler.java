package ca.rdmss.dflow.impl;

import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.dflow.TaskTransition;

public class DafaultExceptionHandler<T> implements ExceptionHandler<T> {
	@Override
	public TaskTransition handleTaskException(T context, Throwable ex) {
		ex.printStackTrace(System.err);
		return TaskTransition.Fail;
	}
}
