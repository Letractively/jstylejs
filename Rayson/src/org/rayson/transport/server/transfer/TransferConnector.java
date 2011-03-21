package org.rayson.transport.server.transfer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rayson.annotation.Transfer;
import org.rayson.api.TransferService;
import org.rayson.api.TransferSocket;
import org.rayson.exception.IllegalServiceException;
import org.rayson.transport.api.ServiceAlreadyExistedException;

public class TransferConnector {
	private HashMap<Short, TransferInvoker> transferInvokers;
	private CallManager callManager;
	private static final int INIT_WORKER_COUNT = 4;
	private List<CallWorker> callWorkers;
	private static final String PROCESS_METHOD_NAME = "process";

	private static final Class<?> PROCESS_METHOD_PARATYPE = TransferSocket.class;

	public TransferConnector() {
		transferInvokers = new HashMap<Short, TransferInvoker>();
		callManager = new CallManager();
		callWorkers = new ArrayList<CallWorker>();
	}

	public void submitCall(short transfer, TransferSocket socket)
			throws TransferCallException {
		TransferInvoker invoker = transferInvokers.get(transfer);
		if (invoker == null)
			throw new TransferCallException(
					new IllegalStateException(
							"Can not find transfer invoker assiosiated with"
									+ transfer));
		TransferCall call = new TransferCall(invoker, socket);
		try {
			callManager.put(call);
		} catch (InterruptedException e) {
			throw new TransferCallException(e);
		}
	}

	public void start() {
		CallWorker worker;
		for (int i = 0; i < INIT_WORKER_COUNT; i++) {
			worker = new CallWorker(callManager);
			worker.start();
			this.callWorkers.add(worker);
		}

	}

	public void registerService(TransferService service)
			throws ServiceAlreadyExistedException, IllegalServiceException {
		if (service == null)
			throw new IllegalServiceException("Service is null");
		Class<? extends TransferService> serviceClass = service.getClass();
		try {
			Method processMethod = serviceClass.getMethod(PROCESS_METHOD_NAME,
					PROCESS_METHOD_PARATYPE);
			Transfer transferAnnotation = processMethod
					.getAnnotation(Transfer.class);
			if (transferAnnotation == null)
				throw new IllegalServiceException(
						"No transfer annotation found in service method "
								+ PROCESS_METHOD_NAME + "()");
			short transfer = transferAnnotation.value();
			TransferInvoker invoker = transferInvokers.get(transfer);
			if (invoker != null)
				throw new ServiceAlreadyExistedException("Transfer service "
						+ transfer);
			invoker = new TransferInvoker(transfer, service, processMethod);
			this.transferInvokers.put(transfer, invoker);

		} catch (Exception e) {
			throw new IllegalServiceException(e.getMessage());
		}

	}

	public boolean serviceExists(short transfer) {
		return this.transferInvokers.containsKey(transfer);
	}
}
