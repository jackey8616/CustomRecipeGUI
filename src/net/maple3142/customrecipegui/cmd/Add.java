package net.maple3142.customrecipegui.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.maple3142.customrecipegui.GUI;
import net.maple3142.customrecipegui.Main;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class Add extends SubCommand {
	public Add(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		for (JsonElement je : M.rguil.store) {
			CRecipe cr = new Gson().fromJson(je, CRecipe.class);
			if (cr.name.equalsIgnoreCase(args[0])) {
				sendMsg(p, RED, "Recipe name already exists.");
				return true;
			}
		}
		if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
			sendMsg(p, RED, "Need a item in main hand to create recipe");
		}
		else {
			new GUI(M, M.config.getString("GUITitle"))
					.addRecipeTemplateGUI()
					.show(p);
			if (M.sess.containsKey(p.getName()))
				M.sess.remove(p.getName());
			M.sess.put(p.getName(), args[0]);
		}
		return true;
	}
}
