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
		async = new UnicastDisruptor<T>();
		sync = new UnicastDisruptor<T>(async);

		sync.setExceptionHandler(exceptionHandler);
		async.setExceptionHandler(exceptionHandler);

		async.startDisruptor();
		sync.startDisruptor();
	}
	
	public void stop(){
		sync.shutdown();
		async.shutdown();
	}
	
	public void stop(long timeout, TimeUnit timeUnit) throws TimeoutException{
		sync.shutdown(timeout, timeUnit);
		async.shutdown(timeout, timeUnit);
	}
	
	@SafeVarargs
	final public void run(T context, Task<T>... tasks){
		sync.onData(context, tasks);
	}

	public void setExceptionHandler(ExceptionHandler<T> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
