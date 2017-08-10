package net.maple3142.customrecipegui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Main extends JavaPlugin{
	
	public String recipejson;
	
	public FileConfiguration config=getConfig();
	public Map<String, String> sess=new HashMap<>();
	public RecipeGUIListener rguil;
	@Override
	public void onEnable() {
		recipejson=this.getDataFolder().getAbsolutePath()+"/recipe.json";
		rguil=new RecipeGUIListener(this);
		Path pr=Paths.get(recipejson);
		if(!Files.exists(pr, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createFile(pr);
				PrintWriter pw=new PrintWriter(recipejson);
				pw.println("[]");
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.loadRecipe();
		config.addDefault("GUItitle", "RecipeGUI");
		config.options().copyDefaults(true);
		saveConfig();
		this.getCommand("recipegui").setExecutor(new Recipecmd(this));
		getServer().getPluginManager().registerEvents(rguil,this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		this.saveRecipe();
		super.onDisable();
	}

	public void saveRecipe() {
		String json=new Gson().toJson(rguil.store);
		try {
			
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(recipejson), Charset.forName("UTF-8")));
			pw.println(json);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
	
	public void loadRecipe() {
		try {
			JsonReader reader=new JsonReader(new FileReader(recipejson));
			ArrayList<JsonElement> el=new ArrayList<>();
			ArrayList<CRecipe> list=new Gson().fromJson(reader, new TypeToken<ArrayList<CRecipe>>(){}.getType());
			for(CRecipe rc:list) {
				ItemStack result=ItemStack.deserialize(rc.result);
				ShapedRecipe recipe=new ShapedRecipe(result);
				recipe.shape(rc.shape[0],rc.shape[1],rc.shape[2]);
				for(CIngredient ig:rc.ingredient) {
					recipe.setIngredient(ig.ch, Material.getMaterial(ig.material));
				}
				getServer().addRecipe(recipe);
				el.add(new Gson().toJsonTree(rc));
			}
			rguil.store=el;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
