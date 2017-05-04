package ca.rdmss.dflow.impl;

import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.ContextFactory;
import ca.rdmss.dflow.lmax.ContextHandler;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public class UnicastDisruptor<T> extends LMaxDisruptor<T>{

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(ProducerType producerType, ContextHandler<T> handler) {
		super( producerType, new ContextFactory<T>());
		subscribeConsumer(handler);
	}	

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(ProducerType multi) {
		super(multi, new ContextFactory<T>());
		subscribeConsumer(new ContextHandler<T>(){
			@Override
			public void onEvent(ContextEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
				for(Task<T> task: event.getTasks()){
					try {
						task.execute(event.getContext());
					} catch( Throwable ex){
						getExceptionHandler().handleTaskException(event.getContext(), ex);
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(ProducerType producerType, LMaxDisruptor<T> disruptor) {
		super( producerType, new ContextFactory<T>());
		subscribeConsumer(new TasksHandler<T>(disruptor));
	}
}
