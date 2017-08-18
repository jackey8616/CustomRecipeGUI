package net.maple3142.customrecipegui.cmd;

import net.maple3142.customrecipegui.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {
	public Reload(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		M.getServer().resetRecipes();
		M.loadRecipe();
		sendMsg(p, ChatColor.GREEN, "Recipes reloaded");
		return true;
	}
}
