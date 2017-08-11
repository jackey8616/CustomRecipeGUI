package net.maple3142.customrecipegui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.maple3142.customrecipegui.dataclasses.CIngredient;
import net.maple3142.customrecipegui.dataclasses.CRecipe;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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
import java.util.Map;

public class Main extends JavaPlugin {

	public String recipejson;

	public FileConfiguration config = getConfig();
	public Map<String, String> sess = new HashMap<>();
	public RecipeGUIListener rguil;

	@Override
	public void onEnable() {
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
		super.onDisable();
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		this.config = getConfig();
	}

	public void loadRecipe() {
		try {
			rguil.store = new JsonFileArrayList<>(recipejson);
			JsonReader reader = new JsonReader(new FileReader(recipejson));
			ArrayList<CRecipe> list = new Gson().fromJson(reader, new TypeToken<ArrayList<CRecipe>>() {
			}.getType());
			for (CRecipe cr : list) {
				ItemStack result = ItemStack.deserialize(cr.result);
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


}
