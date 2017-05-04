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
	Test_DFlow_FlowException.class
})
public class Suite_DFlow { 
	/*
	 * How many time to stress?
	 */
	final public static int MAX_TRY = 2_000_000;
	final public static String THREAD_SET = "1,2,3,4"; //,5,6,7,8,9,10,12,16";

	final static public TestHelper test = new TestHelper();  
}
/*	i7-3630QM CPU@ 2.40Ghz (4 core, 8 logical processors, L1=256kb, l2=1mb, l3=6mb) -ea -Xms1g -Xmx1g

=== Test_DFlow_Unicast done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       301.0  mls 150.5   ns    150.500 pass: expected=2,000,000 actual=2,000,000
2       807.0  mls 403.5   ns    403.500 pass: expected=4,000,000 actual=4,000,000
3       1.5    sec 739.5   ns    739.500 pass: expected=6,000,000 actual=6,000,000
4       3.0    sec 1.5    mks   1482.500 pass: expected=8,000,000 actual=8,000,000
------- ---------- ---------- ----------

=== Test_DFlow_Sync done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       884.0  mls 442.0   ns    442.000 pass: expected=2,000,000 actual=2,000,000
2       1.8    sec 899.5   ns    899.500 pass: expected=4,000,000 actual=4,000,000
3       2.5    sec 1.3    mks   1263.500 pass: expected=6,000,000 actual=6,000,000
4       3.4    sec 1.7    mks   1689.500 pass: expected=8,000,000 actual=8,000,000
------- ---------- ---------- ----------

=== Test_DFlow_Async done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       1.9    sec 947.5   ns    947.500 pass: expected=2,000,000 actual=2,000,000
2       3.4    sec 1.7    mks   1695.500 pass: expected=4,000,000 actual=4,000,000
3       5.3    sec 2.7    mks   2670.000 pass: expected=6,000,000 actual=6,000,000
4       7.0    sec 3.5    mks   3518.000 pass: expected=8,000,000 actual=8,000,000
------- ---------- ---------- ----------

=== Test_DFlow_Flow done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       1.8    sec 903.5   ns    903.500 pass: expected=2,000,000 actual=2,000,000
2       4.0    sec 2.0    mks   2005.000 pass: expected=4,000,000 actual=4,000,000
3       5.3    sec 2.7    mks   2668.500 pass: expected=6,000,000 actual=6,000,000
4       7.4    sec 3.7    mks   3686.500 pass: expected=8,000,000 actual=8,000,000
------- ---------- ---------- ----------

===                           Test_DFlow_FlowException done 2,000,000 time(s) in   3.4 sec (  1.7 mks/try) === pass: expected=1,999,696 actual=1,999,696
*/
