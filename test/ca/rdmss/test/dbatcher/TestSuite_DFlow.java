package ca.rdmss.test.dbatcher;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.rdmss.util.TestHelper;

@RunWith(Suite.class)
@SuiteClasses({ 
	Test_DFlow_Single_Unicast.class,
	Test_DFlow_Single_Sync.class,
	Test_DFlow_Single_Async.class,
	Test_DFlow_Single_Set.class,
	Test_DFlow_Multi_Unicast.class,
	Test_DFlow_Multi_Sync.class,
	Test_DFlow_Multi_Async_.class,
	Test_DFlow_Multi_Set.class
})
public class TestSuite_DFlow { 
	/*
	 * How many time to stress?
	 */
	final public static int MAX_TRY = 2_000_000;
	final public static String THREAD_SET = "1,2,3"; //,4,5,6,7,8,9,10,12,16";

	final static public TestHelper test = new TestHelper();  
}
/*	i7-3630QM CPU@ 2.40Ghz (4 core, 8 logical processors, L1=256kb, l2=1mb, l3=6mb) -ea -Xms1g -Xmx1g

===                          Test_DFlow_Single_Unicast done 2,000,000 time(s) in 197.0 mls ( 98.5  ns/try) === pass: expected=2,000,000 actual=2,000,000
===                             Test_DFlow_Single_Sync done 2,000,000 time(s) in   1.1 sec (574.0  ns/try) === pass: expected=2,000,000 actual=2,000,000
===                            Test_DFlow_Single_Async done 2,000,000 time(s) in   1.7 sec (859.5  ns/try) === pass: expected=2,000,000 actual=2,000,000
===                              Test_DFlow_Single_Set done 2,000,000 time(s) in   1.9 sec (943.0  ns/try) === pass: expected=2,000,000 actual=2,000,000

=== Test_DFlow_Multi_Unicast done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       282.0  mls 141.0   ns    141.000 pass: expected=2,000,000 actual=2,000,000
2       777.0  mls 388.5   ns    388.500 pass: expected=4,000,000 actual=4,000,000
3       1.8    sec 878.5   ns    878.500 pass: expected=6,000,000 actual=6,000,000
------- ---------- ---------- ----------

=== Test_DFlow_Multi_Sync done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       815.0  mls 407.5   ns    407.500 pass: expected=2,000,000 actual=2,000,000
2       1.8    sec 881.0   ns    881.000 pass: expected=4,000,000 actual=4,000,000
3       2.7    sec 1.3    mks   1334.500 pass: expected=6,000,000 actual=6,000,000
------- ---------- ---------- ----------

=== Test_DFlow_Multi_Async_ done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       1.6    sec 778.0   ns    778.000 pass: expected=2,000,000 actual=2,000,000
2       3.0    sec 1.5    mks   1497.500 pass: expected=4,000,000 actual=4,000,000
3       4.8    sec 2.4    mks   2417.000 pass: expected=6,000,000 actual=6,000,000
------- ---------- ---------- ----------

=== Test_DFlow_Multi_Set done 2,000,000 time(s) ===
Threads Total      OneTry     OneTry(ns)
------- ---------- ---------- ----------
1       1.6    sec 787.0   ns    787.000 pass: expected=2,000,000 actual=2,000,000
2       3.4    sec 1.7    mks   1717.500 pass: expected=4,000,000 actual=4,000,000
3       4.8    sec 2.4    mks   2394.000 pass: expected=6,000,000 actual=6,000,000
------- ---------- ---------- ----------

*/
