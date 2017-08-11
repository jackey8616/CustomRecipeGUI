package net.maple3142.customrecipegui;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.maple3142.customrecipegui.dataclasses.CIngredient;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;

public class GUI {
	Inventory inv;
	Main M;

	public GUI(Main M, String name) {
		this.M = M;
		inv = M.getServer().createInventory(null, 27, name);
	}

	public GUI show(Player p) {
		p.openInventory(inv);
		return this;
	}

	public GUI addRecipeTemplateGUI() {
		ItemStack ok = createItem(Material.WOOL, GREEN + "OK", "", 5);
		ItemStack none = createItem(Material.STAINED_GLASS_PANE, GRAY + "NONE", "", 0);
		for (int i = 0; i < 27; i++) {
			int im3 = i / 3;
			if (!(im3 == 1 || im3 == 4 || im3 == 7))
				inv.setItem(i, none);
		}
		setRightButton(ok);
		return this;
	}

	public GUI addRecipeGUI(CRecipe rc) {
		Map<Character, Material> map = new HashMap<>();
		map.put(' ', Material.AIR);
		for (CIngredient ci : rc.ingredient) {
			map.put(ci.ch, Material.getMaterial(ci.material));
		}
		int s = 3;
		for (int l = 0; l <= 2; l++) {
			for (int i = s; i <= s + 2; i++) {
				int idx = l * 10 + i;
				char ch = rc.shape[l].charAt(i - s);
				if (ch == ' ') continue;
				Material m = map.get(ch);
				ItemStack item = createItem(m, null, null, 0);
				inv.setItem(idx, item);
			}
			s--;
		}
		return this;
	}

	public GUI setRightButton(ItemStack item) {
		inv.setItem(16, item);
		return this;
	}

	ItemStack createItem(@NotNull Material m,
	                     @Nullable String name,
	                     @Nullable String lore,
	                     @NotNull int dmg) {
		ItemStack item = new ItemStack(m, 1, (byte) dmg);
		ItemMeta meta = item.getItemMeta();
		if (name != null) meta.setDisplayName(name);
		List<String> lr = new ArrayList<String>();
		if (lore != null) lr.add(lore);
		meta.setLore(lr);
		item.setItemMeta(meta);
		return item;
	}
}
