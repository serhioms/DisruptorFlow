package ca.rdmss.dflow.impl;

import com.lmax.disruptor.EventHandler;

import ca.rdmss.dflow.ExceptionHandler;
import ca.rdmss.dflow.Task;
import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.LMaxDisruptor;

public class TasksHandler<T> implements EventHandler<ContextEvent<T>> {

	final private LMaxDisruptor<T> disruptor;
	
	public TasksHandler(LMaxDisruptor<T> disruptor) {
		this.disruptor = disruptor;
	}

	@Override
	public void onEvent(ContextEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
		pipeline(event.getContext(), event.getTasks(), disruptor.getExceptionHandler());
	}

	@SuppressWarnings("unchecked")
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
}
