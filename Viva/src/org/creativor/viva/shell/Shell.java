package org.creativor.viva.shell;

import java.io.Console;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.creativor.rayson.exception.RpcException;
import org.creativor.viva.StaffLocal;
import org.creativor.viva.api.PortableStaff;

final class Shell {
	private static final String COMMAND_DELIM = " ";

	private static final String TYPE_TIPS = "Please type you command:";

	private static StringBuffer HELP_INFO = null;

	private static List<Command> COMMANDS = null;

	private static Command CONNECT_COMMAND = new Command() {

		@Override
		public String getUsage() {
			return getName() + " ip port";
		}

		@Override
		public String getName() {
			return "connect";
		}

		@Override
		public String getDescription() {
			return "Connect to a staff in Viva system, by giving the ip and port number of one staff";
		}

		@Override
		public void execute(String[] args) throws CommandArgumentException,
				CommandExecutionException {
			if (args == null || args.length != 2)
				throw new CommandArgumentException();
			String ip = args[0];
			short port = Short.parseShort(args[1]);
			try {
				STAFF_LOCAL = new StaffLocal(new InetSocketAddress(ip, port));
			} catch (RpcException e) {
				throw new CommandExecutionException(e);
			}
		}
	};
	private static Command QUIT_COMMAND = new Command() {

		@Override
		public String getUsage() {
			return getName();
		}

		@Override
		public String getName() {
			return "quit";
		}

		@Override
		public String getDescription() {
			return "Quit this shell";
		}

		@Override
		public void execute(String[] args) throws CommandArgumentException,
				CommandExecutionException {
			System.exit(0);
		}
	};
	private static Command DISCONNECT_COMMAND = new Command() {

		@Override
		public String getUsage() {
			return getName();
		}

		@Override
		public String getName() {
			return "disconnect";
		}

		@Override
		public String getDescription() {
			return "disconnect to Viva system";
		}

		@Override
		public void execute(String[] args) throws CommandArgumentException,
				CommandExecutionException {
			STAFF_LOCAL = null;
		}
	};
	private static Command LIST_COMMAND = new Command() {

		@Override
		public String getUsage() {
			return getName();
		}

		@Override
		public String getName() {
			return "list";
		}

		@Override
		public String getDescription() {
			return "List all staffs in Viva system";
		}

		@Override
		public void execute(String[] args) throws CommandArgumentException,
				CommandExecutionException {
			checkConnection();
			PortableStaff[] staffs;
			try {
				staffs = STAFF_LOCAL.getVivaProxy().list();
			} catch (RpcException e) {
				throw new CommandExecutionException(e);
			}
			System.out.println(Arrays.toString(staffs));
		}
	};

	private static StaffLocal STAFF_LOCAL;

	private static Command UNKNOWN_COMMAND = new Command() {

		@Override
		public String getUsage() {
			return getName();
		}

		@Override
		public String getName() {
			return "unknown";
		}

		@Override
		public void execute(String[] args) {
			System.err
					.println("Unknown command, please type help for help information of this shell");
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	private static Command HELP_COMMAND = new Command() {

		@Override
		public String getUsage() {
			return getName();
		}

		@Override
		public String getName() {
			return "help";
		}

		@Override
		public void execute(String[] args) {
			System.out.println(HELP_INFO.toString());
		}

		@Override
		public String getDescription() {
			return "show help information of this shell";
		}
	};
	static {
		setupKnownCommands();
		setupHelpInformation();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// show help information first
		System.out.println(HELP_INFO.toString());
		System.out.println(TYPE_TIPS);
		Console console = System.console();
		String commandString;
		String[] commandArgs;
		String commandName;
		Command command = null;
		while ((commandString = console.readLine()) != null) {
			if (commandString.isEmpty())
				commandString = UNKNOWN_COMMAND.getName();
			StringTokenizer stringTokenizer = new StringTokenizer(
					commandString, COMMAND_DELIM);
			commandArgs = new String[stringTokenizer.countTokens() - 1];
			int i = 0;
			while (stringTokenizer.hasMoreElements()) {
				String commandArg = (String) stringTokenizer.nextElement();
				if (i == 0) {
					i++;
					commandName = commandArg;
					command = findCommand(commandName);
					continue;
				}
				commandArgs[i - 1] = commandArg;
				i++;
			}
			try {
				command.execute(commandArgs);
			} catch (CommandArgumentException e) {
				System.out.println(command.getName() + " usage: "
						+ command.getUsage());
			} catch (CommandExecutionException e) {
				e.printStackTrace();
			}

			System.out.println(TYPE_TIPS);
		}
	}

	private static Command findCommand(String commandName) {
		for (Command command : COMMANDS) {
			if (command.getName().equalsIgnoreCase(commandName.trim()))
				return command;
		}
		return UNKNOWN_COMMAND;
	}

	private static void setupHelpInformation() {
		HELP_INFO = new StringBuffer();
		HELP_INFO
				.append("*****************help information****************************");
		HELP_INFO.append("\n");
		for (Command command : COMMANDS) {
			HELP_INFO.append(command.getName());
			HELP_INFO.append("       ");
			HELP_INFO.append(command.getDescription());
			HELP_INFO.append("       usage: ");
			HELP_INFO.append(command.getUsage());
			HELP_INFO.append("\n");
		}
		HELP_INFO
				.append("******************************************************************");
	}

	private static void setupKnownCommands() {
		COMMANDS = new ArrayList<Command>();
		COMMANDS.add(CONNECT_COMMAND);
		COMMANDS.add(DISCONNECT_COMMAND);
		COMMANDS.add(HELP_COMMAND);
		COMMANDS.add(LIST_COMMAND);
		COMMANDS.add(QUIT_COMMAND);
	}

	private static void checkConnection() throws CommandExecutionException {
		if (STAFF_LOCAL == null)
			throw new CommandExecutionException(new IllegalStateException(
					"Not connected to Viva system yet, please type help"));
	}
}
