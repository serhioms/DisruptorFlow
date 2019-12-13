package ca.rdmss.test.dflow.impl;

import java.util.concurrent.atomic.AtomicInteger;

import ca.rdmss.dflow.TaskSync;
import ca.rdmss.dflow.TaskTransition;

public class TestTask extends TaskSync<TestContext> {

	final private String taskId;
	final private boolean isException;
	final private TaskTransition transition;
	private AtomicInteger fail;
	
	public TestTask(String taskId) {
		this(taskId, true);
	}

	public TestTask(String taskId, boolean isNotException) {
		this(taskId, isNotException, null);
	}

	public TestTask(String taskId, TaskTransition transition) {
		this(taskId, transition, null);
	}

	public TestTask(String taskId, TaskTransition transition, AtomicInteger fail) {
		this.taskId = taskId;
		this.transition = transition;
		this.fail = fail;
		this.isException = false;
	}

	public TestTask(String taskId, boolean isNotException, AtomicInteger fail) {
		this.taskId = taskId;
		this.isException = !isNotException;
		this.fail = fail;
		this.transition = null;
	}

	@Override
	public TaskTransition execute(TestContext context) throws Throwable {
		
		if( context.isPrint ){
			System.out.println(this);
		}

		context.track(taskId);
		
		if( isException ){
			context.track(taskId);
			if( fail == null ){
				System.out.println(String.format(" %s get failed by test reason!", this));
			} else if( fail.get() > 0 ){
				fail.decrementAndGet();
				System.out.println(String.format(" %s get failed by test reason!", this));
			}
			return TaskTransition.End;
		} else if( transition != null ){
			if( fail == null ){
				return transition;
			} else if( fail.get() > 0 ){
				fail.decrementAndGet();
				return transition;
			}
		}
		return TaskTransition.Next;
	}

	@Override
	public String toString() {
		return String.format("Test  sync #%s", taskId);
	}
}
