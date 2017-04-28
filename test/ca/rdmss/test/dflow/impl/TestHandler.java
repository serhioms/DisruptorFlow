package ca.rdmss.test.dflow.impl;

import ca.rdmss.dflow.lmax.ContextEvent;
import ca.rdmss.dflow.lmax.ContextHandler;

public class TestHandler implements ContextHandler<TestContext>{

	@Override
	public void onEvent(ContextEvent<TestContext> event, long sequence, boolean endOfBatch) throws Exception {
		event.getContext().counter.incrementAndGet();
	}
}
