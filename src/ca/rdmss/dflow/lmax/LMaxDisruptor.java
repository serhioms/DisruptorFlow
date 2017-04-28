package ca.rdmss.dflow.lmax;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;

import ca.rdmss.dflow.Task;

public class LMaxDisruptor<T> {

	static final public int BUFFER_SIZE = 100_000; // !00K buffer
	
	final private Disruptor<ContextEvent<T>> disruptor;
	
	final private ContextProducer<T> producer;
	
	protected WaitStrategy waitStrategy = new YieldingWaitStrategy();
	
	ThreadFactory threadFactory = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r);
		}
	};

	public LMaxDisruptor(ProducerType producerType, ContextFactory<T> eventFactory) {
		this(BUFFER_SIZE, producerType, eventFactory);
	}  

	public LMaxDisruptor(int bufferSize, ProducerType producerType, ContextFactory<T> eventFactory) {
		/*
		 * Create LMax Disrupter
		 */
		disruptor = new Disruptor<ContextEvent<T>>(
				eventFactory,
				Util.ceilingNextPowerOfTwo(bufferSize), // size of the ring buffer must be power of 2
				threadFactory,
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
	
	public void onData(T context, Task<T>[] tasks) {
		producer.onData(context, tasks);
	}
	
}