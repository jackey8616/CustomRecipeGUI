package net.maple3142.customrecipegui.cmd;

import net.maple3142.customrecipegui.Main;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GREEN;

public class Reloadcfg extends SubCommand {
	public Reloadcfg(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		M.reloadConfig();
		sendMsg(p, GREEN, "Config reloaded");
		return true;
	}
}
