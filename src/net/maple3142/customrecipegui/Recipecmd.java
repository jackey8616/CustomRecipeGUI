package net.maple3142.customrecipegui;

import net.maple3142.customrecipegui.cmd.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

public class Recipecmd implements CommandExecutor {

	Main M;

	public Recipecmd(Main M) {
		this.M = M;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sendMsg(sender, RED, "Only Player can execute this command");
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			sendDefault(p, cmd);
			return true;
		}
		if (!p.hasPermission("customrecipegui." + args[0].toLowerCase())) {
			sendMsg(p, RED, "No permission");
			return true;
		}

		if (args[0].equalsIgnoreCase("add") && args.length >= 2)
			return new Add(M).exec(p, slice(args, 1));

		if (args[0].equalsIgnoreCase("remove") && args.length >= 2)
			return new Remove(M).exec(p, slice(args, 1));

		if (args[0].equalsIgnoreCase("preview") && args.length >= 2)
			return new Preview(M).exec(p, slice(args, 1));

		if (args[0].equalsIgnoreCase("edit") && args.length >= 2)
			return new Edit(M).exec(p, slice(args, 1));

		if (args[0].equalsIgnoreCase("list") && args.length >= 1)
			return new List(M).exec(p, slice(args, 0));

		if (args[0].equalsIgnoreCase("reload") && args.length >= 1)
			return new Reload(M).exec(p, slice(args, 0));

		if (args[0].equalsIgnoreCase("reloadcfg") && args.length >= 1)
			return new Reloadcfg(M).exec(p, slice(args, 0));

		sendDefault(p, cmd);

		return true;
	}

	void sendDefault(CommandSender p, Command cmd) {
		if (!p.hasPermission("customrecipegui")) {
			sendMsg(p, RED, "No permission");
			return;
		}
		sendMsg(p, GREEN, "CustomRecipeGUI Commands:");
		sendMsg(p, AQUA, "/" + cmd.getName() + " add <recipe name>");
		sendMsg(p, AQUA, "/" + cmd.getName() + " remove <recipe name>");
		sendMsg(p, AQUA, "/" + cmd.getName() + " preview <recipe name>");
		sendMsg(p, AQUA, "/" + cmd.getName() + " edit <recipe name>");
		sendMsg(p, AQUA, "/" + cmd.getName() + " list");
		sendMsg(p, AQUA, "/" + cmd.getName() + " reload");
		sendMsg(p, AQUA, "/" + cmd.getName() + " reloadcfg");
	}

	void sendMsg(CommandSender p, org.bukkit.ChatColor c, String msg) {
		p.sendMessage(c + msg);
	}

	<T> T[] slice(T[] ar, int n) {
		if (n == 0) return ar;
		return Arrays.copyOfRange(ar, n, ar.length);
	}
}
