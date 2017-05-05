package ca.rdmss.test.dflow;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.rdmss.util.TestHelper;

@RunWith(Suite.class)
@SuiteClasses({ 
	Test_DFlow_Unicast.class,
	Test_DFlow_Sync.class,
	Test_DFlow_Async.class,
	Test_DFlow_Flow.class,
	Test_DFlow_FlowException.class,
	Test_DFlow_MultiContext.class
})
public class Suite_DFlow { 
	/*
	 * How many time to stress?
	 */
	final public static int MAX_TRY = 2_000_000;
	final public static String THREAD_SET = "1,2,3,4";

	final static public TestHelper test = new TestHelper();  
}
