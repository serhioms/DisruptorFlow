package ca.rdmss.dflow.impl;

import com.lmax.disruptor.EventHandler;

import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public class TasksHandler<T> implements EventHandler<ContextEvent<T>> {

	final private LMaxDisruptor<AsyncContext<T>> disruptor;
	
	public TasksHandler(LMaxDisruptor<AsyncContext<T>> disruptor) {
		this.disruptor = disruptor;
	}

	@Override
	public void onEvent(ContextEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
		pipeline(event.getContext(), event.getTasks());
	}

	private void pipeline(T context, Task<T>[] tasks) {
		for(Task<T> task: tasks ){
			
			if( disruptor != null ){
				task.setDisruptor(disruptor);
			}
			
			if( task.isSet() ){
				pipeline(context, task.getSet()); // TODO: process set vie recursion
			} else if( task.isAsync() ){
				task.publishAsync(context);
			} else {
				try {
					if( !task.execute(context) ){
						break; // task require stop feature processing
					}
				} catch( Throwable t){
					t.printStackTrace(); // TODO: exceeption handler must be here
				}
			}
		}
	}
}
