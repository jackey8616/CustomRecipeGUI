package net.maple3142.customrecipegui.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.maple3142.customrecipegui.GUI;
import net.maple3142.customrecipegui.Main;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class Edit extends SubCommand {
	public Edit(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
			sendMsg(p, RED, "Need a item in main hand to edit recipe");
			return true;
		}
		if (M.sess.containsKey(p.getName()))
			M.sess.remove(p.getName());
		M.sess.put(p.getName(), args[0]);
		for (JsonElement je : M.rguil.store) {
			CRecipe cr = new Gson().fromJson(je, new TypeToken<CRecipe>(){}.getType());
			if (cr.name.equalsIgnoreCase(args[0])) {
				new GUI(M, M.config.getString("GUITitleEdit"))
						.addRecipeTemplateGUI()
						.addRecipeGUI(cr)
						.show(p);
				return true;
			}
		}
		sendMsg(p, RED, "Recipe Not Found!");
		return true;
	}
}
