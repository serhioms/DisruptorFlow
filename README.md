# DisruptorFlow
Sequential sync/async task processing based on [LMax Disruptor](https://github.com/LMAX-Exchange/disruptor/blob/master/docs/Disruptor.docx)

This is a regular Maven project.

## DisruptorFlow processing diagram

![alt text](https://github.com/serhioms/DisruptorFlow/blob/master/result/DisruptorFlow.png)


## Sync Task

Sync task is descendent of TaskSync. Disruptor flow start it, wait for the end then start next task in a sequence. All synchronous tasks run in the same thread.

    public class TestSyncTask extends TaskSync<TestContext> {
    	int id = tasskIdGenerator.incrementAndGet();
    	
        @Override
        public TaskTransition execute(TestContext context) throws Throwable {
            System.out.printf("%d) Hi from Sync!\n", id);
            return TaskTransition.Next;
        }
    }

## Async Task

Async task is descendent of TaskAsync.  Disruptor flow processor start it then immedeately start next task in a sequence. Asynchronous tasks perform in separate threads in parallel of synchronous tasks.

    public class TestAsyncTask extends TaskAsync<TestContext> {
    	int id = tasskIdGenerator.incrementAndGet();

    	@Override
        public TaskTransition execute(TestContext context) throws Throwable {
            System.out.printf("%d) Hi from Async!\n", id);
            return TaskTransition.Next;
        }
    }
    
## Flow
                
Flow is descendent of class TaskFlow. Constructor of this class accept any amount of sync/async tasks. Also you can set specific exception handler for the flow.

	    TaskFlow<TestContext> flow = new TaskFlow<TestContext>(
	            new TestSyncTask(),
	            new TestAsyncTask(),
	            new TestSyncTask());

## Disruptor Flow

Disruptor flow is a generic task processor class. You have to specify which class to be a context for all your task. Also you can set default exception handler (System.out handler predefined).

	    DisruptorFlow<TestContext> dflow = new DisruptorFlow<TestContext>();
        ***
	    dflow.start();
        ***
	    dflow.shutdown();

## Exception Handler

Exception Handler is a generic class which invokes by disruptor flow processor in case of any exception happen during task execution. It provide the flow context.

	    dflow.setExceptionHandler(new ExceptionHandler<TestContext>(){
	        @Override
	        public TaskTransition handleTaskException(TestContext context, Throwable ex) {
	            System.err.printf("Flow failed due to %s\n", ex.getMessage());
	            return TaskTransition.Fail;
	        }
	    });

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
