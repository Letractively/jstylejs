package org.creativor.viva;

import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.creativor.rayson.api.Session;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.viva.api.PortableStaff;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaService;

final class VivaServiceImpl implements VivaService {
	static String SERVICE_DESCRIPTION = "Viva rpc service";
	static String SERVICE_NAME = "viva";
	private Staff me;
	private TreeMap<Integer, StaffLocal> staffs;

	public VivaServiceImpl(Staff me) {
		this.me = me;
		this.staffs = new TreeMap<Integer, StaffLocal>();
	}

	public void addStaff(StaffLocal staff) {
		this.staffs.put(staff.getId(), staff);
	}

	Iterator<StaffLocal> staffItor() {
		return Collections.unmodifiableCollection(staffs.values()).iterator();
	}

	public boolean exists(int staffId) {
		return this.staffs.containsKey(staffId);
	}

	@Override
	public int getId(Session session) {
		return me.getId();
	}

	@Override
	public boolean join(Session session, int hashCode) {
		// Add to list first.
		this.staffs.put(hashCode,
				new StaffLocal(hashCode, session.getPeerAddress()));
		return join1(hashCode);
	}

	private boolean join1(int hashCode) {
		Entry<Integer, StaffLocal> left = staffs.lowerEntry(hashCode);
		Entry<Integer, StaffLocal> right = staffs.ceilingEntry(hashCode);

		boolean leftResult = false;
		boolean rightResult = false;
		if (left != null && left.getValue().equals(me)) {
			try {
				leftResult = left
						.getValue()
						.getVivaProxy()
						.notifyJoin(hashCode, this.me.getIp(),
								this.me.getPort(), true);
			} catch (RpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (right != null && right.getValue().equals(me)) {
			try {
				rightResult = right
						.getValue()
						.getVivaProxy()
						.notifyJoin(hashCode, this.me.getIp(),
								this.me.getPort(), false);
			} catch (RpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean result = leftResult || rightResult;
		return result;

	}

	@Override
	public boolean notifyJoin(Session session, int joiner, String ip,
			short port, boolean leftDirection) {
		// If joiner has already joined in, end the notify progress.
		if (this.staffs.containsKey(joiner))
			return true;
		// add to list first.
		this.staffs.put(joiner, new StaffLocal(joiner, new InetSocketAddress(
				SERVICE_DESCRIPTION, port)));
		Entry<Integer, StaffLocal> next;
		if (leftDirection)
			next = staffs.lowerEntry(this.me.getId());
		else
			next = staffs.ceilingEntry(this.me.getId());
		if (next == null)
			return true;
		try {
			next.getValue().getVivaAsyncProxy()
					.notifyJoin(joiner, ip, port, leftDirection);
		} catch (NetWorkException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Staff getMe() {
		return me;
	}

	/**
	 * join myself into the viva system. That will get all staffs from other
	 * Servant. <br>
	 * Servant will use this way to join into system.
	 * 
	 * @return
	 */
	public boolean joinMe() {
		return join1(me.getId());
	}

	@Override
	public PortableStaff[] list(Session session) {
		List<PortableStaff> list = new ArrayList<PortableStaff>();
		for (StaffLocal staff : staffs.values()) {
			list.add(staff.getPortable());
		}
		return list.toArray(new PortableStaff[0]);
	}
}