package mincrasethome;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {
	private String st2,st1;
	private List<String> ExceptionWorld;
	private Boolean exception;
	private Boolean limit;
	private double x_s,y_s,z_s;
	private double x_e,y_e,z_e;
	public Home(){
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
		Player player = null;
		if(!(sender instanceof Player)){
			if(sender instanceof BlockCommandSender){
				if(args.length<1){
					return true;
				}
				if(Bukkit.getPlayer(args[0]) != null){
					player = Bukkit.getPlayer(args[0]);
				}
			}else{
				return true;
			}
		}else{
			player = (Player) sender;
		}

		if (!player.hasPermission("mincra.home")) {
            player.sendMessage("mincra.homeの権限を持っていない。");
            return true;
        }
		if(exception){
			if(ExceptionWorld.contains(player.getWorld().getName())){
				player.sendMessage(st2);
				return true;
			}
		}
		if(limit){
			Location l=player.getLocation();
			if(!(l.getX()>x_s&&l.getX()<x_e)){
				player.sendMessage(st1);
				return true;
			}
			if(!(l.getY()>y_s&&l.getY()<y_e)){
				player.sendMessage(st1);
				return true;
			}
			if(!(l.getZ()>z_s&&l.getZ()<z_e)){
				player.sendMessage(st1);
				return true;
			}
		}
	    File folder = new File(MincraSetHome.plugin.getDataFolder(), "userdata");
	    if (!(folder.exists())){
	    	folder.mkdirs();
	    }
	    File userdata = new File(folder,player.getUniqueId()+".yml");
	    if(!userdata.exists()){
	    	player.sendMessage(ChatColor.RED+"homeが見つかりませんでした。");
	    	return true;
	    }
	    FileConfiguration config2=new YamlConfiguration();
	    try {
			config2.load(userdata);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    if(args.length==0||sender instanceof BlockCommandSender){
			Double x=config2.getDouble("Location.X");
			Double y=config2.getDouble("Location.Y");
			Double z=config2.getDouble("Location.Z");
			Float yaw=(float) config2.getDouble("Location.Yaw");
			Float pitch=(float) config2.getDouble("Location.Pitch");
			String world=config2.getString("Location.World");
			if(exception){
				if(ExceptionWorld.contains(world)){
					player.sendMessage(st2);
					return true;
				}
			}
			if(x==null||y==null||z==null||yaw==null||pitch==null||world==null){
				player.sendMessage(ChatColor.GRAY+"homeのデータが壊れています。");
				return true;
			}
			if(Bukkit.getWorld(world)==null){
				player.sendMessage(ChatColor.GRAY+"homeのワールドを読み込めませんでした。");
				return true;
			}
			Location loc = new Location(Bukkit.getWorld(world),x,y,z,yaw,pitch);
			new PlayerTereport(player,loc).runTaskLater(MincraSetHome.plugin,1);
		    return true;
	    }else if(args.length==1){
	    	if(args[0].equalsIgnoreCase("list")){
	    		player.sendMessage(ChatColor.GRAY+"「list」という名前は登録できません");
	    		return true;
	    	}
	    	String homename = args[0];
	    	List<String> homelist = config2.getStringList("Config.homelist");
	    	if(homelist==null){
	    		player.sendMessage(ChatColor.GREEN+homename+"homeは存在しません。");
	    		return true;
			}
			if(!(homelist.contains(homename))){
				player.sendMessage(ChatColor.GRAY+homename+"homeは存在しません。");
				return true;
			}
			Double x=config2.getDouble(homename+".Location.X");
			Double y=config2.getDouble(homename+".Location.Y");
			Double z=config2.getDouble(homename+".Location.Z");
			Float yaw=(float) config2.getDouble(homename+".Location.Yaw");
			Float pitch=(float) config2.getDouble(homename+".Location.Pitch");
			String world=config2.getString(homename+".Location.World");
			if(x==null||y==null||z==null||yaw==null||pitch==null||world==null){
				player.sendMessage(ChatColor.GRAY+"homeのデータが壊れています。");
				return true;
			}
			if(exception){
				if(ExceptionWorld.contains(world)){
					player.sendMessage(st2);
					return true;
				}
			}
			if(Bukkit.getWorld(world)==null){
				player.sendMessage(ChatColor.GRAY+"homeのワールドを読み込めませんでした。");
				return true;
			}
			Location loc = new Location(Bukkit.getWorld(world),x,y,z,yaw,pitch);
			new PlayerTereport(player,loc).runTaskLater(MincraSetHome.plugin,1);
	    	return true;
	    }
	    return false;
	}
}
