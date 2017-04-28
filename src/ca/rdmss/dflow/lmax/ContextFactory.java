package ca.rdmss.dflow.lmax;

import com.lmax.disruptor.EventFactory;

public class ContextFactory<T> implements EventFactory<ContextEvent<T>> {

	@Override
	public ContextEvent<T> newInstance() {
		return new ContextEvent<T>();
	}
}
