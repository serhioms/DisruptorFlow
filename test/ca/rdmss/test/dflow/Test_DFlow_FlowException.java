package ca.rdmss.test.dflow;

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.dflow.DisruptorFlow;
import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.dflow.TaskFlow;
import ca.rdmss.dflow.TaskTransition;
import ca.rdmss.dflow.impl.ContextEvent;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dflow.impl.TestContext;
import ca.rdmss.test.dflow.impl.TestTask;
import ca.rdmss.test.dflow.impl.TestTaskAsync;
import ca.rdmss.test.dflow.impl.TestTaskResult;

@MultiTest(repeatNo=Test_DFlow_FlowException.MAX_TRY)
public class Test_DFlow_FlowException {

	final public static int MAX_TRY = Suite_DFlow.MAX_TRY;
	
	final public static int MAX_NOT_COMPLETED = 100;
	final public static int MAX_EXCEPTION = 4;
	
	final public static AtomicInteger END = new AtomicInteger(MAX_NOT_COMPLETED);
	final public static AtomicInteger STOP = new AtomicInteger(MAX_NOT_COMPLETED);
	final public static AtomicInteger FAILED = new AtomicInteger(MAX_NOT_COMPLETED);
	final public static AtomicInteger EXCEPTION = new AtomicInteger(MAX_EXCEPTION);

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
	
	@MultiBefore
	public void setUp() throws Exception {
		Suite_DFlow.test.clean();

		dflow.setExceptionHandler(new ExceptionHandler<ContextEvent<TestContext>>(){

			@Override
			public TaskTransition handleTaskException(ContextEvent<TestContext> event, Throwable ex) {
				System.err.printf("%d: ", event.getContext().counter.get());
				ex.printStackTrace(System.err);
				return TaskTransition.Fail;
			}
			
		});
		
		dflow.start();
	}
	
	@MultiThread
	public void producer(){
		dflow.process(new TestContext(false), 
				new TestTask("1"),
				new TaskFlow<TestContext>(
						new TestTask("2", TaskTransition.End, END),
						new TestTaskAsync("3"),
						new TaskFlow<TestContext>(
								new TestTask("41", TaskTransition.Stop, STOP),
								new TestTask("42", TaskTransition.Fail, FAILED),
								new TestTask("43", false, EXCEPTION)),
						new TestTask("5")),
				new TestTask("6"),
				new TestTaskResult()
				);
	}

	@Test
	public void test() throws Throwable {

		dflow.stop();
		
		// 1 producers -> 1 consumer
		int expected = MAX_TRY*1*1 - MAX_NOT_COMPLETED*3-MAX_EXCEPTION;

		int actual = Suite_DFlow.test.getTotal();
		
		boolean isFailed = actual != expected;
		
		if( isFailed ){ 
			rule.helper.result += String.format(" %s: expected=%,d actual=%,d", "failed", expected, actual);
		} else {
			rule.helper.result += String.format(" %s: expected=%,d actual=%,d", "pass", expected, actual);
		}

		System.out.printf("%s\n", rule.getReport());

		if( isFailed ){
			fail("Check log");
		}
	}
}
