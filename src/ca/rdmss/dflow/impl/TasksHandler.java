package ca.rdmss.dflow.impl;

import com.lmax.disruptor.EventHandler;

import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public class TasksHandler<T> implements EventHandler<ContextEvent<T>> {

	final private LMaxDisruptor<AsyncContext<T>> disruptor;
	
	DafaultExceptionHandler<T> tempDefExceptionHandler = new DafaultExceptionHandler<T>();// TODO: must be disruptor handler
	
	public TasksHandler(LMaxDisruptor<AsyncContext<T>> disruptor) {
		this.disruptor = disruptor;
	}

	@Override
	public void onEvent(ContextEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
		pipeline(event.getContext(), event.getTasks(), tempDefExceptionHandler); // TODO: must be disruptor handler
	}

	private boolean pipeline(T context, Task<T>[] tasks, ExceptionHandler<T> defExceptionHandler) {
		for(Task<T> task: tasks ){
			
			if( disruptor != null ){
				task.setDisruptor(disruptor);
			}

			ExceptionHandler<T> exceptionHandler = task.getExceptionHandler() != null? task.getExceptionHandler(): defExceptionHandler;
			
			if( task.isSet() ){
				if( !pipeline(context, task.getSet(), exceptionHandler) ) // TODO: process set vie recursion
					return false; 
			} else if( task.isAsync() ){
				task.publishAsync(context);
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
}
