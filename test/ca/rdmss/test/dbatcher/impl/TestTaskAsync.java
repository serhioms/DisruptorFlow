package ca.rdmss.test.dbatcher.impl;

import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.TaskAsync;

public class TestTaskAsync extends TaskAsync<TestContext> {

	final private String taskId;
	
	public TestTaskAsync(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public boolean execute(TestContext context) throws Throwable {
		context.track(taskId);
		return Task.CONTINUE;
	}
}
