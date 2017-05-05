package ca.rdmss.test.dflow.impl;

import ca.rdmss.dflow.TaskSync;
import ca.rdmss.dflow.TaskTransition;
import ca.rdmss.test.dflow.Suite_DFlow;

public class TestTaskResult2 extends TaskSync<TestContext2> {

	@Override
	public TaskTransition execute(TestContext2 context) throws Throwable {

		if( context.isPrint ){
			System.out.println(this);
		}
		
		Suite_DFlow.test.count( context.getTasktrack() ); // collect task path
		
		context.clean();
		
		return TaskTransition.Next;
	}

	@Override
	public String toString() {
		return "Test task result...";
	}
}
