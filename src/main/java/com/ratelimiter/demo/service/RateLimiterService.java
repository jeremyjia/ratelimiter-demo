package com.ratelimiter.demo.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class RateLimiterService implements IRateLimiterService<String> {

	//每个用户对应一个Queue，Queue中存放请求的时间戳，用于判断一定时间内的流量
	private static ConcurrentHashMap<String, PriorityBlockingQueue<Long>> map = new ConcurrentHashMap<>();

	private final ReentrantLock lock = new ReentrantLock();

	private static final int HOUR = 3;
	private static final int MINUTE = 2;
	private static final int SECOND = 1;
	private long timeMillis;

	//application.properties设置，默认是TPS=100/s
	@Value("${limit.number}")
	private int number;

	@Value("${limit.unit}")
	private int unit;

	@PostConstruct
	public void init() {
		timeMillis = getTimeMillis();
		System.out.println("limit number:" + number + " limit unit:" + unit);
	}

	@Override
	public boolean isLimited(String userLicense) {
		lock.lock();
		try {
			while (map.get(userLicense) == null) {
				map.put(userLicense, new PriorityBlockingQueue<Long>());
			}
		} finally {
			lock.unlock();
		}
		long currentTimeMillis = System.currentTimeMillis();
		PriorityBlockingQueue<Long> priorityQueue = map.get(userLicense);
		if (priorityQueue.size() >= number) {
			if (currentTimeMillis - priorityQueue.peek() <= timeMillis) {
				priorityQueue.offer(currentTimeMillis);
				if (priorityQueue.size() > number) {
					priorityQueue.poll();
				}
				return true;
			} else {
				priorityQueue.offer(currentTimeMillis);
				if (priorityQueue.size() > number) {
					priorityQueue.poll();
				}
				return false;
			}
		} else {
			priorityQueue.offer(currentTimeMillis);
			return false;
		}
	}

	private long getTimeMillis() {
		if (unit == SECOND) {
			return 1000;
		} else if (unit == MINUTE) {
			return 1000 * 60;
		} else if (unit == HOUR) {
			return 1000 * 60 * 60;
		}
		return 1000;
	}

}
