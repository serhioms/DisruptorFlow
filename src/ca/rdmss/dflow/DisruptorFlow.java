package ca.rdmss.dflow;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.impl.DafaultExceptionHandler;
import ca.rdmss.dflow.impl.UnicastDisruptor;

public class DisruptorFlow<T> {

	private UnicastDisruptor<T> async;
	private UnicastDisruptor<T> sync;
	
	private ProducerType producerType = ProducerType.MULTI;

	private ExceptionHandler<T> exceptionHandler = new DafaultExceptionHandler<T>();

	public void start(){
		async = new UnicastDisruptor<T>(ProducerType.MULTI);
		sync = new UnicastDisruptor<T>(producerType, async); // TODO: Actually let's put MULTI only

		sync.setExceptionHandler(exceptionHandler);
		async.setExceptionHandler(exceptionHandler);

		async.startDisruptor();
		sync.startDisruptor();
	}
	
	public void shutdown(){
		sync.shutdown();
		async.shutdown();
	}
	
	public void shutdown(long timeout, TimeUnit timeUnit) throws TimeoutException{
		sync.shutdown(timeout, timeUnit);
		async.shutdown(timeout, timeUnit);
	}
	
	@SafeVarargs
	final public void onData(T context, Task<T>... tasks){
		sync.onData(context, tasks);
	}
	
	public ProducerType getProducerType() {
		return producerType;
	}

	public void setProducerType(ProducerType producerType) {
		this.producerType = producerType;
	}
	
	public void setExceptionHandler(ExceptionHandler<T> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
