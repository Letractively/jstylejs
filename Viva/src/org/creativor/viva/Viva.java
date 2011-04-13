package org.creativor.viva;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.server.RpcServer;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;
import org.creativor.rayson.util.Log;
import org.creativor.viva.api.PortableStaff;
import org.creativor.viva.api.Staff;
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
		System.out.println("Start Viva staff with port number: \"" + args[0]
				+ "\"");
		short portNumber = Short.parseShort(args[0]);
		Viva viva = new Viva(portNumber);
		viva.start();
	}

	private InetSocketAddress address;
	boolean imServant;
	private List<Servant> servants;

	private RpcServer server;

	private VivaServiceImpl service;

	Viva(short portNumber) throws LoadConfigException,
			IllegalArgumentException, IllegalServiceException {
		imServant = false;
		this.address = new InetSocketAddress(portNumber);

		// setup log file
		String logFileName = "viva_" + this.address.getHostName() + "_"
				+ this.address.getPort();
		try {
			Environment.getEnvironment().setupLogFile(logFileName);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}

		int hashCode = HashCoder.getHashCode(this.address.toString());
		Staff me = new StaffLocal(hashCode, this.address.getHostName(),
				(short) this.address.getPort());
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

		// 1 start rpc server and register services.
		this.server = new RpcServer((short) this.address.getPort());
		this.server.registerService(VivaServiceImpl.SERVICE_NAME,
				VivaServiceImpl.SERVICE_DESCRIPTION, service);
		this.server.registerService(CardServiceImpl.SERVICE_NAME,
				CardServiceImpl.SERVICE_DESCRIPTION, new CardServiceImpl());
		this.server.start();

		// 2 add servants from configuration file.
		boolean foundServant = false;
		StaffLocal servantStaff;
		for (Servant servant : this.servants) {
			if (this.address.equals(servant.getAddress())) {
				imServant = true;
			} else {
				// try to add this servant.
				try {
					servantStaff = new StaffLocal(servant.getAddress());
				} catch (RpcException e) {
					e.printStackTrace();
					// ignore it
					continue;
				}
				foundServant = true;
				this.service.addStaff(servantStaff);
			}
		}
		if (!imServant && !foundServant)
			throw new IOException("No servant found");
		boolean joinResult = false;
		// 3 Join into Viva system.
		if (imServant) {// If i am servant.
			joinResult = this.service.joinMe();
		} else {
			// Find one servant to join
			for (Iterator<StaffLocal> iterator = this.service.staffItor(); iterator
					.hasNext();) {
				StaffLocal staff = iterator.next();
				try {
					joinResult = staff.getVivaProxy().join(
							this.service.getMe().getId(),
							(short) this.address.getPort());
					if (joinResult)
						break;
				} catch (RpcException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		if (!joinResult)
			return false;
		// 4 Got all staffs from one servant.
		for (Iterator<StaffLocal> iterator = this.service.staffItor(); iterator
				.hasNext();) {
			StaffLocal staff = iterator.next();
			try {
				PortableStaff[] list = staff.getVivaProxy().list();
				for (PortableStaff portableStaff : list) {
					if (!this.service.exists(portableStaff.getId()))
						this.service.addStaff(StaffLocal
								.fromPortable(portableStaff));
				}
				break;
			} catch (RpcException e) {
				e.printStackTrace();
				continue;
			}
		}

		return true;
	}
}