package org.creativor.viva.shell;

abstract class Command {
	public abstract String getName();

	public abstract String getDescription();

	public abstract String getUsage();

	public abstract void execute(String[] args)
			throws CommandArgumentException, CommandExecutionException;
}