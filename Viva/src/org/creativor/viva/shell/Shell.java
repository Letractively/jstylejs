/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva.shell;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import org.creativor.rayson.exception.RpcException;
import org.creativor.viva.StaffLocal;
import org.creativor.viva.api.PortableStaff;

/**
 *
 * @author Nick Zhang
 */
final class Shell {
	private static final String COMMAND_DELIM = " ";

	public static final short VERSION = 1;

	private static final String TYPE_TIPS = "Please type you command:";

	private static final String COMMAND_INPUT_TITLE = "$viva>";

	private static StringBuffer HELP_INFO = null;

	private static final int HELP_INFO_MAX_LENGTH = 160;

	private static Console CONSOLE;

	private static PrintStream SILENT_PRINT_STREAM;
	private static PrintStream DEFAULT_PRINT_STREAM = System.out;
	static {
		try {
			SILENT_PRINT_STREAM = new PrintStream(File.createTempFile(
					"viva_log", ""));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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
			CONSOLE.writer().println("bye!");
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

	private static Command ECHO_COMMAND = new Command() {

		@Override
		public String getName() {
			return "echo";
		}

		@Override
		public String getDescription() {
			return "Make system logger informations on or off";
		}

		@Override
		public String getUsage() {
			return getName() + " on(off)";
		}

		@Override
		public void execute(String[] args) throws CommandArgumentException,
				CommandExecutionException {
			boolean echo = false;
			if (args == null || args.length == 0)
				echo = true;
			else {
				String arg = args[0];
				if (arg.equalsIgnoreCase("on"))
					echo = true;
			}
			if (!echo) {
				System.setErr(SILENT_PRINT_STREAM);
				System.setOut(SILENT_PRINT_STREAM);
			} else {
				System.setErr(DEFAULT_PRINT_STREAM);
				System.setOut(DEFAULT_PRINT_STREAM);
			}

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
			try {
				CONSOLE.writer().println(
						STAFF_LOCAL.getVivaProxy().pictureStaffs());
			} catch (RpcException e) {
				throw new CommandExecutionException(e);
			}
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
			CONSOLE.writer()
					.println(
							"Unknown command, please type help for help information of this shell");
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
			CONSOLE.writer().println(HELP_INFO.toString());
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
		CONSOLE = System.console();

		// show help information first
		CONSOLE.writer().println(HELP_INFO.toString());
		CONSOLE.writer().println(TYPE_TIPS);
		// echo off
		try {
			ECHO_COMMAND.execute(new String[] { "off" });
		} catch (CommandArgumentException e1) {
			CONSOLE.writer().print(e1.getMessage());
		} catch (CommandExecutionException e1) {
			CONSOLE.writer().print(e1.getMessage());
		}
		String commandString;
		String[] commandArgs;
		String commandName;
		Command command = null;
		CONSOLE.writer().print(COMMAND_INPUT_TITLE);
		CONSOLE.writer().flush();
		while ((commandString = CONSOLE.readLine()) != null) {
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
				CONSOLE.writer().println(
						command.getName() + " usage: " + command.getUsage());
			} catch (CommandExecutionException e) {
				e.printStackTrace();
				CONSOLE.writer().println(
						"Command execute error: " + e.getMessage());
			}
			CONSOLE.writer().print("\n");
			CONSOLE.writer().print(COMMAND_INPUT_TITLE);
			CONSOLE.writer().flush();
		}
	}

	private static Command findCommand(String commandName) {
		for (Command command : COMMANDS) {
			if (command.getName().equalsIgnoreCase(commandName.trim()))
				return command;
		}
		return UNKNOWN_COMMAND;
	}

	private static String appendString(String source, int toLength,
			char fillChar) {
		int sourceLength = source.length();
		if (toLength < sourceLength)
			toLength = sourceLength;
		char[] fillChars = new char[toLength - sourceLength];
		Arrays.fill(fillChars, fillChar);
		return source + new String(fillChars);
	}

	private static String getCommandHelpString(Command command) {
		int namePartLength = 15;
		String namePart = appendString(command.getName(), namePartLength, ' ');
		int usagePartLegth = 35;
		String usagePart = appendString(command.getUsage(), usagePartLegth, ' ');
		String descriptionPart = appendString(command.getDescription(),
				HELP_INFO_MAX_LENGTH - namePartLength - usagePartLegth - 7, ' ');
		return "| " + namePart + "| " + descriptionPart + "| " + usagePart
				+ "|";
	}

	private static void setupHelpInformation() {
		HELP_INFO = new StringBuffer();
		String helpTitle = " Viva system shell, version " + VERSION + " ";
		int starsLength = HELP_INFO_MAX_LENGTH / 2 - helpTitle.length();
		HELP_INFO.append("+"
				+ appendString(appendString("=", starsLength, '=') + helpTitle,
						HELP_INFO_MAX_LENGTH - 2, '=') + "+");
		HELP_INFO.append("\n");
		for (Command command : COMMANDS) {
			HELP_INFO.append(getCommandHelpString(command));
			HELP_INFO.append("\n");
		}
		HELP_INFO.append("+" + appendString("=", HELP_INFO_MAX_LENGTH - 2, '=')
				+ "+");
	}

	private static void setupKnownCommands() {
		COMMANDS = new ArrayList<Command>();
		COMMANDS.add(CONNECT_COMMAND);
		COMMANDS.add(DISCONNECT_COMMAND);
		COMMANDS.add(HELP_COMMAND);
		COMMANDS.add(LIST_COMMAND);
		COMMANDS.add(QUIT_COMMAND);
		COMMANDS.add(ECHO_COMMAND);
	}

	private static void checkConnection() throws CommandExecutionException {
		if (STAFF_LOCAL == null)
			throw new CommandExecutionException(new IllegalStateException(
					"Not connected to Viva system yet, please type help"));
	}
}
