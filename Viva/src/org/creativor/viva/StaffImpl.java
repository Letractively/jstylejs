package org.creativor.viva;

import org.creativor.rayson.api.Portable;
import org.creativor.viva.api.Card;
import org.creativor.viva.api.Staff;

final class StaffImpl implements Staff {

	private int id;

	public StaffImpl(int hashCode) {
		this.id = hashCode;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Card getCard(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putCard(String key, Portable value) {
		// TODO Auto-generated method stub

	}

}