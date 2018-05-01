package ca.rdmss.test.dflow.impl;

import java.util.concurrent.atomic.AtomicInteger;

import ca.rdmss.dflow.impl.ContextEvent;

public class TestContext3 extends ContextEvent<TestContext3> {

	final public AtomicInteger counter = new AtomicInteger(0);
	final public boolean isPrint;

	private String tasktrack = "";
	
	public TestContext3() {
		this(false);
	}

	public TestContext3(boolean isPrint) {
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