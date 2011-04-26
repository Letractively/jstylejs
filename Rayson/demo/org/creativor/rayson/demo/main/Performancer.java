/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.creativor.rayson.demo.main;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.demo.api.DemoProxy;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.transport.server.ServerConfig;

/**
 * @author Nick Zhang
 */
final class Performancer {

	private class RpcInvoker {

		private DemoProxy[] proxys;

		RpcInvoker(Integer[] ports) throws IllegalServiceException {
			proxys = new DemoProxy[ports.length];
			for (int i = 0; i < ports.length; i++) {
				proxys[i] = Rayson.createProxy("demo", DemoProxy.class,
						new InetSocketAddress(ports[i]));
			}
		}

		public void invoke() {
			long startTime = 0;
			long endTime = 0;
			boolean failed;
			failed = false;
			String returnString = null;
			for (DemoProxy proxy : proxys) {
				startTime = System.currentTimeMillis();
				try {
					returnString = proxy.echo(ECHO_MSG);
				} catch (Exception e) {
					System.err.println("Run one rpc call failed: "
							+ e.getMessage());
					failed = true;
				}
				if (!failed && !ECHO_MSG.equals(returnString))
					failed = true;
				if (failed) {
					counter.failOne();
					return;
				}
				endTime = System.currentTimeMillis();
				counter.oneCallTakeTime(endTime - startTime);
				counter.succOne();
			}
		}
	}

	private class CallThread extends Thread {
		CallThread() {
			setDaemon(true);
		}

		@Override
		public void run() {

			for (int i = 0; i < Config.CALL_COUNT_PER_THREAD; i++) {
				rpcInvoker.invoke();
			}
			counter.quitOneThread();
		}
	}

	private static class Config {

		public static final long SAMPLE_INTERVAL = 1000;
		private static int CALL_COUNT_PER_THREAD = 100;
		private static Integer[] PORT_NUMBER_ARRAY = new Integer[] { ServerConfig.DEFAULT_PORT_NUMBER };
		private static int THREAD_COUNT = 10;

		public static String print() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("thread count: ");
			sb.append(Config.THREAD_COUNT);
			sb.append(", ");
			sb.append("call count per thread: ");
			sb.append(Config.CALL_COUNT_PER_THREAD);
			sb.append(", ");
			sb.append("server port numbers: ");
			sb.append(Arrays.toString(Config.PORT_NUMBER_ARRAY));
			sb.append("}");
			return sb.toString();
		}
	}

	private static class Counter {

		private int failedCallCount = 0;
		private long quitThreadCount = 0;
		private int succCallCount = 0;
		private long totallSuccCallTime = 0;

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
			totallSuccCallTime += takeTime;
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
			sb.append(", ");
			sb.append("All succefully calls take time: ");
			sb.append(this.totallSuccCallTime);
			if (succCallCount > 0) {
				sb.append(", ");
				sb.append("Each successful call take time: ");
				sb.append(this.totallSuccCallTime / this.succCallCount);
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

	private class SampleThread extends Thread {
		SampleThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				doSample();
				try {
					Thread.sleep(Config.SAMPLE_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			}
		}

		private void doSample() {
			System.out.print("Performance sample information:");
			System.out.println("Max Memory:" + Runtime.getRuntime().maxMemory()
					+ "\n" + "available Memory:"
					+ Runtime.getRuntime().freeMemory());
			System.out.println("Cup usage:"
					+ ManagementFactory.getOperatingSystemMXBean()
							.getSystemLoadAverage());
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
				System.out.println("All threads is quit.\n The counter is :\n "
						+ counter.toString());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Eixt the test
			System.exit(0);
		}
	}

	private static final String ECHO_MSG = "echo_msg";

	/**
	 * @param args
	 * @throws IllegalServiceException
	 */
	public static void main(String[] args) throws IllegalServiceException {

		Performancer performance = new Performancer();
		printHelp();
		parseConfig(args);
		System.out.println("Ready to run test.\n The cofiguration is:"
				+ Config.print());
		performance.init();
		performance.run();
	}

	/**
	 * @throws IllegalServiceException
	 */
	public void init() throws IllegalServiceException {
		this.rpcInvoker = new RpcInvoker(Config.PORT_NUMBER_ARRAY);
	}

	/**
	 * @param args
	 */
	private static void parseConfig(String[] args) {
		int argumentIndex;
		for (String arg : args) {
			if (arg.startsWith(HELP_ARGUMENT)) {
				// printHelp();
				System.exit(-1);
			}
			argumentIndex = -1;
			if ((argumentIndex = arg.indexOf(THREAD_COUNT_ARGUMENT)) >= 0)
				Config.THREAD_COUNT = Integer.parseInt(arg
						.substring(argumentIndex
								+ THREAD_COUNT_ARGUMENT.length()));
			if ((argumentIndex = arg.indexOf(THREAD_CALL_COUNT_ARGUMENT)) >= 0)
				Config.CALL_COUNT_PER_THREAD = Integer.parseInt(arg
						.substring(argumentIndex
								+ THREAD_CALL_COUNT_ARGUMENT.length()));
			if ((argumentIndex = arg.indexOf(PORT_NUMBER_ARGUMENT)) >= 0)
				Config.PORT_NUMBER_ARRAY = parsePortNumbers(arg
						.substring(argumentIndex
								+ PORT_NUMBER_ARGUMENT.length()));
		}
	}

	/**
	 * @param substring
	 * @return
	 */
	private static Integer[] parsePortNumbers(String substring) {
		StringTokenizer tokenizer = new StringTokenizer(substring, ",");
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (tokenizer.hasMoreElements()) {
			list.add(Integer.parseInt((String) tokenizer.nextElement()));
		}
		return list.toArray(new Integer[] {});
	}

	private static void printHelp() {
		System.out
				.println("*****************************************************");
		System.out
				.println("This programme is used to test the performance of Rayson.");
		System.out.println("Araguemts：\n " + HELP_ARGUMENT + " to print help, "
				+ PORT_NUMBER_ARGUMENT + " to assign to server port, "
				+ THREAD_CALL_COUNT_ARGUMENT
				+ " to assign call count each thread, " + THREAD_COUNT_ARGUMENT
				+ " to assign thread count");
		System.out
				.println("*****************************************************");

	}

	private static String THREAD_COUNT_ARGUMENT = "-t";
	private static String THREAD_CALL_COUNT_ARGUMENT = "-c";
	private static String PORT_NUMBER_ARGUMENT = "-p";
	private static String HELP_ARGUMENT = "-h";

	private Counter counter;

	private RpcInvoker rpcInvoker;

	Performancer() {
		counter = new Counter();

	}

	public void run() throws IllegalServiceException {
		for (int i = 0; i < Config.THREAD_COUNT; i++) {
			(new CallThread()).start();
		}
		(new SampleThread()).start();
		(new ReportThread()).start();
	}
}
