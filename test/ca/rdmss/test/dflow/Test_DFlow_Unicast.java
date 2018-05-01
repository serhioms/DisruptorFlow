package ca.rdmss.test.dflow;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import ca.rdmss.dflow.impl.ContextEvent;
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
		
		unicast = new UnicastDisruptor<TestContext>(new TestHandler()) {
			@Override
			public void process(TestContext t) {
				if( t.getContext() == null) {
					System.currentTimeMillis();
				}
				if( t.getTasks() == null) {
					System.currentTimeMillis();
				}
			}
		}; // 1 consumer
		
		unicast.start();
	}
	
	@MultiThread
	public void producer(){
		unicast.publish(context);
	}

	boolean isFailed;

	@MultiEndOfSet
	public void endOfSet(){
		
		unicast.shutdown();

		// N producers -> 1 consumer
		int expected = Suite_DFlow.MAX_TRY*rule.getThreadNo()*1;

		int actual = context.counter.get();
		
		if( actual != expected ){ 
			isFailed = true;
			rule.helper.result += String.format(" %s: expected=%,d actual=%,d", "failed", expected, actual);
		} else {
			rule.helper.result += String.format(" %s: expected=%,d actual=%,d", "pass", expected, actual);
		}
		
		// Prepare for next set
		context.counter.set(0);
		
		// Initialize new unicast
		unicast = new UnicastDisruptor<TestContext>(new TestHandler()) {

			@Override
			public void process(TestContext t) {
			}
			
		}; // 1 consumer
		
		unicast.start();
	}

	@Test
	public void test() throws Throwable {

		unicast.shutdown();

		System.out.printf("%s\n", rule.getReport());

		if( isFailed ){
			fail("Check log");
		}
	}
}
