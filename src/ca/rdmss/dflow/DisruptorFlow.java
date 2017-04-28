package ca.rdmss.dflow;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.impl.AsyncContext;
import ca.rdmss.dflow.impl.AsyncTaskExecutor;
import ca.rdmss.dflow.impl.UnicastDisruptor;

public class DisruptorFlow<T> {

	private UnicastDisruptor<AsyncContext<T>> async;
	private UnicastDisruptor<T> sync;
	
	private ProducerType producerType = ProducerType.MULTI;
	
	public void start(){
		async = new UnicastDisruptor<AsyncContext<T>>(producerType, new AsyncTaskExecutor<T>());
		sync = new UnicastDisruptor<T>(async);

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
}
