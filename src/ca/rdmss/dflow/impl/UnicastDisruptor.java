package ca.rdmss.dflow.impl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.ProducerType;

import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.ContextFactory;
import ca.rdmss.dflow.lmax.ContextHandler;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public class UnicastDisruptor<T> extends LMaxDisruptor<T>{

	@SuppressWarnings("unchecked")
	public UnicastDisruptor(ContextHandler<T> handler) {
		super( ProducerType.MULTI, new ContextFactory<T>());
		subscribeConsumer(handler);
	}	

	@SuppressWarnings("unchecked")
	public UnicastDisruptor() {
		super(ProducerType.MULTI, new ContextFactory<T>());
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
	public UnicastDisruptor(final LMaxDisruptor<T> disruptor) {
		super(ProducerType.MULTI, new ContextFactory<T>());
		subscribeConsumer(new EventHandler<ContextEvent<T>>() {
				@Override
				public void onEvent(ContextEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
					pipeline(event.getContext(), event.getTasks(), disruptor.getExceptionHandler());
				}
				
			private boolean pipeline(T context, Task<T>[] tasks, ExceptionHandler<T> exceptionHandler) {
				for(Task<T> task: tasks ){
					
					if( task.isSet() ){
						if( !pipeline(context, task.getSet(), task.getExceptionHandler() != null? task.getExceptionHandler(): exceptionHandler) )
							return false; 
					} else if( task.isAsync() ){
						disruptor.onData(context, task);
					} else {
						try {
							switch( task.execute(context) ){
							case Next: continue;
							case Fail: return false;
							case Stop: return false;
							case End:  return false;
							}
						} catch( Throwable ex){
							switch( exceptionHandler.handleTaskException(context, ex) ){
							case Next: continue;
							case Fail: return false;
							case Stop: return false;
							case End:  return false;
							}
						}
					}
				}
				return true;
			}
		});
	}
}
