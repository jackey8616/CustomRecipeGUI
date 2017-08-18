package net.maple3142.customrecipegui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.maple3142.customrecipegui.cmd.Remove;
import net.maple3142.customrecipegui.dataclasses.CIngredient;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.Map;

public class RecipeGUIListener implements Listener {
	Main M;
	public JsonFileArrayList<JsonElement> store;

	public RecipeGUIListener(Main M, String file) {
		this.M = M;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack click = e.getCurrentItem();
		Inventory inv = e.getInventory();
		if (click == null) return;
		if (inv.getName() == M.config.getString("GUITitlePreview")) {
			e.setCancelled(true);
			return;
		}
		if (inv.getName() != M.config.getString("GUITitle") && inv.getName() != M.config.getString("GUITitleEdit"))
			return;

		Material type = click.getType();
		if (type == Material.STAINED_GLASS_PANE) {
			e.setCancelled(true);
			return;
		}
		if (type != Material.WOOL) return;

		char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
		String[] shape = {"", "", ""};
		Map<Material, Character> map = new HashMap<>();
		Map<Character, Material> umap = new HashMap<>();
		int cc = 0, s = 3;
		for (int l = 0; l <= 2; l++) {
			for (int i = s; i <= s + 2; i++) {
				int idx = l * 10 + i;
				ItemStack item = inv.getItem(idx);
				if (item == null) {
					shape[l] += " ";
				} else if (!map.containsKey(item.getType())) {
					umap.put(chars[cc], item.getType());
					map.put(item.getType(), chars[cc++]);
					shape[l] += map.get(item.getType());
				} else {
					shape[l] += map.get(item.getType());
				}
			}
			s--;
		}
		ItemStack result = p.getInventory().getItemInMainHand();
		if (result.getType() == Material.AIR) {
			sendMsg(p, ChatColor.RED, "need a item in hand to create recipe");
			return;
		}
		ShapedRecipe recipe = new ShapedRecipe(result);
		recipe.shape(shape[0], shape[1], shape[2]);
		CRecipe cr = new CRecipe(M.sess.get(p.getName()), shape, result.serialize());
		for (Map.Entry<Character, Material> en : umap.entrySet()) {
			recipe.setIngredient(en.getKey(), en.getValue());
			cr.addIngredient(new CIngredient(en.getKey(), en.getValue().toString()));
		}
		if (inv.getName() == M.config.getString("GUITitleEdit"))
			new Remove(M).exec(p, new String[]{M.sess.get(p.getName()), ""});
		if (!M.getServer().addRecipe(recipe)) {
			p.closeInventory();
			return;
		}
		JsonElement obj;
		store.add(obj = new Gson().toJsonTree(cr));
		if (inv.getName() == M.config.getString("GUITitleEdit")) {
			p.closeInventory();
			sendMsg(p, ChatColor.GREEN, "Recipe Edited!");
			return;
		}
		sendMsg(p, ChatColor.GREEN, "Recipe Added!");
		p.closeInventory();
	}

	void sendMsg(CommandSender p, org.bukkit.ChatColor c, String msg) {
		p.sendMessage(c + msg);
	}
}
