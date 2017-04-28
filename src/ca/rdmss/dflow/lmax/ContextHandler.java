package ca.rdmss.dflow.lmax;

import com.lmax.disruptor.EventHandler;

public interface ContextHandler<T> extends EventHandler<ContextEvent<T>> {

}
