package org.creativor.viva;

import java.net.InetSocketAddress;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.viva.api.Card;
import org.creativor.viva.api.CardProxy;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaProxy;

final class StaffLocal implements Staff {

	private CardProxy cardProxy;
	private int id;
	private VivaProxy vivaProxy;

	public StaffLocal(int hashCode, InetSocketAddress serverAddress) {
		this.id = hashCode;
		try {
			this.vivaProxy = Rayson.createProxy(VivaServiceImpl.SERVICE_NAME,
					VivaProxy.class, serverAddress);

			this.cardProxy = Rayson.createProxy(CardServiceImpl.SERVICE_NAME,
					CardProxy.class, serverAddress);
		} catch (IllegalServiceException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public StaffLocal(InetSocketAddress serverAddress)
			throws IllegalServiceException, RpcException {
		this.vivaProxy = Rayson.createProxy(VivaServiceImpl.SERVICE_NAME,
				VivaProxy.class, serverAddress);
		this.id = this.vivaProxy.getId();
		this.cardProxy = Rayson.createProxy(CardServiceImpl.SERVICE_NAME,
				CardProxy.class, serverAddress);
	}

	@Override
	public Card getCard(String key) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public VivaProxy getVivaProxy() {
		return this.vivaProxy;
	}

	@Override
	public void putCard(String key, Portable value) {
		// TODO Auto-generated method stub

	}
}