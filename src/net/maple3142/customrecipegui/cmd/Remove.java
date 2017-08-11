package net.maple3142.customrecipegui.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.maple3142.customrecipegui.Main;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class Remove extends SubCommand {
	public Remove(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		for (JsonElement je : M.rguil.store) {
			CRecipe cr = new Gson().fromJson(je, new TypeToken<CRecipe>(){}.getType());
			if (cr.name.equalsIgnoreCase(args[0])) {
				M.rguil.store.remove(je);
				M.getServer().resetRecipes();
				M.loadRecipe();
				if(args.length>1)
					return true;//from edit
				sendMsg(p, GREEN, "Recipe Removed!");
				return true;
			}
		}
		sendMsg(p, RED, "Recipe Not Found!");
		return true;
	}
}
