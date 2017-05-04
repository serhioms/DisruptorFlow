package ca.rdmss.dflow;

abstract public class TaskAsync<T> extends TaskSync<T>{
	
	@Override
	final public boolean isAsync() {
		return true;
	}
}
