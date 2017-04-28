package ca.rdmss.test.dbatcher;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.impl.UnicastDisruptor;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dbatcher.impl.TestContext;
import ca.rdmss.test.dbatcher.impl.TestHandler;

@MultiTest(repeatNo=TestSuite_DFlow.MAX_TRY)
public class Test_DFlow_Single_Unicast {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	TestContext context = new TestContext();
	
	UnicastDisruptor<TestContext> unicast;
	
	@MultiBefore
	public void setUp() throws Exception {
		
		TestSuite_DFlow.test.clean();
		
		unicast = new UnicastDisruptor<TestContext>(ProducerType.SINGLE, 
				new TestHandler()); // 1 consumer
		
		unicast.startDisruptor();
	}
	
	@MultiThread
	public void producer(){
		unicast.onData(context);
	}

	@Test
	public void test() throws Throwable {

		unicast.shutdown();
		
		// 1 producers -> 1 consumer
		int expected = TestSuite_DFlow.MAX_TRY*1*1;

		int actual = context.counter.get();
		
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
