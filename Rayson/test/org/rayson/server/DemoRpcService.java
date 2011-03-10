package org.rayson.server;

import java.io.EOFException;

import org.rayson.api.Session;
import org.rayson.exception.RemoteException;

public class DemoRpcService implements TestRemoteService {
	@Override
	public String echo(Session session, String message) throws EOFException,
			RemoteException {
		return "hellddo wodasfrldd";
	}

	@Override
	public int getInt(Session session) throws RemoteException {
		// TODO Auto-generated method stub
		return 345;
	}

	@Override
	public int[] getIntArray(Session session) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger(Session session) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void voidMethod(Session session) throws RemoteException {
		// TODO Auto-generated method stub

	}
}
