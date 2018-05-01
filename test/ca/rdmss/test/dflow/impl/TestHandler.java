package ca.rdmss.test.dflow.impl;

import java.util.concurrent.atomic.AtomicReference;

import com.lmax.disruptor.EventHandler;

public class TestHandler implements EventHandler<AtomicReference<TestContext>> {

	@Override
	public void onEvent(AtomicReference<TestContext> event, long sequence, boolean endOfBatch) throws Exception {
		if( event.get() == null ) {
			System.currentTimeMillis();
		} else if( event.get().getContext() == null ) {
			System.currentTimeMillis();
		} else if( event.get().getContext().counter == null ) {
			System.currentTimeMillis();
		}

		event.get().getContext().counter.incrementAndGet();
	}
}
