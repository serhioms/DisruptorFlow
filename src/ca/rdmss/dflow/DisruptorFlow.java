package ca.rdmss.dflow;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.TimeoutException;

import ca.rdmss.dflow.impl.DafaultExceptionHandler;
import ca.rdmss.dflow.impl.UnicastDisruptor;

public class DisruptorFlow<T> {

	private UnicastDisruptor<T> async;
	private UnicastDisruptor<T> sync;
	
	private ExceptionHandler<T> exceptionHandler = new DafaultExceptionHandler<T>();

	public void start(){
		if( async != null || sync != null ){
			throw new RuntimeException("Disruptor already started!?");
		}
		
		async = new UnicastDisruptor<T>();
		sync = new UnicastDisruptor<T>(async);

		sync.setExceptionHandler(exceptionHandler);
		async.setExceptionHandler(exceptionHandler);

		async.startDisruptor();
		sync.startDisruptor();
	}
	
	public void stop(){
		try {
			sync.shutdown();
			sync = null;
			async.shutdown();
			async = null;
		} catch(NullPointerException e){
			throw new RuntimeException("Disruptor not started!?");
		}
	}
	
	public void stop(long timeout, TimeUnit timeUnit) throws TimeoutException{
		sync.shutdown(timeout, timeUnit);
		async.shutdown(timeout, timeUnit);
	}
	
	@SafeVarargs
	final public void process(T context, Task<T>... tasks){
		try {
			sync.onData(context, tasks);
		} catch(NullPointerException e){
			throw new RuntimeException("Disruptor not started!?");
		}
	}

	public void setExceptionHandler(ExceptionHandler<T> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
