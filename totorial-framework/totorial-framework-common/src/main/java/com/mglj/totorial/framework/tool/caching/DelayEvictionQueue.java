package com.mglj.totorial.framework.tool.caching;

import com.mglj.totorial.framework.tool.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 延迟删除缓存的队列
 * 
 * @author zsp
 *
 */
class DelayEvictionQueue {

	private final static Logger logger = LoggerFactory.getLogger(DelayEvictionQueue.class);

	private final static String COMMAND_STOP = "$stop";

	private final DelayQueue<CacheEvictionMessage> queue;
	private final ExecutorService workerThreadPool;
	private final int delayStopMillis;
	
	public DelayEvictionQueue(ExecutorService workerThreadPool, int delayStopMillis) {
		this.queue = new DelayQueue<>();
		this.workerThreadPool = workerThreadPool;
		if(delayStopMillis < 1) {
			delayStopMillis = 1;
		}
		this.delayStopMillis = delayStopMillis;
	}
	
	void start() {
		workerThreadPool.execute(() -> {
			for(;;) {
				CacheEvictionMessage message;
				try {
					LogUtils.debug(logger, "等待缓存延迟淘汰消息");
					message = queue.take();
				} catch (InterruptedException e) {
					break;
				}
				final Object key = message.getKey();
				LogUtils.debug(logger, "处理缓存延迟淘汰消息：{}" , key);
				if(COMMAND_STOP.equals(key)) {
					break;
				}
				workerThreadPool.execute(() -> {
					message.getCache().remove(key);
				});
			}
		});
		LogUtils.info(logger, "缓存延迟淘汰任务已启动");
	}
	
	void stop() {
		queue.put(
				new CacheEvictionMessage(null, COMMAND_STOP, delayStopMillis));
	}
	
	public void evict(DelayEvictionCache cache, Object key) {
		if(COMMAND_STOP.equals(key)) {
			throw new IllegalArgumentException(COMMAND_STOP + " is a reserve key");
		}
		queue.put(new CacheEvictionMessage(cache, key, cache.getDelayEvictMillis()));
	}
	
	static class CacheEvictionMessage implements Delayed {

		private final DelayEvictionCache cache;
		private final Object key;
		private final long timestamp;
		
		CacheEvictionMessage(DelayEvictionCache cache, Object key, long timeout) {
			this.cache = cache;
			this.key = key;
			this.timestamp = System.currentTimeMillis() + timeout;
		}

		Cache getCache() {
			return cache;
		}

		Object getKey() {
			return key;
		}
		
		long getTimestamp() {
			return timestamp;
		}

		@Override
		public int compareTo(Delayed other) {
			 return (int)(timestamp - ((CacheEvictionMessage)other).getTimestamp());
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long result = timestamp - System.currentTimeMillis();
	        return result;
		}
		
	}
	
}
