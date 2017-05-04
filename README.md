# DisruptorFlow
Sequential sync/async task processing based on [LMax Disruptor](https://github.com/LMAX-Exchange/disruptor/blob/master)

This is a regular Maven project.

## DisruptorFlow processing diagram

![alt text](https://github.com/serhioms/DisruptorFlow/blob/master/result/DisruptorFlow.png)


## Sync Task

Sync task is descendent from TaskSync<T>. Flow disruptor start it, wait for the end then start next task in a sequence. All synchronous tasks run in the same thread.

    public class TestSyncTask extends TaskSync<TestContext> {
    	int id = tasskIdGenerator.incrementAndGet();
    	
        @Override
        public TaskTransition execute(TestContext context) throws Throwable {
            System.out.printf("%d) Hi from Sync!\n", id);
            return TaskTransition.Next;
        }
    }

## Async Task

Async task is descendant from TaskAsync<T>. Flow disruptor start it in new thread then immedeately start next task in a sequence. All asynchronous tasks run in separate threads in parallel of synchronous tasks.

    public class TestAsyncTask extends TaskAsync<TestContext> {
    	int id = tasskIdGenerator.incrementAndGet();

    	@Override
        public TaskTransition execute(TestContext context) throws Throwable {
            System.out.printf("%d) Hi from Async!\n", id);
            return TaskTransition.Next;
        }
    }
    
## Flow
                
Flow (or subflow) is descendant from TaskFlow<T>. You can set many sync/asyn tasks for the flow in constructor or vie setter. All this task will be executed sequentially. You can set specific exception handler for the flow/subflow.

	    TaskFlow<TestContext> flow = new TaskFlow<TestContext>(
	            new TestSyncTask(),
	            new TestAsyncTask(),
	            new TestSyncTask());

## Flow Disruptor

Flow disruptor is generic class which perform task flow execution in pipeline pattern. Pipeline pattern described [here](https://github.com/LMAX-Exchange/disruptor/blob/master/docs/Disruptor.docx). The only parameter of flow disruptor is context class.

	    DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
        ***
	    dflow.start();
        ***
	    dflow.stop();

## Exception Handler

Exception Handler is generic class which flow disruptor call on any exception happen during task execution. There is default handler implemented as simple as System.err messenger.

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


### Using in test

[example]()
