# DisruptorFlow
Sequential sync/async task processing based on [LMax Disruptor](https://github.com/LMAX-Exchange/disruptor/blob/master/docs/Disruptor.docx)

This is a regular Maven project.

## DisruptorFlow processing diagram

![alt text](https://github.com/serhioms/DisruptorFlow/blob/master/result/DisruptorFlow%20Diagram.png)


## Sync Task

Sync task is descendent of TaskSync. Sequential processor start it, wait for the end then start next task in a sequence. All synchronous tasks run in one thread.


## Async Task

Async task is descendent of TaskAsync. Sequential processor start then immedeately start next task in a sequence. Asynchronous tasks perform in separate threads in parallel of synchronous tasks.


## Task Set

Task set is an object of class TaskSet. Constructor of this class accept any amount of sync/async tasks.


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
