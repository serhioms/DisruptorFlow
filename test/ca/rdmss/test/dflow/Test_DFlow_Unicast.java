package ca.rdmss.test.dflow;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.dflow.impl.UnicastDisruptor;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiEndOfSet;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dflow.impl.TestContext;
import ca.rdmss.test.dflow.impl.TestHandler;

@MultiTest(repeatNo=Suite_DFlow.MAX_TRY, threadSet=Suite_DFlow.THREAD_SET)
public class Test_DFlow_Unicast {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	TestContext context = new TestContext();
	
	UnicastDisruptor<TestContext> unicast;
	
	@MultiBefore
	public void setUp() throws Exception {
		
		Suite_DFlow.test.clean();
		
		unicast = new UnicastDisruptor<TestContext>(new TestHandler()); // 1 consumer
		
		unicast.startDisruptor();
	}
	
	@MultiThread
	public void producer(){
		unicast.onData(context);
	}

	@MultiEndOfSet
	public void endOfSet(){
		
		unicast.shutdown();

		// N producers -> 1 consumer
		int expected = Suite_DFlow.MAX_TRY*rule.getThreadNo()*1;

		int actual = context.counter.get();
		
		if( actual != expected ){ 
			rule.addFailed(expected, actual);
		} else {
			rule.addPass(expected, actual);
		}
		
		// Prepare for next set
		context.counter.set(0);
		
		// Initialize new unicast
		unicast = new UnicastDisruptor<TestContext>(new TestHandler()); // 1 consumer
		
		unicast.startDisruptor();
	}

	@Test
	public void test() throws Throwable {

		unicast.shutdown();

		System.out.printf("%s\n", rule.getReport());

		if( rule.isFailed() ){
			fail("Check log");
		}
	}
}
