package com.mglj.totorial.framework.tools.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class NamedThreadFactory implements ThreadFactory {

	private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

	private final AtomicInteger threadNum = new AtomicInteger(1);

	private final String prefix;

	private final boolean daemo;

	private final ThreadGroup group;

	public NamedThreadFactory() {
		this("pool-" + POOL_SEQ.getAndIncrement(), false);
	}

	public NamedThreadFactory(String prefix) {
		this(prefix, false);
	}

	public NamedThreadFactory(String prefix, boolean daemo) {
		this.prefix = prefix + "-thread-";
		this.daemo = daemo;
		SecurityManager s = System.getSecurityManager();
		this.group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		String name = prefix + threadNum.getAndIncrement();
		Thread ret = new Thread(group, runnable, name, 0);
		ret.setDaemon(daemo);
		return ret;
	}

	public ThreadGroup getThreadGroup() {
		return group;
	}
}