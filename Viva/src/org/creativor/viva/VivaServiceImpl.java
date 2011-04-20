/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.util.Log;
import org.creativor.viva.api.HashCodeCollisionException;
import org.creativor.viva.api.PortableStaff;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaService;

final class VivaServiceImpl implements VivaService {

	private static Logger LOGGER = Log.getLogger();
	static String SERVICE_DESCRIPTION = "Viva rpc service";
	static String SERVICE_NAME = "viva";
	private Staff myself;
	private TreeMap<Integer, StaffLocal> staffs;

	public VivaServiceImpl(StaffLocal myself) {
		if (myself == null)
			throw new NullPointerException("Myself should not be empty");
		this.myself = myself;
		this.staffs = new TreeMap<Integer, StaffLocal>();
		// add myself to circle.
		this.staffs.put(myself.getId(), myself);
	}

	public void addStaff(StaffLocal staff) {
		// do not add myself.
		if (staff.getId() == myself.getId())
			return;
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
		return myself.getId();
	}

	@Override
	public boolean join(Session session, int hashCode, int port)
			throws HashCodeCollisionException {
		if (this.staffs.containsKey(hashCode))
			throw new HashCodeCollisionException(hashCode
					+ " staff is already exists");
		String hostName = session.getPeerAddress().getHostName();
		LOGGER.info(hashCode + " host:" + hostName + ", port: " + port
				+ " is joining into the viva system");
		// 1. do join operation.
		boolean result = join1(hashCode, hostName, port);
		// 2. If failed, remove it.
		if (!result)
			this.staffs.remove(hashCode);
		return result;
	}

	private boolean join1(int hashCode, String ip, int port) {

		Entry<Integer, StaffLocal> left = staffs.higherEntry(myself.getId());
		if (left != null && left.getKey() == hashCode)
			left = staffs.higherEntry(hashCode);

		Entry<Integer, StaffLocal> right = staffs.lowerEntry(myself.getId());
		if (right != null && right.getKey() == hashCode)
			right = staffs.higherEntry(hashCode);

		// IF not myself, add to list first.
		if (hashCode != myself.getId())
			this.staffs.put(hashCode, new StaffLocal(hashCode, ip, port));

		boolean leftResult = true;
		boolean rightResult = true;
		// Notify left staff join in
		if (left != null) {
			try {
				leftResult = left.getValue().getVivaProxy()
						.notifyJoin(myself.getId(), ip, port, true);
			} catch (RpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Notify right staff join in
		if (right != null) {
			try {
				rightResult = right.getValue().getVivaProxy()
						.notifyJoin(myself.getId(), ip, port, false);
			} catch (RpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean result = leftResult || rightResult;
		return result;

	}

	@Override
	public boolean notifyJoin(Session session, int joiner, String ip, int port,
			boolean leftDirection) {
		// If joiner has already joined in, end the notify progress.
		if (this.staffs.containsKey(joiner))
			return true;
		// add to list first.
		this.staffs.put(joiner, new StaffLocal(joiner, ip, port));
		Entry<Integer, StaffLocal> next;
		if (leftDirection)
			next = staffs.lowerEntry(this.myself.getId());
		else
			next = staffs.higherEntry(this.myself.getId());

		if (next == null)
			return true;

		try {
			CallFuture<Boolean> future = next.getValue().getVivaAsyncProxy()
					.notifyJoin(joiner, ip, port, leftDirection);
		} catch (NetWorkException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public Staff getMyself() {
		return myself;
	}

	/**
	 * join myself into the viva system. That will get all staffs from other
	 * Servant. <br>
	 * Servant will use this way to join into system.
	 * 
	 * @return
	 */
	public boolean joinMyself() {
		return join1(myself.getId(), myself.getIp(), myself.getPort());
	}

	@Override
	public PortableStaff[] list(Session session) {
		List<PortableStaff> list = new ArrayList<PortableStaff>();
		for (StaffLocal staff : staffs.values()) {
			list.add(staff.getPortable());
		}
		return list.toArray(new PortableStaff[0]);
	}

	@Override
	public String pictureStaffs(Session session) {
		StringBuffer sb = new StringBuffer();
		sb.append("|->");
		for (Staff staff : staffs.values()) {
			sb.append(pictureOneStaff(staff));
		}
		sb.append("|");
		return sb.toString();
	}

	private String pictureOneStaff(Staff staff) {
		return ((staff.getId() == myself.getId()) ? (staff.getId() + "*")
				: staff.getId()) + "->";
	}

	@Override
	public boolean isSupportedVersion(Session session) {
		return true;
	}
}