package mincrasethome;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerTereport extends BukkitRunnable{
    Player player;
    Location loc;
	public PlayerTereport(Player _player, Location _loc) {
		player=_player;
		loc=_loc;
	}

	@Override
	public void run() {
		int count = 0;
			while(!(loc.getBlock().getType().equals(Material.AIR)&&loc.add(0, 1, 0).getBlock().getType().equals(Material.AIR))){
				loc.add(0, 1, 0);
				count++;
				if(count>=100){
					player.sendMessage(ChatColor.GRAY+"home地点が埋まっているためにテレポートを開始できませんでした。");
					break;
				}
			}
			player.teleport(loc);
			Vector v=new Vector(0,0.1D,0);
			player.setVelocity(v);
			player.setNoDamageTicks(100);
		}
	}

