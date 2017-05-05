package ca.rdmss.test.dflow;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.dflow.DisruptorFlow;
import ca.rdmss.dflow.TaskFlow;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dflow.impl.TestContext;
import ca.rdmss.test.dflow.impl.TestContext2;
import ca.rdmss.test.dflow.impl.TestContext3;
import ca.rdmss.test.dflow.impl.TestTask;
import ca.rdmss.test.dflow.impl.TestTask2;
import ca.rdmss.test.dflow.impl.TestTask3;
import ca.rdmss.test.dflow.impl.TestTaskAsync;
import ca.rdmss.test.dflow.impl.TestTaskAsync2;
import ca.rdmss.test.dflow.impl.TestTaskAsync3;
import ca.rdmss.test.dflow.impl.TestTaskResult;
import ca.rdmss.test.dflow.impl.TestTaskResult2;
import ca.rdmss.test.dflow.impl.TestTaskResult3;

@MultiTest(repeatNo=Test_DFlow_MultiContext.MAX_TRY, threadSet="5")
public class Test_DFlow_MultiContext {

	final public static int MAX_TRY = Suite_DFlow.MAX_TRY/3/5+1;
	
	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	DisruptorFlow<TestContext> dflow1 = new DisruptorFlow<TestContext>();
	DisruptorFlow<TestContext2> dflow2 = new DisruptorFlow<TestContext2>();
	DisruptorFlow<TestContext3> dflow3 = new DisruptorFlow<TestContext3>();
	
	@MultiBefore
	public void setUp() throws Exception {
		Suite_DFlow.test.clean();

		dflow1.start();
		dflow2.start();
		dflow3.start();
	}
	
	@MultiThread
	public void producer1(){
		dflow1.process(new TestContext(false), 
				new TestTask("1"),
				new TaskFlow<TestContext>(
						new TestTask("2"),
						new TestTaskAsync("3"),
						new TaskFlow<TestContext>(
								new TestTask("41"),
								new TestTask("42"),
								new TestTask("43")),
						new TestTask("5")),
				new TestTask("6"),
				new TestTaskResult()
				);
	}

	
	@MultiThread
	public void producer2(){
		dflow2.process(new TestContext2(false), 
				new TestTask2("1"),
				new TaskFlow<TestContext2>(
						new TestTask2("2"),
						new TestTaskAsync2("3"),
						new TaskFlow<TestContext2>(
								new TestTask2("41"),
								new TestTask2("42"),
								new TestTask2("43")),
						new TestTask2("5")),
				new TestTask2("6"),
				new TestTaskResult2()
				);
	}

	
	@MultiThread
	public void producer3(){
		dflow3.process(new TestContext3(false), 
				new TestTask3("1"),
				new TaskFlow<TestContext3>(
						new TestTask3("2"),
						new TestTaskAsync3("3"),
						new TaskFlow<TestContext3>(
								new TestTask3("41"),
								new TestTask3("42"),
								new TestTask3("43")),
						new TestTask3("5")),
				new TestTask3("6"),
				new TestTaskResult3()
				);
	}

	@Test
	public void test() throws Throwable {

		dflow1.stop();
		dflow2.stop();
		dflow3.stop();

		// 1 producers -> 1 consumer = 3 times
		int expected = MAX_TRY*1*1*3*rule.getThreadNo();

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
