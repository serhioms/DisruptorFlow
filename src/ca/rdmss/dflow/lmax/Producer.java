package ca.rdmss.dflow.lmax;

import com.lmax.disruptor.RingBuffer;

import ca.rdmss.dflow.Task;

public class Producer<T> implements ContextProducer<T> {

	final private RingBuffer<ContextEvent<T>> ringBuffer;
	
	public Producer(RingBuffer<ContextEvent<T>> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	@Override
	public void onData(T context) {
		publishEvent(context, null);
	}

	@Override
	public void onData(T context, @SuppressWarnings("unchecked") Task<T>... tasks) {
		publishEvent(context, tasks);
	}
	
	synchronized private void publishEvent(T context, Task<T>[] tasks) {
		/*
		 * Grab the next sequence
		 */
		long sequence = ringBuffer.next();
		try {
			/*
			 * Get the entry in the disruptor for the sequence
			 */
			ContextEvent<T> event = ringBuffer.get(sequence);
			/*
			 * Fill with data
			 */
			event.setContext(context);
			event.setTasks(tasks);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
