package ca.rdmss.dflow.lmax;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;

import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.impl.DafaultExceptionHandler;

public class LMaxDisruptor<T> {

	static final public int BUFFER_SIZE = 300_000; // 100K buffer by default

	// Sleep BatchEventProcessor rather the spin if not busy
	protected WaitStrategy waitStrategy = new com.lmax.disruptor.SleepingWaitStrategy(); 
	
	// Spin BatchEventProcessor if not busy with yielding among others
	//protected WaitStrategy waitStrategy = new com.lmax.disruptor.YieldingWaitStrategy();  
	
	private ExceptionHandler<T> exceptionHandler = new DafaultExceptionHandler<T>();

	private Disruptor<ContextEvent<T>> disruptor;
	private ContextProducer<T> producer;
	
	ThreadFactory threadFactory = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r);
		}
	};
	
	public LMaxDisruptor(ProducerType producerType, ContextFactory<T> eventFactory) {
		this(BUFFER_SIZE, producerType, eventFactory);
	}  

	//@SuppressWarnings("deprecation")
	public LMaxDisruptor(int bufferSize, ProducerType producerType, ContextFactory<T> eventFactory) {
		/*
		 * Create LMax Disrupter
		 */
		disruptor = new Disruptor<ContextEvent<T>>(
				eventFactory,
				Util.ceilingNextPowerOfTwo(bufferSize), // size of the ring buffer must be power of 2
				threadFactory, // Each disruptor runs in 1 thread
				producerType,
				waitStrategy
				);
		/*
		 * Get ring buffer from disruptor to be used for publishing
		 */
		RingBuffer<ContextEvent<T>> ringBuffer = disruptor.getRingBuffer();
		
		/*
		 * Initialize producer
		 */
		producer = new Producer<T>(ringBuffer);

	}  

	/*
	 * Connect consumers
	 */
	@SuppressWarnings("unchecked")
	public void subscribeConsumer(EventHandler<ContextEvent<T>>... consumer){
		disruptor.handleEventsWith(consumer); // TODO: There are many other disruptor handlers... 
	}
	
	/*
	 * Start disrupter
	 */
	public void startDisruptor(){
		disruptor.setDefaultExceptionHandler(new com.lmax.disruptor.ExceptionHandler<ContextEvent<T>>(){
			@Override
			public void handleEventException(Throwable ex, long sequence, ContextEvent<T> event) {
				exceptionHandler.handleTaskException(event.getContext(), ex);
			}

			@Override
			public void handleOnStartException(Throwable ex) {
				exceptionHandler.handleTaskException(null, ex);
			}

			@Override
			public void handleOnShutdownException(Throwable ex) {
				exceptionHandler.handleTaskException(null, ex);
			}
		});
		disruptor.start();
	}

	public void shutdown() {
		disruptor.shutdown();
	}

	public void shutdown(long timeout, TimeUnit timeUnit) throws TimeoutException {
		disruptor.shutdown(timeout, timeUnit);
	}

	public void onData(T context) {
		producer.onData(context);
	}
	
	@SuppressWarnings("unchecked")
	public void onData(T context, Task<T>... tasks) {
		producer.onData(context, tasks);
	}
	

	public ExceptionHandler<T> getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler<T> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
