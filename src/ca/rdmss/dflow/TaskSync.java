package ca.rdmss.dflow;

import ca.rdmss.dflow.impl.AsyncContext;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

abstract public class TaskSync<T> implements Task<T> {

	@Override
	public void publishAsync(T context) {
		throw new RuntimeException("publishAsync() must be implemented for AsyncTask only!");
	}

	@Override
	public boolean isAsync() {
		return false; // task is sync by default
	}

	@Override
	public void setDisruptor(LMaxDisruptor<AsyncContext<T>> disruptor) {
		// make sense to implement for AsyncTask only
	}

	@Override
	public boolean isSet() {
		return false; // task is simple by default (not a set of tasks)
	}

	@Override
	public Task<T>[] getSet() {
		throw new RuntimeException("getSet() must be implemented for set only!");
	}

	@Override
	public void setSet(Task<T>[] tasks) {
		throw new RuntimeException("setSet() must be implemented for set only!");
	}

	
}
