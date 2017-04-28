package ca.rdmss.test.dbatcher.impl;

import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.TaskSync;

public class TestTask extends TaskSync<TestContext> {

	final private String taskId;
	
	public TestTask(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public boolean execute(TestContext context) throws Throwable {
		context.track(taskId);
		return Task.CONTINUE;
	}
}
