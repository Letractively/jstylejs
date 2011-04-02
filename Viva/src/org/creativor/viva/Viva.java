package org.creativor.viva;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.creativor.rayson.server.RpcServer;
import org.creativor.rayson.util.Log;
import org.creativor.viva.api.Staff;
import org.creativor.viva.api.VivaService;
import org.creativor.viva.conf.ConfTool;
import org.creativor.viva.conf.LoadConfigException;
import org.creativor.viva.conf.Servants;

public final class Viva {

	private static Logger LOGGER = Log.getLogger();
	private VivaService service;
	private Staff me;
	private List<Servant> servants;
	private InetSocketAddress address;

	Viva(short portNumber) throws LoadConfigException, IllegalArgumentException {
		this.address = new InetSocketAddress(portNumber);
		this.service = new VivaServiceImpl();
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

	public boolean start() throws IOException {
		this.server = new RpcServer((short) this.address.getPort());
		this.server.start();
		boolean imServant = false;
		for (Servant servant : this.servants) {
			if (this.address.equals(servant.getAddress())) {
				imServant = true;
				break;
			}
		}

		int hashCode = HashCoder.getHashCode(this.address.toString());
		this.me = new StaffImpl(hashCode);
		return true;
	}

	private RpcServer server;

	/**
	 * @param args
	 * @throws LoadConfigException
	 * @throws IOException
	 */
	public static void main(String[] args) throws LoadConfigException,
			IOException {
		if (args == null || args.length != 1)
			throw new IllegalArgumentException(
					"Should tell me the port number to start this viva system");
		short portNumber = Short.parseShort(args[0]);
		Viva viva = new Viva(portNumber);
		viva.start();
	}

}