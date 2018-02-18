package mincrasethome;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Sethome implements CommandExecutor {
	String st1,st2;
	private Boolean limit;
	private double x_s,y_s,z_s;
	private double x_e,y_e,z_e;
	private List<String> ExceptionWorld;
	private Boolean exception;
	private HashMap<String,Integer> pexlist = new HashMap<String,Integer>();
	public Sethome(){
		 List<Map<?, ?>> list = MincraSetHome.config.getMapList("groups");
		if(list.isEmpty()) Bukkit.getLogger().info("[MincraSethome]"+" "+"[読み込みエラー]"+"Groupsが読み込めませんでした。");
		for(Map<?, ?> e:list){
			for(Entry<?, ?> group:e.entrySet()){
				Integer pexlimit = (Integer) group.getValue();
				String pexname = (String) group.getKey();
				if(pexlimit!=null&&pexname!=null){
					pexlist.put(pexname,pexlimit);
					Bukkit.getLogger().info("[MincraSethome]"+" "+"権限:"+pexname+" "+"制限量:"+pexlimit);
				}else{
					Bukkit.getLogger().info("[MincraSethome]"+" "+"[読み込みエラー]"+group);
				}
				if(!pexlist.containsKey("default")){
					Bukkit.getLogger().info("[MincraSethome]"+" "+"[読み込みエラー]"+"デフォルト値が読み込めませんでした。");
					pexlist.put("default",1);
				}
			}
		}
		exception=MincraSetHome.config.getBoolean("ExceptionWorld");
		if(exception){
			ExceptionWorld=MincraSetHome.config.getStringList("World");
			st2=MincraSetHome.config.getString("string2");
		}
		limit=MincraSetHome.config.getBoolean("limit");
		if(limit){
			x_s=MincraSetHome.config.getDouble("Location.x_start");
			y_s=MincraSetHome.config.getDouble("Location.y_start");
			z_s=MincraSetHome.config.getDouble("Location.z_start");
			x_e=MincraSetHome.config.getDouble("Location.x_end");
			y_e=MincraSetHome.config.getDouble("Location.y_end");
			z_e=MincraSetHome.config.getDouble("Location.z_end");
			if(x_s>x_e){
				double swap;
				swap=x_s;
				x_s=x_e;
				x_e=swap;
			}
			if(y_s>y_e){
				double swap;
				swap=y_s;
				y_s=y_e;
				y_e=swap;
			}
			if(z_s>z_e){
				double swap;
				swap=z_s;
				z_s=z_e;
				z_e=swap;
			}
			st1=MincraSetHome.config.getString("Location.string");
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			return false;
		}
		if (!sender.hasPermission("mincra.home")) {
            sender.sendMessage("mincra.homeの権限を持っていない。");
            return true;
        }
		Player player=(Player)sender;
		if(exception){
			if(ExceptionWorld.contains(player.getWorld().getName())){
				player.sendMessage(st2);
				return true;
			}
		}
		if(limit){
				Location l=player.getLocation();
				if(l.getX()<x_s||l.getX()>x_e){
					player.sendMessage(st1);
					return true;
				}
				if(l.getY()<y_s||l.getY()>y_e){
					player.sendMessage(st1);
					return true;
				}
				if(l.getZ()<z_s||l.getZ()>z_e){
					player.sendMessage(st1);
					return true;
				}
			/*if(args.length>=3){
				Double args1=Double.valueOf("args[0]"),args2=Double.valueOf("args[1]"),args3=Double.valueOf("args[2]");
				if(!(args1>x_s&&args1<x_e)){
					player.sendMessage(st1);
					return true;
				}
				if(!(args2>y_s&&args2<y_e)){
					player.sendMessage(st1);
					return true;
				}
				if(!(args3>z_s&&args3<z_e)){
					player.sendMessage(st1);
					return true;
				}
			}*/
		}
	    File folder = new File(MincraSetHome.plugin.getDataFolder(), "userdata");
	    if (!(folder.exists())){
	    	folder.mkdirs();
	    }
	    File userdata = new File(folder,player.getUniqueId()+".yml");
		    if(!userdata.exists()){
		    	try {
					userdata.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    }
		/*if(args.length>=3){
			Double args1=Double.valueOf("args[0]"),args2=Double.valueOf("args[1]"),args3=Double.valueOf("args[2]");
	    	FileConfiguration config2=new YamlConfiguration();
			try {
				config2.load(userdata);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
			Location loc=player.getLocation();
			config2.set("Location.X", args1);
			config2.set("Location.Y", args2);
			config2.set("Location.Z", args3);
			config2.set("Location.Yaw", loc.getYaw());
			config2.set("Location.Pitch", loc.getPitch());
			config2.set("Location.World", player.getWorld().getName());
			try {
				config2.save(userdata);
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.sendMessage(ChatColor.GREEN+"デフォルトhomeを設定しました。");
	    }else{*/
	    	FileConfiguration config2=new YamlConfiguration();
			try {
				config2.load(userdata);
			} catch (IOException | InvalidConfigurationException e1) {
				e1.printStackTrace();
			}
			Location loc=player.getLocation();
		if(args.length==0){
			config2.set("Location.X", loc.getX());
			config2.set("Location.Y", loc.getY());
			config2.set("Location.Z", loc.getZ());
			config2.set("Location.Yaw", loc.getYaw());
			config2.set("Location.Pitch", loc.getPitch());
			config2.set("Location.World", player.getWorld().getName());
			try {
				config2.save(userdata);
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.sendMessage(ChatColor.GREEN+"デフォルトhomeを設定しました。");
			return true;
		}else if(args.length==1){
			if(args[0].equalsIgnoreCase("list")){
				File folder2 = new File(MincraSetHome.plugin.getDataFolder(), "userdata");
			    if (!(folder2.exists())){
			    	folder2.mkdirs();
			    }
			    File userdata2 = new File(folder2,player.getUniqueId()+".yml");
				    if(!userdata2.exists()){
				    	try {
							userdata2.createNewFile();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				    }
			    FileConfiguration config3=new YamlConfiguration();
				try {
					config3.load(userdata2);
				} catch (IOException | InvalidConfigurationException e1) {
					e1.printStackTrace();
				}
				if(config3.contains("Config.homelist")){
					List<String> homelist = config3.getStringList("Config.homelist");
					player.sendMessage(ChatColor.GRAY+"----------------ホーム地点リスト----------------");
					for(String homename:homelist){
						player.sendMessage(ChatColor.GRAY+homename);
					}
					player.sendMessage(ChatColor.GRAY+"--------------------------------------------------");
				}else{
					player.sendMessage(ChatColor.GRAY+"----------------ホーム地点リスト----------------");
					player.sendMessage(ChatColor.GRAY+"--------------------------------------------------");
				}
				return true;
			}else{
				if(!(config2.contains("Config.quantity"))){
					config2.set("Config.quantity", 0);
				}
				if(!(config2.contains("Config.homelist"))){
					int quantity = config2.getInt("Config.quantity");
					String homename = args[0];
					quantity++;
					config2.set("Config.quantity", quantity);
					config2.set("Config.homelist", Arrays.asList(homename));
					config2.set(homename+".Location.X", loc.getX());
					config2.set(homename+".Location.Y", loc.getY());
					config2.set(homename+".Location.Z", loc.getZ());
					config2.set(homename+".Location.Yaw", loc.getYaw());
					config2.set(homename+".Location.Pitch", loc.getPitch());
					config2.set(homename+".Location.World", player.getWorld().getName());
					try {
						config2.save(userdata);
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.sendMessage(ChatColor.GREEN+homename+"homeを設定しました。");
					return true;
				}
				int quantity = config2.getInt("Config.quantity");
				if(config2.getStringList("Config.homelist").contains(args[0])){
					String homename = args[0];
					config2.set(homename+".Location.X", loc.getX());
					config2.set(homename+".Location.Y", loc.getY());
					config2.set(homename+".Location.Z", loc.getZ());
					config2.set(homename+".Location.Yaw", loc.getYaw());
					config2.set(homename+".Location.Pitch", loc.getPitch());
					config2.set(homename+".Location.World", player.getWorld().getName());
					try {
						config2.save(userdata);
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.sendMessage(ChatColor.GREEN+homename+"homeを上書き設定しました。");
				}else{
					Integer limitquantity = pexlist.get("default");
					if(player.hasPermission("mincra.home.10")){
						limitquantity = 10;
					}
					else if(player.hasPermission("mincra.home.20")){
						limitquantity = 20;
					}
					else if(player.hasPermission("mincra.home.30")){
						limitquantity = 30;
					}
					else if(player.hasPermission("mincra.home.40")){
						limitquantity = 40;
					}
					else if(player.hasPermission("mincra.home.50")){
						limitquantity = 50;
					}
//PermissionsExに依存しない形を選択					for(String pexname:pex.getPermissionsManager().getUser(player.getName()).getGroupNames()){
//						if(pexlist.containsKey(pexname)){
//							if(pexlist.get(pexname)>limitquantity){
//								limitquantity = pexlist.get(pexname);
//							}
//						}
//					}
					if(limitquantity > quantity){
						quantity++;
						String homename = args[0];
						config2.set("Config.quantity", quantity);
						List<String> list = config2.getStringList("Config.homelist");
						list.add(homename);
						config2.set("Config.homelist", list);
						config2.set(homename+".Location.X", loc.getX());
						config2.set(homename+".Location.Y", loc.getY());
						config2.set(homename+".Location.Z", loc.getZ());
						config2.set(homename+".Location.Yaw", loc.getYaw());
						config2.set(homename+".Location.Pitch", loc.getPitch());
						config2.set(homename+".Location.World", player.getWorld().getName());
						try {
							config2.save(userdata);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(ChatColor.GREEN+homename+"homeを設定しました。");
						return true;
					}else{
						player.sendMessage(ChatColor.GRAY+"homeをこれ以上増やすことはできません。");
						return true;
					}
				}
			}
		}
		return false;
	}
}
