package org.creativor.viva;

import java.net.InetSocketAddress;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.viva.api.Card;
import org.creativor.viva.api.CardAsyncProxy;
import org.creativor.viva.api.CardProxy;
import org.creativor.viva.api.PortableStaff;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaAsyncProxy;
import org.creativor.viva.api.VivaProxy;

public final class StaffLocal implements Staff {

	public static StaffLocal fromPortable(PortableStaff portable) {
		StaffLocal staffLocal = new StaffLocal(portable.getId(),
				new InetSocketAddress(portable.getIp(), portable.getPort()));
		return staffLocal;
	}

	private CardAsyncProxy cardAsyncProxy;
	private CardProxy cardProxy;
	private int id;
	private PortableStaff portable;
	private VivaAsyncProxy vivaAsyncProxy;

	private VivaProxy vivaProxy;

	public StaffLocal(InetSocketAddress serverAddress) throws RpcException {
		try {
			this.vivaProxy = Rayson.createProxy(VivaServiceImpl.SERVICE_NAME,
					VivaProxy.class, serverAddress);
		} catch (IllegalServiceException e) {
			throw new RuntimeException(e);
		}
		this.id = this.vivaProxy.getId();
		init(serverAddress);
	}

	private StaffLocal(int hashCode, InetSocketAddress serverAddress) {
		this.id = hashCode;
		init(serverAddress);
	}

	public StaffLocal(int hashCode, String hostName, int port) {
		this(hashCode, new InetSocketAddress(hostName, port));
	}

	@Override
	public Card getCard(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public CardAsyncProxy getCardAsyncProxy() {
		return cardAsyncProxy;
	}

	public CardProxy getCardProxy() {
		return cardProxy;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getIp() {
		return portable.getIp();
	}

	@Override
	public int getPort() {
		return portable.getPort();
	}

	public PortableStaff getPortable() {
		return portable;
	}

	public VivaAsyncProxy getVivaAsyncProxy() {
		return vivaAsyncProxy;
	}

	public VivaProxy getVivaProxy() {
		return this.vivaProxy;
	}

	private void init(InetSocketAddress serverAddress) {
		this.portable = new PortableStaff(id, serverAddress.getAddress()
				.getHostAddress(), serverAddress.getPort());
		try {
			if (this.vivaProxy == null)
				this.vivaProxy = Rayson.createProxy(
						VivaServiceImpl.SERVICE_NAME, VivaProxy.class,
						serverAddress);

			this.cardProxy = Rayson.createProxy(CardServiceImpl.SERVICE_NAME,
					CardProxy.class, serverAddress);
			this.cardAsyncProxy = Rayson.createAsyncProxy(
					CardServiceImpl.SERVICE_NAME, CardAsyncProxy.class,
					serverAddress);
			this.vivaAsyncProxy = Rayson.createAsyncProxy(
					VivaServiceImpl.SERVICE_NAME, VivaAsyncProxy.class,
					serverAddress);
		} catch (IllegalServiceException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putCard(String key, Portable value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return portable.toString();
	}
}