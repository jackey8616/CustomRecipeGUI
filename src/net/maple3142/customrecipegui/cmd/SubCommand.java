package net.maple3142.customrecipegui.cmd;

import net.maple3142.customrecipegui.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {
	public Main M;

	public SubCommand(Main M) {
		this.M = M;
	}

	public abstract boolean exec(Player p, String[] args);

	void sendMsg(CommandSender p, org.bukkit.ChatColor c, String msg) {
		p.sendMessage(c + msg);
	}
}
