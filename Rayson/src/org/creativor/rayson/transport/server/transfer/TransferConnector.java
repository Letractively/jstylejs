/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.server.transfer;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.creativor.rayson.annotation.TransferCode;
import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferService;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;

/**
 *
 * @author Nick Zhang
 */
@TransferConfig
public class TransferConnector {
	private HashMap<Short, TransferInvoker> transferInvokers;
	private CallManager callManager;
	private List<CallWorker> callWorkers;
	private TransferConfig config;
	private static final String PROCESS_METHOD_NAME = "process";

	private static final Class<?>[] PROCESS_METHOD_PARATYPES = new Class<?>[] {
			TransferArgument.class, TransferSocket.class };

	public TransferConnector() {
		transferInvokers = new HashMap<Short, TransferInvoker>();
		callManager = new CallManager();
		callWorkers = new ArrayList<CallWorker>();
		config = this.getClass().getAnnotation(TransferConfig.class);
	}

	public void submitCall(short transfer, TransferArgument argument,
			TransferSocket socket) throws TransferCallException {
		TransferInvoker invoker = transferInvokers.get(transfer);
		if (invoker == null)
			throw new TransferCallException(
					new IllegalStateException(
							"Can not find transfer invoker assiosiated with"
									+ transfer));
		TransferCall call = new TransferCall(invoker, argument, socket);
		try {
			callManager.put(call);
		} catch (InterruptedException e) {
			throw new TransferCallException(e);
		}
	}

	public void start() {
		CallWorker worker;
		for (int i = 0; i < this.config.workerCount(); i++) {
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
					PROCESS_METHOD_PARATYPES);
			Class<? extends TransferArgument> argumentType = (Class<? extends TransferArgument>) getGenericParameterType(serviceClass);
			TransferCode transferAnnotation = null;
			if (argumentType != null)
				transferAnnotation = argumentType
						.getAnnotation(TransferCode.class);
			if (transferAnnotation == null)
				throw new IllegalServiceException(
						"No transfer annotation found in service generic type ");
			short transferCode = transferAnnotation.value();
			TransferInvoker invoker = transferInvokers.get(transferCode);
			if (invoker != null)
				throw new ServiceAlreadyExistedException("Transfer service "
						+ transferCode);
			invoker = new TransferInvoker(transferCode, service, processMethod);
			this.transferInvokers.put(transferCode, invoker);

		} catch (Exception e) {
			throw new IllegalServiceException(e.getMessage());
		}

	}

	private Type getGenericParameterType(
			Class<? extends TransferService> serviceClass) {
		Type[] ts = serviceClass.getGenericInterfaces();
		for (Type t : ts) {
			if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
				for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
					return t1;
				}
			}
		}
		return null;
	}

	public boolean serviceExists(short transferCode) {
		return this.transferInvokers.containsKey(transferCode);
	}

	/**
	 * 
	 * @param transferCode
	 * @param clientVersion
	 * @return
	 * @throws ServiceNotFoundException
	 *             If transfer code associated service is not found.
	 */
	public boolean isSupportedVersion(short transferCode, short clientVersion)
			throws ServiceNotFoundException {
		TransferInvoker invoker = this.transferInvokers.get(transferCode);
		return invoker.isSupportedVersion(clientVersion);
	}
}
