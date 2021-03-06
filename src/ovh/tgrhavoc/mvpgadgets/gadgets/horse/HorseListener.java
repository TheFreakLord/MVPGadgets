package ovh.tgrhavoc.mvpgadgets.gadgets.horse;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import ovh.tgrhavoc.mvpgadgets.MVPGadgets;

public class HorseListener implements Listener {
	
	MVPGadgets plugin;
	
	public HorseListener(MVPGadgets g){
		plugin = g;
	}
	
	@EventHandler
	public void playerInteractWithEntity(PlayerInteractEntityEvent event){
		if (event.getRightClicked() instanceof Horse){
			Horse h = (Horse)event.getRightClicked();
			if (h.isCustomNameVisible() && h.getCustomName().contains(player.getName())){
				h.setPassenger(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void dismound(VehicleExitEvent e) {
		if (!e.getVehicle().isOnGround()){
			e.setCancelled(true);
			return;
		}
		if (e.getExited() instanceof Player && e.getVehicle() instanceof Horse) {
			Player player = (Player) e.getExited();
			Horse h = (Horse) e.getVehicle();
			if (h.isCustomNameVisible()
					&& h.getCustomName().equals(player.getName() + "'s horse")) {
				createExplosion(player.getLocation());
				h.remove();
			}
		}
	}
	
	private void createExplosion(Location loc){
		List<Item> itemsSpanwed = new ArrayList<Item>();
		for (int i=0; i<=50; i++){
			float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
			double y = 0.5;
			float z = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
			Item bone = loc.getWorld().dropItem(loc, new ItemStack(Material.BONE, 64));
			bone.setPickupDelay(Integer.MAX_VALUE);
			bone.setVelocity(new Vector(x, y, z));
			itemsSpanwed.add(bone);
		}
		final List<Item> finalItems = itemsSpanwed;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				for (Item i: finalItems){
					i.remove();
				}
			}
		}, (20L * 2));
	}

}
