package org.creativor.viva;

import java.util.TreeMap;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.Session;
import org.creativor.viva.api.Card;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaService;

final class VivaServiceImpl implements VivaService {

	private TreeMap<Integer, Staff> staffs;
	private Staff me;

	public VivaServiceImpl(Staff me) {
		this.me = me;
	}

	@Override
	public void join(Session session, int hashCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public Card getCard(Session session, int hashCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putCard(Session session, int hashCode, Portable value) {
		// TODO Auto-generated method stub

	}
}
