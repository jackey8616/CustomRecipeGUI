package net.maple3142.customrecipegui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
	public RecipeGUIListener(Main M,String file) {
		this.M=M;
		this.store=new JsonFileArrayList<>(file);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p=(Player) e.getWhoClicked();
		ItemStack click=e.getCurrentItem();
		Inventory inv=e.getInventory();
		if(click==null)return;
		if(inv.getName()==M.config.getString("GUITitle")) {
			Material type=click.getType();
			if(type==Material.STAINED_GLASS_PANE)e.setCancelled(true);
			else if(type==Material.WOOL){
				char[] chars= {'A','B','C','D','E','F','G','H','I'};
				String[] shape= {"","",""};
				Map<Material,Character> map=new HashMap<>();
				Map<Character,Material> umap=new HashMap<>();
				int cc=0,s=3;
				for(int l=0;l<=2;l++) {
					for(int i=s;i<=s+2;i++) {
						int idx=l*10+i;
						ItemStack item=inv.getItem(idx);
						if(item==null) {
							shape[l]+=" ";
						}
						else if(!map.containsKey(item.getType())) {
							umap.put(chars[cc],item.getType());
							map.put(item.getType(),chars[cc++]);
							shape[l]+=map.get(item.getType());
						}
						else {
							shape[l]+=map.get(item.getType());
						}
					}
					s--;
				}
				ItemStack res=p.getInventory().getItemInMainHand();
				if(res.getType()==Material.AIR) {
					sendMsg(p,ChatColor.RED,"need a item in hand to create recipe");
					return;
				}
				ShapedRecipe recipe=new ShapedRecipe(res);
				recipe.shape(shape[0],shape[1],shape[2]);
				CRecipe cr=new CRecipe(M.sess.get(p.getName()),shape,res.serialize());
				for(Map.Entry<Character,Material> en:umap.entrySet()) {
					recipe.setIngredient(en.getKey(), en.getValue());
					cr.addIngredient(new CIngredient(en.getKey(),en.getValue().toString()));
				}
				if(M.getServer().addRecipe(recipe)) {
					sendMsg(p,ChatColor.GREEN,"Recipe Added!");
					store.add(new Gson().toJsonTree(cr));
				}
				p.closeInventory();
			}
		}
		else if(inv.getName()==M.config.getString("GUITitlePreview")){
			e.setCancelled(true);
		}
	}
	void sendMsg(CommandSender p,org.bukkit.ChatColor c,String msg) {
		p.sendMessage(c+msg);
	}
}
