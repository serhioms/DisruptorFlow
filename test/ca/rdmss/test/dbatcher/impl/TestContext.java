package ca.rdmss.test.dbatcher.impl;

import java.util.concurrent.atomic.AtomicInteger;

public class TestContext {

	final public AtomicInteger counter = new AtomicInteger(0);

	private String tasktrack = "";
	
	public void track(String id){
		synchronized( this ){
			tasktrack += id;
		}
		counter.incrementAndGet();
	}
	
	public String getTasktrack(){
		return tasktrack;
	}
	
}
