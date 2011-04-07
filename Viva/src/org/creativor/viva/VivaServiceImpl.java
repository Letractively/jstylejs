package org.creativor.viva;

import java.util.TreeMap;

import org.creativor.rayson.api.Session;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaService;

final class VivaServiceImpl implements VivaService {
	static String SERVICE_DESCRIPTION = "Viva rpc service";
	static String SERVICE_NAME = "viva";
	private Staff me;
	private TreeMap<Integer, StaffLocal> staffs;

	public VivaServiceImpl(Staff me) {
		this.me = me;
	}

	public void addStaff(StaffLocal staff) {
		this.staffs.put(staff.getId(), staff);
	}

	@Override
	public int getId(Session session) {
		return me.getId();
	}

	@Override
	public void join(Session session, int hashCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyJoin(Session session, int hashCode) {
		// TODO Auto-generated method stub

	}
}