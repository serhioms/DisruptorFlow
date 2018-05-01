package ca.rdmss.test.dflow.impl;

import java.util.concurrent.atomic.AtomicInteger;

import ca.rdmss.dflow.impl.ContextEvent;

public class TestContext extends ContextEvent<TestContext> {

	final public AtomicInteger counter = new AtomicInteger(0);
	final public boolean isPrint;

	private String tasktrack = "";
	
	public TestContext() {
		this(false);
	}

	public TestContext(boolean isPrint) {
		this.setContext(this);
		this.isPrint = isPrint;
	}

	public void track(String id){
		synchronized( this ){
			tasktrack += id;
		}
		counter.incrementAndGet();
	}
	
	public String getTasktrack(){
		return tasktrack;
	}
	
	public void clean() {
		tasktrack = null;
	}
}
