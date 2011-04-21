/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import java.io.EOFException;
import org.creativor.rayson.api.Session;

/**
 * 
 * @author Nick Zhang
 */
public class TestServiceImpl implements TestService {
	@Override
	public String echo(Session session, String message) throws EOFException {
		// throw new NullPointerException("eof");
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

	@Override
	public boolean isSupportedVersion(Session session) {
		return true;
	}

}