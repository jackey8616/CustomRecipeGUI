package net.maple3142.customrecipegui.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.maple3142.customrecipegui.Main;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class List extends SubCommand {
	public List(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		ArrayList<String> list = new ArrayList<>();
		for (JsonElement je : M.rguil.store) {
			CRecipe cr = new Gson().fromJson(je, CRecipe.class);
			list.add(cr.name);
		}
		if (list.size() > 0) {
			sendMsg(p, GREEN, "Recipes list:");
			for (String s : list) {
				sendMsg(p, GREEN, " - " + s);
			}
		} else sendMsg(p, RED, "No recipes found");
		return true;
	}
}
