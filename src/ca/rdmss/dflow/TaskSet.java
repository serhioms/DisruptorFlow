package ca.rdmss.dflow;

final public class TaskSet<T> extends TaskSync<T>{

	private Task<T>[] set;

	@SafeVarargs
	public TaskSet(Task<T>... set) {
		this.set = set;
	}

	public Task<T>[] getSet() {
		return set;
	}

	public void setSet(Task<T>[] set) {
		this.set = set;
	}

	@Override
	final public boolean execute(T context) throws Throwable {
		throw new RuntimeException("execute() can't be applied to set");
	}

	@Override
	final public void publishAsync(T context) {
		throw new RuntimeException("publishAsync() can't be applied to set");
	}

	@Override
	final public boolean isSet() {
		return true;
	}
}
