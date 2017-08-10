package net.maple3142.customrecipegui;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Recipecmd implements CommandExecutor{
	
	Main M;
	public Recipecmd(Main M) {
		this.M=M;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p=(Player)sender;
			if(args.length==0) {
				 sendDefault(p,cmd);
				 return true;
			}
			if(args[0].equalsIgnoreCase("add")&&args.length>=2) {
				if(!p.hasPermission("customrecipegui.add")) {
					sendMsg(p, RED, "No permission");
					return true;
				}
				for(JsonElement je:M.rguil.store) {
					CRecipe cr=new Gson().fromJson(je, CRecipe.class);
					if(cr.name.equalsIgnoreCase(args[1])) {
						sendMsg(p,RED,"Recipe name already exists.");
						return true;
					}
				}
				if(p.getInventory().getItemInMainHand().getType()==Material.AIR) {
					sendMsg(p,RED,"Need a item in hand to create recipe");
				}
				else {
					Inventory inv=M.getServer().createInventory(null,27,M.config.getString("GUItitle"));
					ItemStack ok=createItem(Material.WOOL,GREEN+"ok","",5);
					ItemStack none=createItem(Material.STAINED_GLASS_PANE,GRAY+"NONE","",0);
					for(int i=0;i<27;i++) {
						int im3=i/3;
						if(!(im3==1||im3==4||im3==7))
							inv.setItem(i, none);
					}
					inv.setItem(16, ok);
					p.openInventory(inv);
					if(M.sess.containsKey(p.getName()))
						M.sess.remove(p.getName());
					M.sess.put(p.getName(), args[1]); 
				}
			}
			else if(args[0].equalsIgnoreCase("remove")&&args.length>=2) {
				if(!p.hasPermission("customrecipegui.add")) {
					sendMsg(p, RED, "No permission");
					return true;
				}
				for(JsonElement je:M.rguil.store) {
					CRecipe cr=new Gson().fromJson(je, CRecipe.class);
					if(cr.name.equalsIgnoreCase(args[1])) {
						M.rguil.store.remove(je);
						sendMsg(p,AQUA,"Recipe Removed!");
						M.saveRecipe();
						M.getServer().resetRecipes();
						M.loadRecipe();
						
						return true;
					}
				}
				sendMsg(p,RED,"Recipe Not Found!");
			}
			else sendDefault(p,cmd);
		}
		else {
			sendMsg(sender,RED,"Only Player can execute this command");
		}
		return true;
	}

	void sendDefault(CommandSender p,Command cmd) {
		sendMsg(p,AQUA,"/"+cmd.getName()+" add <recipe name>");
		sendMsg(p,AQUA,"/"+cmd.getName()+" add <recipe name>");
	}
	void sendMsg(CommandSender p,org.bukkit.ChatColor c,String msg) {
		p.sendMessage(c+msg);
	}
	ItemStack createItem(Material m,String name,String lore,int dmg) {
		ItemStack item=new ItemStack(m,1,(byte)dmg);
		ItemMeta meta=item.getItemMeta();
		meta.setDisplayName(name);
		List<String> lr=new ArrayList<String>();
		lr.add(lore);
		meta.setLore(lr);
		item.setItemMeta(meta);
		return item;
	}
}
