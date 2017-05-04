package ca.rdmss.dflow.impl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.ContextFactory;
import ca.rdmss.dflow.lmax.ContextHandler;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public class UnicastDisruptor<T> extends LMaxDisruptor<T>{

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(ProducerType producerType, final Task<T> task) {
		super(producerType, new ContextFactory<T>());
		subscribeConsumer( new EventHandler<ContextEvent<T>>(){
			@Override
			public void onEvent(ContextEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
				try {
					switch( task.execute(event.getContext()) ){
					case Next: return;
					case Fail: 
					case Stop: 
					case End:  return;
					}
				} catch( Throwable ex){
					switch( getExceptionHandler().handleTaskException(null, ex) ){
					case Next: return;
					case Fail: 
					case Stop: 
					case End:  return;
					}
				}
			}
			
		});
	}

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(ProducerType producerType, ContextHandler<T> handler) {
		super( producerType, new ContextFactory<T>());
		subscribeConsumer(handler);
	}	

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(LMaxDisruptor<AsyncContext<T>> disruptor) {
		super( ProducerType.SINGLE, new ContextFactory<T>());
		subscribeConsumer(new TasksHandler<T>(disruptor));
	}
}
