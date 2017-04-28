package ca.rdmss.test.dbatcher.impl;

import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.TaskSync;
import ca.rdmss.test.dbatcher.TestSuite_DFlow;

public class TestTaskResult extends TaskSync<TestContext> {

	@Override
	public boolean execute(TestContext context) throws Throwable {
		
		TestSuite_DFlow.test.count( context.getTasktrack() ); // collect task path
		
		return Task.CONTINUE;
	}
}
