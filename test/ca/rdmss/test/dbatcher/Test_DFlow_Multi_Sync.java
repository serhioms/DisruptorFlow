package ca.rdmss.test.dbatcher;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.DisruptorFlow;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiEndOfSet;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dbatcher.impl.TestContext;
import ca.rdmss.test.dbatcher.impl.TestTask;
import ca.rdmss.test.dbatcher.impl.TestTaskResult;

@MultiTest(repeatNo=TestSuite_DFlow.MAX_TRY, threadSet=TestSuite_DFlow.THREAD_SET)
public class Test_DFlow_Multi_Sync {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);
	
	DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
	
	@MultiBefore
	public void setUp() throws Exception {
		dflow.setProducerType(ProducerType.MULTI);
		dflow.start();
	}
	
	@MultiThread
	public void producer(){
		dflow.onData(new TestContext(), 
				new TestTask("1"),
				new TestTask("2"),
				new TestTask("3"),
				new TestTask("4"),
				new TestTask("5"),
				new TestTaskResult()
				);
	}

	boolean isFailed;

	@MultiEndOfSet
	public void endOfSet(){

		dflow.shutdown();
		
		// N producers -> 1 consumer
		int expected = TestSuite_DFlow.MAX_TRY*rule.getThreadNo()*1;

		int actual = TestSuite_DFlow.test.getTotal();
		
		if( actual != expected ){ 
			isFailed = true;
			rule.helper.result += String.format(" %s: expected=%,d actual=%,d", "failed", expected, actual);
		} else {
			rule.helper.result += String.format(" %s: expected=%,d actual=%,d", "pass", expected, actual);
		}
		
		// Prepare for next set
		TestSuite_DFlow.test.clean();

		dflow.start();
	}

	@Test
	public void test() throws Throwable {

		dflow.shutdown();

		System.out.printf("%s\n", rule.getReport());

		if( isFailed ){
			fail("Check log");
		}
	}
}
