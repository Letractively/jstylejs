/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.creativor.rayson.demo;

import java.net.InetSocketAddress;
import org.creativor.rayson.api.TestProxy;
import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.transport.server.ServerConfig;

/**
 * @author Nick Zhang
 */
final class Performancer {

	private class CallThread extends Thread {
		CallThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			long startTime = 0;
			long endTime = 0;
			boolean failed;
			String returnString = null;
			for (int i = 0; i < Config.CALL_COUNT_PER_THREAD; i++) {
				failed = false;
				startTime = System.currentTimeMillis();
				try {
					returnString = testProxy.echo(ECHO_MSG);
				} catch (Exception e) {
					failed = true;
				}
				if (!failed && !ECHO_MSG.equals(returnString))
					failed = true;
				if (failed) {
					counter.failOne();
					continue;
				}
				endTime = System.currentTimeMillis();
				counter.oneCallTakeTime(endTime - startTime);
				counter.succOne();
			}
			counter.quitOneThread();
		}
	}

	private static class Config {

		private static int CALL_COUNT_PER_THREAD = 1000;
		private static int PORT_NUMBER = ServerConfig.DEFAULT_PORT_NUMBER;
		private static int THREAD_COUNT = 100;

		public static String print() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("thread count: ");
			sb.append(Config.THREAD_COUNT);
			sb.append(", ");
			sb.append("call count per thread: ");
			sb.append(Config.CALL_COUNT_PER_THREAD);
			sb.append(", ");
			sb.append("server port number: ");
			sb.append(Config.PORT_NUMBER);
			sb.append("}");
			return sb.toString();
		}
	}

	private static class Counter {

		private int failedCallCount = 0;
		private long quitThreadCount = 0;
		private int succCallCount = 0;
		private long totallCallTime = 0;

		public synchronized void failOne() {
			failedCallCount++;

		}

		public int getCallCount() {
			return failedCallCount + succCallCount;
		}

		/**
		 * @return the failedCallCount
		 */
		public int getFailedCallCount() {
			return failedCallCount;
		}

		/**
		 * @return the succCallCount
		 */
		public int getSuccCallCount() {
			return succCallCount;
		}

		/**
		 * @return
		 */
		private boolean isAllThreadQuit() {
			return quitThreadCount == Config.THREAD_COUNT;
		}

		public synchronized void oneCallTakeTime(long takeTime) {
			totallCallTime += takeTime;
		}

		/**
		 * 
		 */
		public synchronized void quitOneThread() {
			quitThreadCount++;
			if (isAllThreadQuit())
				this.notifyAll();
		}

		public synchronized void succOne() {
			succCallCount++;

		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("Totall call count: ");
			sb.append(this.getCallCount());
			sb.append(", ");
			sb.append("Successful call count: ");
			sb.append(this.succCallCount);
			sb.append(", ");
			sb.append("Failed call count: ");
			sb.append(this.failedCallCount);
			if (succCallCount > 0) {
				sb.append(", ");
				sb.append("Each successful call take time: ");
				sb.append(this.totallCallTime / this.succCallCount);
				sb.append(" milli-seconds");

			}
			sb.append("}");
			return sb.toString();
		}

		public void waitForAllThreadQuit() throws InterruptedException {
			synchronized (this) {
				if (!isAllThreadQuit())
					this.wait();
			}
		}
	}

	private class ReportThread extends Thread {
		ReportThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			try {
				counter.waitForAllThreadQuit();
				System.out.println("All threads is quit.\n Config is: \n "
						+ Config.print() + "\n The counter is :\n "
						+ counter.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static final String ECHO_MSG = "echo_msg";

	/**
	 * @param args
	 * @throws IllegalServiceException
	 */
	public static void main(String[] args) throws IllegalServiceException {

		Performancer performance = new Performancer();
		parseConfig(args);
		performance.start();
	}

	/**
	 * @param args
	 */
	private static void parseConfig(String[] args) {

	}

	private Counter counter;

	private TestProxy testProxy;

	Performancer() {
		counter = new Counter();

	}

	public void start() throws IllegalServiceException {
		testProxy = Rayson.createProxy("demo", TestProxy.class,
				new InetSocketAddress(Config.PORT_NUMBER));
		for (int i = 0; i < Config.THREAD_COUNT; i++) {
			(new CallThread()).start();
		}
		(new ReportThread()).start();
	}
}
