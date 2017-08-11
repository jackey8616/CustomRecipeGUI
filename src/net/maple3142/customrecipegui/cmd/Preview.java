package net.maple3142.customrecipegui.cmd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.maple3142.customrecipegui.GUI;
import net.maple3142.customrecipegui.Main;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.RED;

public class Preview extends SubCommand {
	public Preview(Main M) {
		super(M);
	}

	@Override
	public boolean exec(Player p, String[] args) {
		for (JsonElement je : M.rguil.store) {
			CRecipe cr = new Gson().fromJson(je, new TypeToken<CRecipe>() {
			}.getType());
			if (cr.name.equalsIgnoreCase(args[0])) {
				ItemStack result = ItemStack.deserialize(cr.result);
				new GUI(M, M.config.getString("GUITitlePreview"))
						.addRecipeTemplateGUI()
						.addRecipeGUI(cr)
						.setRightButton(result)
						.show(p);
				return true;
			}
		}
		sendMsg(p, RED, "Recipe Not Found!");
		return true;
	}
}
