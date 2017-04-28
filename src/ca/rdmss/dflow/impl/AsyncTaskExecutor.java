package ca.rdmss.dflow.impl;

import ca.rdmss.dflow.TaskSync;

final public class AsyncTaskExecutor<T> extends TaskSync<AsyncContext<T>> {

	@Override
	public boolean execute(AsyncContext<T> acontext) throws Throwable {
		return acontext.task.execute(acontext.context);
	}
}
