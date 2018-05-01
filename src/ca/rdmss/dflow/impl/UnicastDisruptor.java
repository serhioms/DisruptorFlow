package ca.rdmss.dflow.impl;

import java.util.concurrent.atomic.AtomicReference;

import com.lmax.disruptor.EventHandler;

import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.disruptor.LMaxDisruptor;

abstract public class UnicastDisruptor<T> extends LMaxDisruptor<T> {

	private ExceptionHandler<T> exceptionHandler = new DafaultExceptionHandler<T>();
	
	abstract public void process(T t);
	
	@SafeVarargs
	public UnicastDisruptor(EventHandler<AtomicReference<T>>... handler) {
		super();
		subscribeConsumer(handler);
	}	

	public UnicastDisruptor() {
		super();
		subscribeConsumer(new EventHandler<AtomicReference<T>>() {
			@Override
			public void onEvent(AtomicReference<T> event, long sequence, boolean endOfBatch) throws Exception {
				process(event.get());
			}
		});
	}

	public UnicastDisruptor(final LMaxDisruptor<T> async) {
		super();
		subscribeConsumer(new EventHandler<AtomicReference<T>>() {
			@Override
			public void onEvent(AtomicReference<T> event, long sequence, boolean endOfBatch) throws Exception {
				process(event.get());
			}
		});
	}

	public ExceptionHandler<T> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<T> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
