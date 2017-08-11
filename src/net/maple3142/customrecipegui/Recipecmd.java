package net.maple3142.customrecipegui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static org.bukkit.ChatColor.*;

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
					sendMsg(p,RED,"Need a item in main hand to create recipe");
				}
				else {
					new GUI(M,M.config.getString("GUITitle"))
							.addRecipeTemplateGUI()
							.show(p);
					if(M.sess.containsKey(p.getName()))
						M.sess.remove(p.getName());
					M.sess.put(p.getName(), args[1]);
				}
			}
			else if(args[0].equalsIgnoreCase("remove")&&args.length>=2) {
				if(!p.hasPermission("customrecipegui.remove")) {
					sendMsg(p, RED, "No permission");
					return true;
				}
				for(JsonElement je:M.rguil.store) {
					CRecipe cr=new Gson().fromJson(je, CRecipe.class);
					if(cr.name.equalsIgnoreCase(args[1])) {
						M.rguil.store.remove(je);
						sendMsg(p,GREEN,"Recipe Removed!");
						M.getServer().resetRecipes();
						M.loadRecipe();
						return true;
					}
				}
				sendMsg(p,RED,"Recipe Not Found!");
			}
			else if(args[0].equalsIgnoreCase("preview")&&args.length>=2){
				if(!p.hasPermission("customrecipegui.preview")) {
					sendMsg(p, RED, "No permission");
					return true;
				}
				for(JsonElement je:M.rguil.store) {
					CRecipe cr=new Gson().fromJson(je, CRecipe.class);
					if(cr.name.equalsIgnoreCase(args[1])) {
						new GUI(M,M.config.getString("GUITitlePreview"))
								.addRecipeTemplateGUI()
								.addRecipeGUI(cr)
								.show(p);
						return true;
					}
				}
				sendMsg(p,RED,"Recipe Not Found!");
			}
			else if(args[0].equalsIgnoreCase("list")&&args.length>=1){
				if(!p.hasPermission("customrecipegui.list")) {
					sendMsg(p, RED, "No permission");
					return true;
				}
				ArrayList<String> list=new ArrayList<>();
				for(JsonElement je:M.rguil.store) {
					CRecipe cr=new Gson().fromJson(je, CRecipe.class);
					list.add(cr.name);
				}
				if(list.size()>0){
					sendMsg(p,GREEN,"Recipes list:");
					for(String s:list){
						sendMsg(p,GREEN," - "+s);
					}
				}
				else sendMsg(p,RED,"No recipes found");
			}
			else if(args[0].equalsIgnoreCase("reloadcfg")&&args.length>=1){
				if(!p.hasPermission("customrecipegui.reloadcfg")) {
					sendMsg(p, RED, "No permission");
					return true;
				}
				M.reloadConfig();
				sendMsg(p,GREEN,"Config reloaded");
			}
			else sendDefault(p,cmd);
			//TODO: "/reload": reload recipes
		}
		else {
			sendMsg(sender,RED,"Only Player can execute this command");
		}
		return true;
	}

	void sendDefault(CommandSender p,Command cmd) {
		if(p.hasPermission("customrecipegui")){
			sendMsg(p,GREEN,"CustomRecipeGUI Commands:");
			sendMsg(p,AQUA,"/"+cmd.getName()+" add <recipe name>");
			sendMsg(p,AQUA,"/"+cmd.getName()+" remove <recipe name>");
			sendMsg(p,AQUA,"/"+cmd.getName()+" preview <recipe name>");
			sendMsg(p,AQUA,"/"+cmd.getName()+" list");
			sendMsg(p,AQUA,"/"+cmd.getName()+" reloadcfg");
			return;
		}
		sendMsg(p, RED, "No permission");
	}
	void sendMsg(CommandSender p,org.bukkit.ChatColor c,String msg) {
		p.sendMessage(c+msg);
	}

}
