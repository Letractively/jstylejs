package org.creativor.viva;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.server.RpcServer;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;
import org.creativor.rayson.util.Log;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaService;
import org.creativor.viva.conf.ConfTool;
import org.creativor.viva.conf.LoadConfigException;
import org.creativor.viva.conf.Servants;

public final class Viva {
	private static Logger LOGGER = Log.getLogger();

	/**
	 * @param args
	 * @throws LoadConfigException
	 * @throws IOException
	 * @throws IllegalServiceException
	 * @throws ServiceAlreadyExistedException
	 */
	public static void main(String[] args) throws LoadConfigException,
			IOException, ServiceAlreadyExistedException,
			IllegalServiceException {
		if (args == null || args.length != 1)
			throw new IllegalArgumentException(
					"Should tell me the port number to start this viva system");
		short portNumber = Short.parseShort(args[0]);
		Viva viva = new Viva(portNumber);
		viva.start();
	}

	private InetSocketAddress address;
	boolean imServant;
	private List<Servant> servants;

	private RpcServer server;

	private VivaService service;

	Viva(short portNumber) throws LoadConfigException, IllegalArgumentException {
		imServant = false;
		this.address = new InetSocketAddress(portNumber);
		int hashCode = HashCoder.getHashCode(this.address.toString());
		Staff me = new StaffImpl(hashCode);
		this.service = new VivaServiceImpl(me);
		Servants confServants = ConfTool.getSingleton().getConfiguration(
				Servants.class);
		this.servants = new ArrayList<Servant>();
		Servant servant;
		for (String servantString : confServants.getNodes()) {
			try {
				servant = Servant.fromAddress(servantString);
			} catch (Exception e) {
				throw new LoadConfigException(e);
			}
			this.servants.add(servant);
		}
	}

	public boolean start() throws IOException, ServiceAlreadyExistedException,
			IllegalServiceException {
		this.server = new RpcServer((short) this.address.getPort());
		this.server.registerService(VivaServiceImpl.SERVICE_NAME,
				VivaServiceImpl.SERVICE_DESCRIPTION, service);
		this.server.registerService(CardServiceImpl.SERVICE_NAME,
				CardServiceImpl.SERVICE_DESCRIPTION, new CardServiceImpl());
		this.server.start();
		for (Servant servant : this.servants) {
			if (this.address.equals(servant.getAddress())) {
				imServant = true;
				break;
			}
		}
		return true;
	}

}