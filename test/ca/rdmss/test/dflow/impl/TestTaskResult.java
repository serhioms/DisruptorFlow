package ca.rdmss.test.dflow.impl;

import ca.rdmss.dflow.TaskSync;
import ca.rdmss.dflow.TaskTransition;
import ca.rdmss.test.dflow.Suite_DFlow;

public class TestTaskResult extends TaskSync<TestContext> {

	@Override
	public TaskTransition execute(TestContext context) throws Throwable {

		if( context.isPrint ){
			System.out.println(this);
		}
		
		Suite_DFlow.test.count( context.getTasktrack() ); // collect task path
		
		return TaskTransition.Next;
	}

	@Override
	public String toString() {
		return "Test task result...";
	}
}
