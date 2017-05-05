package ca.rdmss.test.dflow.impl;

import ca.rdmss.dflow.TaskSync;
import ca.rdmss.dflow.TaskTransition;
import ca.rdmss.test.dflow.Suite_DFlow;

public class TestTaskResult3 extends TaskSync<TestContext3> {

	@Override
	public TaskTransition execute(TestContext3 context) throws Throwable {

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
