package ca.rdmss.test.dflow.impl;

import java.util.concurrent.atomic.AtomicInteger;

public class TestContext2 {

	final public AtomicInteger counter = new AtomicInteger(0);
	final public boolean isPrint;

	private String tasktrack = "";
	
	public TestContext2() {
		this(false);
	}

	public TestContext2(boolean isPrint) {
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