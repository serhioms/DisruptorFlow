# Flow Disruptor
Sequential sync/async task processing based on [LMax Disruptor](https://github.com/LMAX-Exchange/disruptor)

This is a regular Maven project.

## Flow disruptor processing diagram

![alt text](https://github.com/serhioms/DisruptorFlow/blob/master/result/DisruptorFlow.png)


## Sync Task

Sync task is descendent from `TaskSync< T >`. Flow disruptor start it, wait for the end then start next task in a sequence. All synchronous tasks run in the same thread.

    public class TestSyncTask extends TaskSync<TestContext> {
    	int id = tasskIdGenerator.incrementAndGet();
    	
        @Override
        public TaskTransition execute(TestContext context) throws Throwable {
            System.out.printf("%d) Hi from Sync!\n", id);
            return TaskTransition.Next;
        }
    }

## Async Task

Async task is descendant from `TaskAsync< T >`. Flow disruptor start it in new thread then immedeately start next task in a sequence. All asynchronous tasks run in separate threads in parallel of synchronous tasks.

    public class TestAsyncTask extends TaskAsync<TestContext> {
    	int id = tasskIdGenerator.incrementAndGet();

    	@Override
        public TaskTransition execute(TestContext context) throws Throwable {
            System.out.printf("%d) Hi from Async!\n", id);
            return TaskTransition.Next;
        }
    }
    
## Flow
                
Flow (or subflow) is descendant from `TaskFlow< T >`. You can set many sync/asyn tasks for the flow in constructor or vie setter. All this task will be executed sequentially. You can set specific exception handler for the flow/subflow.

	    TaskFlow<TestContext> flow = new TaskFlow<TestContext>(
	            new TestSyncTask(),
	            new TestAsyncTask(),
	            new TestSyncTask());

## Flow Disruptor

Flow disruptor is generic class which perform task flow execution vie pipeline pattern. Pipeline pattern described [here](https://github.com/LMAX-Exchange/disruptor/blob/master/docs/Disruptor.docx). The only parameter of flow disruptor is context class.

	    DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
        ***
	    dflow.start();
        ***
	    dflow.stop();

## Exception Handler

Exception Handler is generic class which flow disruptor call on any exception happen during task execution. There is a default handler implemented as simple as System.err messenger.

	    dflow.setExceptionHandler(new ExceptionHandler<TestContext>(){
	        @Override
	        public TaskTransition handleTaskException(TestContext context, Throwable ex) {
	            System.err.printf("Flow failed due to %s\n", ex.getMessage());
	            return TaskTransition.Fail;
	        }
	    });

## Flow context

Flow context is any java class. Context instance must be provided when you run particular flow. This instance will be propagated among all flow task execution.

    public class TestContext {
    	***
    }
    
    dflow.run(new TestContext(), flow);


### [Full Example](https://github.com/serhioms/DisruptorFlow/blob/master/test/ca/rdmss/test/dflow/DFlowExample.java)

Typical output:

	1) Hi from Sync!
	3) Hi from Sync!
	2) Hi from Async!
	
Async task is the second in flow but usually executed bit later then third sync task.

    
## Usage

### Declaring dependency
#### Maven

```xml
    <dependencies>
        <!-- https://mvnrepository.com/artifact/com.lmax/disruptor -->
        <dependency>
          <groupId>com.lmax</groupId>
          <artifactId>disruptor</artifactId>
          <version>3.3.6</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/ca.rdmss/multi-test -->
        <dependency>
          <groupId>ca.rdmss</groupId>
          <artifactId>multi-test</artifactId>
          <version>1.2.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.12</version>
        </dependency>
    </dependencies>
```


### [DisruptorFlow Test Suit](https://github.com/serhioms/DisruptorFlow/blob/master/test/ca/rdmss/test/dflow/Suite_DFlow.java)

Output for i7-3630QM CPU@ 2.40Ghz (4 core, 8 logical processors, L1=256kb, l2=1mb, l3=6mb) -ea -Xms1g -Xmx1g

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

===Test_DFlow_FlowException done 2,000,000 time(s) in   3.4 sec (  1.7 mks/try) === pass: expected=1,999,696 actual=1,999,696


