package ca.rdmss.test.dflow;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.dflow.DisruptorFlow;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiEndOfSet;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dflow.impl.TestContext;
import ca.rdmss.test.dflow.impl.TestTask;
import ca.rdmss.test.dflow.impl.TestTaskResult;

@MultiTest(repeatNo=Suite_DFlow.MAX_TRY, threadSet=Suite_DFlow.THREAD_SET)
public class Test_DFlow_Sync {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);
	
	DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
	
	@MultiBefore
	public void setUp() throws Exception {
		dflow.start();
		// Prepare for next set
		Suite_DFlow.test.clean();	}
	
	@MultiThread
	public void producer(){
		dflow.process(new TestContext(), 
				new TestTask("1"),
				new TestTask("2"),
				new TestTask("3"),
				new TestTask("4"),
				new TestTask("5"),
				new TestTaskResult()
				);
	}

	@MultiEndOfSet
	public void endOfSet(){

		dflow.stop();
		
		// N producers -> 1 consumer
		int expected = Suite_DFlow.MAX_TRY*rule.getThreadNo()*1;

		int actual = Suite_DFlow.test.getTotal();
		
		if( actual != expected ){ 
			rule.addFailed(expected, actual);
		} else {
			rule.addPass(expected, actual);
		}
		
		// Prepare for next set
		Suite_DFlow.test.clean();

		dflow.start();
	}

	@Test
	public void test() throws Throwable {

		dflow.stop();

		System.out.printf("%s\n", rule.getReport());

		if( rule.isFailed() ){
			fail("Check log");
		}
	}
}
