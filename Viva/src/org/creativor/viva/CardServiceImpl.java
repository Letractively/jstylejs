package org.creativor.viva;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.Session;
import org.creativor.viva.api.Card;
import org.creativor.viva.api.CardService;

final class CardServiceImpl implements CardService {
	static String SERVICE_DESCRIPTION = "Card rpc service";

	static String SERVICE_NAME = "card";

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
