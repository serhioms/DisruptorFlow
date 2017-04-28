# DisruptorFlow
Sequential sync/async task processing based on [LMax Disruptor](https://github.com/LMAX-Exchange/disruptor/blob/master/docs/Disruptor.docx)


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


## DisruptorFlow diagram

![alt text](https://github.com/serhioms/DisruptorFlow/blob/master/result/DisruptorFlow%20Diagram.png)
