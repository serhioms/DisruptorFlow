package ca.rdmss.dflow;

abstract public class TaskSync<T> implements Task<T> {

	@Override
	public boolean isAsync() {
		return false; // task is sync by default
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

	@Override
	public ExceptionHandler<T> getExceptionHandler() {
		throw new RuntimeException("getExceptionHandler() must be implemented for set only!");
	}
}
