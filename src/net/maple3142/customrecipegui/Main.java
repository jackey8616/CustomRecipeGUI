package net.maple3142.customrecipegui;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.maple3142.customrecipegui.dataclasses.CIngredient;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main extends JavaPlugin {

	public String recipejson;

	public FileConfiguration config = getConfig();
	public Map<String, String> sess = new HashMap<>();
	public RecipeGUIListener rguil;

	@Override
	public void onEnable() {
		System.out.println("OnEnable");
		recipejson = this.getDataFolder().getAbsolutePath() + "/recipe.json";
		rguil = new RecipeGUIListener(this, recipejson);
		Path pr = Paths.get(recipejson);
		if (!Files.exists(pr, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createFile(pr);
				PrintWriter pw = new PrintWriter(recipejson);
				pw.println("[]");
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.loadRecipe();
		config.addDefault("GUITitle", "RecipeGUI");
		config.addDefault("GUITitlePreview", "RecipeGUIPreview");
		config.addDefault("GUITitleEdit", "RecipeGUIEdit");
		config.options().copyDefaults(true);
		saveConfig();
		this.getCommand("recipegui").setExecutor(new Recipecmd(this));
		getServer().getPluginManager().registerEvents(rguil, this);
		super.onEnable();
	}

	@Override
	public void onDisable() {
		System.out.println("OnDisable");
		super.onDisable();
	}

	@Override
	public void reloadConfig() {
		System.out.println("OnReloadConfig");
		super.reloadConfig();
		this.config = getConfig();
	}

	public void loadRecipe() {
		System.out.println("OnReloadRecipe");
		try {
			rguil.store = new JsonFileArrayList<>(recipejson);
			JsonReader reader = new JsonReader(new FileReader(recipejson));
			ArrayList<CRecipe> list = new Gson().fromJson(reader, new TypeToken<ArrayList<CRecipe>>() {
			}.getType());
			for (CRecipe cr : list) {
				ItemStack result = deserialize(cr.result);
				ShapedRecipe recipe = new ShapedRecipe(result);
				recipe.shape(cr.shape[0], cr.shape[1], cr.shape[2]);
				for (CIngredient ig : cr.ingredient) {
					recipe.setIngredient(ig.ch, Material.getMaterial(ig.material));
				}
				getServer().addRecipe(recipe);
				rguil.store.addNoUpdate(new Gson().toJsonTree(cr));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Copy from 'org.inventory.ItemStack#deserialize'
	public ItemStack deserialize(Map<String, Object> args) {
		Material type = Material.getMaterial((String)args.get("type"));
		short damage = 0;
		int amount = 1;
		if (args.containsKey("damage")) {
			damage = ((Number)args.get("damage")).shortValue();
		}

		if (args.containsKey("amount")) {
			amount = ((Number)args.get("amount")).intValue();
		}

		ItemStack result = new ItemStack(type, amount, damage);
		Object raw;
		if (args.containsKey("enchantments")) {
			// This never be call.
			raw = args.get("enchantments");
			if (raw instanceof Map) {
				Map<?, ?> map = (Map)raw;
				Iterator var7 = map.entrySet().iterator();

				while(var7.hasNext()) {
					Map.Entry<?, ?> entry = (Map.Entry)var7.next();
					Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());
					if (enchantment != null && entry.getValue() instanceof Integer) {
						result.addUnsafeEnchantment(enchantment, ((Integer)entry.getValue()).intValue());
					}
				}
			}
		} else if (args.containsKey("meta")) {
			// It's actually here.
			LinkedTreeMap<String, Object> a = (LinkedTreeMap<String, Object>) args.get("meta");
			Object innerRaw = a.get("enchantments");
			if (innerRaw instanceof Map) {
				Map<?, ?> map = (Map)innerRaw;
				Iterator var7 = map.entrySet().iterator();

				while(var7.hasNext()) {
					Map.Entry<?, ?> entry = (Map.Entry)var7.next();
					String enchantmentName = entry.getKey().toString().substring(entry.getKey().toString().indexOf(", ") + 2, entry.getKey().toString().lastIndexOf("]"));
					//Enchantment enchantment = Enchantment.getByName(ntry.getKey().toString());
					Enchantment enchantment = Enchantment.getByName(enchantmentName);

					//if (enchantment != null && entry.getValue() instanceof Integer) {
					if (enchantment != null && entry.getValue() instanceof Number) {
						//result.addUnsafeEnchantment(enchantment, ((Integer)entry.getValue()).intValue());
						result.addUnsafeEnchantment(enchantment, ((Number) entry.getValue()).intValue());
					}
				}
			}

			// Origin code
			raw = args.get("meta");
			if (raw instanceof ItemMeta) {
				result.setItemMeta((ItemMeta)raw);
			}
		}

		return result;
	}
}
