package mincrasethome;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MincraCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length<1){
			sender.sendMessage("パラメタが足りません");
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("mincrasethome")){
			switch(args[0]){
				case"reload":
					if (!sender.hasPermission("mincra.sethome.admin")) {
		                sender.sendMessage("mincra.sethome.adminの権限がありません");
		                return true;
		            }
					Bukkit.getServer().getPluginManager().disablePlugin(MincraSetHome.plugin);
					Bukkit.getServer().getPluginManager().enablePlugin(MincraSetHome.plugin);
					return true;
				case"remove":
					if(remove(sender,args)) return true;
				case"list":
					if(!(sender instanceof Player)){
						return false;
					}
					Player player=(Player)sender;
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
				    FileConfiguration config2=new YamlConfiguration();
					try {
						config2.load(userdata);
					} catch (IOException | InvalidConfigurationException e1) {
						e1.printStackTrace();
					}
					if(config2.contains("Config.homelist")){
						List<String> homelist = config2.getStringList("Config.homelist");
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
				default:
					return false;
			}
		}
		return false;
	}

	private boolean remove(CommandSender sender, String[] args) {
		if(!(args.length==2)){
			return false;
		}
		if(!(sender instanceof Player)){
			return false;
		}
		Player player=(Player)sender;
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
	    FileConfiguration config2 = new YamlConfiguration();
		try {
			config2.load(userdata);
		} catch (IOException | InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
		if(!config2.contains("Config.quantity")){
			player.sendMessage(ChatColor.GRAY+"ホームが見つかりませんでした。");
			return true;
		}
		if(!config2.contains("Config.homelist")){
			player.sendMessage(ChatColor.GRAY+"ホームが見つかりませんでした。");
			return true;
		}
		if(!config2.getStringList("Config.homelist").contains(args[1])){
			player.sendMessage(ChatColor.GRAY+"ホームが見つかりませんでした。");
			return true;
		}
		String homename = args[1];
		List<String> list = config2.getStringList("Config.homelist");
		list.remove(homename);
		config2.set("Config.homelist", list);
		int quantity = config2.getInt("Config.quantity")-1;
		config2.set("Config.quantity", quantity);
		config2.set(homename, null);
		player.sendMessage(ChatColor.GRAY+homename+"homeを削除しました。");
		try {
			config2.save(userdata);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
