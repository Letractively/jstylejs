package org.rayson.server;

import java.io.EOFException;

import org.rayson.api.Session;

public class TestServiceImpl implements TestService {
	@Override
	public String echo(Session session, String message) throws EOFException {
		return message;
	}

	@Override
	public int getInt(Session session) {
		// TODO Auto-generated method stub
		return 345;
	}

	@Override
	public int[] getIntArray(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void voidMethod(Session session) {
		// TODO Auto-generated method stub

	}

}