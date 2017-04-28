package ca.rdmss.test.dbatcher;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.DisruptorFlow;
import ca.rdmss.multitest.annotation.MultiBefore;
import ca.rdmss.multitest.annotation.MultiTest;
import ca.rdmss.multitest.annotation.MultiThread;
import ca.rdmss.multitest.junitrule.MultiTestRule;
import ca.rdmss.test.dbatcher.impl.TestContext;
import ca.rdmss.test.dbatcher.impl.TestTask;
import ca.rdmss.test.dbatcher.impl.TestTaskAsync;
import ca.rdmss.test.dbatcher.impl.TestTaskResult;

@MultiTest(repeatNo=TestSuite_DFlow.MAX_TRY)
public class Test_DFlow_Single_Async {

	@Rule
	public MultiTestRule rule = new MultiTestRule(this);

	DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
	
	@MultiBefore
	public void setUp() throws Exception {
		TestSuite_DFlow.test.clean();
		
		dflow.setProducerType(ProducerType.SINGLE);
		dflow.start();
	}
	
	@MultiThread
	public void producer1(){
		dflow.onData(new TestContext(), 
				new TestTask("1"),
				new TestTask("2"),
				new TestTaskAsync("3"),
				new TestTask("4"),
				new TestTask("5"),
				new TestTaskResult()
				);
	}
	
	@Test
	public void test() throws Throwable {

		dflow.shutdown();
		
		// 1 producers -> 1 consumer
		int expected = TestSuite_DFlow.MAX_TRY*1*1;

		int actual = TestSuite_DFlow.test.getTotal();
		
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
	}}
