package ca.rdmss.dflow;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.TimeoutException;

import ca.rdmss.dflow.impl.ContextEvent;
import ca.rdmss.dflow.impl.DafaultExceptionHandler;
import ca.rdmss.dflow.impl.UnicastDisruptor;

public class DisruptorFlow<T extends ContextEvent<T>> {

	private UnicastDisruptor<ContextEvent<T>> async;
	private UnicastDisruptor<ContextEvent<T>> sync;
	
	private ExceptionHandler<ContextEvent<T>> exceptionHandler = new DafaultExceptionHandler<ContextEvent<T>>();

	public void start(){
		if( async != null || sync != null ){
			throw new RuntimeException("Disruptor already started!?");
		}

		sync = new UnicastDisruptor<ContextEvent<T>>() {
			@Override
			public void process(ContextEvent<T> event) {
				pipeline(event.getContext(), event.getTasks(), async.getExceptionHandler());
			}
			
			private boolean pipeline(T context, Task<T>[] tasks, ExceptionHandler<ContextEvent<T>> parentHandler) {
				for(Task<T> task: tasks ){
					
					if( task.isSet() ){
						
						ExceptionHandler<ContextEvent<T>> taskHandler = task.getExceptionHandler();
						
						if( !pipeline(context, task.getSet(), taskHandler != null? taskHandler: parentHandler) ){
							return false; 
						}
					} else if( task.isAsync() ){
						async.publish(new ContextEvent<T>(context, task));
					} else {
						try {
							switch( task.execute(context) ){
							case Next: continue;
							case Fail: return false;
							case Stop: return false;
							case End:  return false;
							}
						} catch( Throwable ex){
							ExceptionHandler<ContextEvent<T>> taskHandler = task.getExceptionHandler();
							switch( taskHandler!=null? taskHandler.handleTaskException(context, ex): parentHandler.handleTaskException(context, ex) ){
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
		};
		
		async = new UnicastDisruptor<ContextEvent<T>>(async) {
			@Override
			public void process(ContextEvent<T> event) {
				for(Task<T> task: event.getTasks()){
					try {
						task.execute(event.getContext());
					} catch( Throwable ex){
						ExceptionHandler<ContextEvent<T>> taskHandler = task.getExceptionHandler();
						if( taskHandler!= null ){
							taskHandler.handleTaskException(event.getContext(), ex);
						} else {
							getExceptionHandler().handleTaskException(event.getContext(), ex);
						}
					}
				}
			}
		};

		sync.setExceptionHandler(exceptionHandler);
		async.setExceptionHandler(exceptionHandler);

		async.start();
		sync.start();
	}
	
	public void stop(){
		try {
			sync.shutdown();
			sync = null;
			async.shutdown();
			async = null;
		} catch(NullPointerException e){
			throw new RuntimeException("Disruptor not started!?");
		}
	}
	
	public void stop(long timeout, TimeUnit timeUnit) throws TimeoutException{
		sync.shutdown(timeout, timeUnit);
		async.shutdown(timeout, timeUnit);
	}
	
	@SafeVarargs
	final public void process(T context, Task<T>... tasks){
		try {
			sync.publish(new ContextEvent<T>(context, tasks));
		} catch(NullPointerException e){
			throw new RuntimeException("Disruptor not started!?");
		}
	}

	@SafeVarargs
	final public Task<T>[] createFlow(Task<T>... tasks){
		return tasks;
	}

	public void setExceptionHandler(ExceptionHandler<ContextEvent<T>> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
}
