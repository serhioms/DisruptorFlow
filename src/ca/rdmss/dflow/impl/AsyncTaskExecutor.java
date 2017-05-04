package ca.rdmss.dflow.impl;

import ca.rdmss.dflow.TaskSync;
import ca.rdmss.dflow.TaskTransition;

final public class AsyncTaskExecutor<T> extends TaskSync<AsyncContext<T>> {

	@Override
	public TaskTransition execute(AsyncContext<T> acontext) throws Throwable {
		return acontext.task.execute(acontext.context); // It does not matter what async task returns - ignored
	}
	
}
