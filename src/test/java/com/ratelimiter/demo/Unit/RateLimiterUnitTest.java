package com.ratelimiter.demo.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ratelimiter.demo.controller.CommonController;
import com.ratelimiter.demo.exception.RateLimiterException;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RateLimiterUnitTest {

	@Autowired
	private CommonController operator = null;

	@Value("${limit.number}")
	private int limitNumber;

	@Value("${limit.unit}")
	private int limitUnit;

	private int frequency = 10;

	@Before
	public void setup() throws Exception {
		System.out.println("********" + limitNumber);
		if (1000 / limitNumber >= 1) {
			frequency = 1000 / limitNumber;
		}
	}

	// 此Case用多线程模拟超过TPS的并发请求到来时的限流个数
	@Test
	public void TEST_sendRequestsConcurrent() throws Exception {
		List<Thread> threadList = new ArrayList<>();
		long start = System.currentTimeMillis();
		AtomicInteger count = new AtomicInteger();
		for (int i = 0; i < limitNumber + 10; i++) {
			Thread thread = new Thread(() -> {
				try {
					Map<String, Object> respObject = operator.request("jeremy");
					Assert.assertEquals("OK!", respObject.get("Status").toString());
				} catch (RateLimiterException e) {
					System.out.println("TEST_sendRequestsConcurrent::" + e.getMessage());
					count.incrementAndGet();
				} catch (Exception e1) {
				}
			});
			threadList.add(thread);
			thread.start();
		}

		threadList.forEach(e -> {
			try {
				e.join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		});
		long end = System.currentTimeMillis();
		System.out.println("time consume:" + (end - start) + "ms");
		int n = count.get();
		Assert.assertEquals(10, n); // 超过TPS的这10个请求应被限流
	}

	// 此Case测试以固定的临界频率发送请求, 验证不会出现限流
	@Test
	public void TEST_sendRequestWithFixedFre() throws Exception {
		if (limitUnit != 1) {
			System.out.println("Ignore for limitUnit:" + limitUnit);
			return; // 只测试秒级的限流，分钟和小时级别的,在浏览器中手动测试即可
		}
		AtomicInteger count = new AtomicInteger();
		int taskNumber = 300;
		final CountDownLatch latch = new CountDownLatch(taskNumber);
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		for (int i = 1; i <= taskNumber; i++) {
			singleThreadExecutor.execute(new Runnable() {
				public void run() {
					try {
						operator.request("Ming");
						count.incrementAndGet();
						Thread.sleep(frequency);
					} catch (Exception e) {
					} finally {
						latch.countDown();
					}
				}
			});
		}

		try {
			latch.await(); // 等待子任务完成
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		singleThreadExecutor.shutdown();
		Assert.assertEquals(taskNumber, count.get());
	}

}
