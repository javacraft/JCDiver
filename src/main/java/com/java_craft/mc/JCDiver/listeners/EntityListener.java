package com.java_craft.mc.JCDiver.listeners;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.java_craft.mc.JCDiver.JCDiver;


public class EntityListener implements Listener {
	private JCDiver	plugin;


	public EntityListener( JCDiver plugin ) {
		this.plugin = plugin;
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage( EntityDamageEvent event ) {
		if ( event.getCause() == DamageCause.DROWNING ) {
			Entity entity = event.getEntity();

			if ( entity != null && entity instanceof Player ) {
				Player player = (Player) entity;

				// If player does not have basic permission, return
				if ( !player.hasPermission( JCDiver.PERM_ENABLED ) )
					return;

				PlayerInventory inventory = player.getInventory();
				if ( inventory == null ) {
					plugin.log( Level.SEVERE, "Unable to access player inventory!" );
					return;
				}

				ItemStack armor;
				armor = inventory.getHelmet();
				boolean hasGoldHelmet = (armor != null && armor.getType() == Material.GOLD_HELMET);
				armor = inventory.getChestplate();
				boolean hasGoldChestplate = (armor != null && armor.getType() == Material.GOLD_CHESTPLATE);
				armor = inventory.getLeggings();
				boolean hasGoldLeggings = (armor != null && armor.getType() == Material.GOLD_LEGGINGS);
				armor = inventory.getBoots();
				boolean hasGoldBoots = (armor != null && armor.getType() == Material.GOLD_BOOTS);

				// If player has full armor and permission to use, cancel damage. Otherwise tick damage
				// with helmet only, if equipped.
				if ( player.hasPermission( JCDiver.PERM_UNLIMITED ) && hasGoldHelmet && hasGoldChestplate
						&& hasGoldLeggings && hasGoldBoots ) {
					event.setCancelled( true );

				}
				else if ( hasGoldHelmet ) {
					event.setCancelled( true );

					int sugarCaneAmount = 0;

					if ( inventory.contains( Material.SUGAR_CANE ) ) {
						inventory.removeItem( new ItemStack( Material.SUGAR_CANE, 1 ) );
						for ( Integer itemStacks : inventory.all( Material.SUGAR_CANE ).keySet() ) {
							sugarCaneAmount += inventory.all( Material.SUGAR_CANE ).get( itemStacks ).getAmount();
						}

						switch ( sugarCaneAmount ) {
							case 60:
								player.sendMessage( ChatColor.GREEN + "You still have sugar canes for 60 more ticks ..." );
								break;
							case 30:
								player.sendMessage( ChatColor.YELLOW + "You only have sugar canes for 30 more ticks ..." );
								break;
							case 10:
								player.sendMessage( ChatColor.GOLD + "You only have sugar canes for 10 more ticks ..." );
								break;
							case 0:
								player.sendMessage( ChatColor.RED + "You have no sugar canes left." );
								player.sendMessage( ChatColor.RED + "Water will now slowly destroy Helmet ..." );
								break;
						}
					}
					else {

						short durability = player.getInventory().getHelmet().getDurability();

						if ( durability == 1 ) {
							player.sendMessage( ChatColor.RED + "You have no sugar canes." );
							player.sendMessage( ChatColor.RED + "Water will slowly destroy helmet ..." );
						}
						else if ( durability == 23 ) {
							player.sendMessage( ChatColor.RED + "You have no sugar canes." );
							player.sendMessage( ChatColor.RED + "Water destroyed helmet to 1 / 3 durability ..." );
						}
						else if ( durability == 46 ) {
							player.sendMessage( ChatColor.RED + "You have no sugar canes." );
							player.sendMessage( ChatColor.RED + "Water destroyed helmet to 2 / 3 durability ..." );
						}
						else if ( durability == 68 ) {
							player.sendMessage( ChatColor.RED + "You have no sugar canes." );
							player.sendMessage( ChatColor.RED + "Water has destroyed the helmet." );
							player.sendMessage( ChatColor.RED + "YOU ARE NOW GOING TO DROWN !!!!" );
						}

						Integer tmpInt = durability + JCDiver.DAMAGE;
						short newDamage = tmpInt.shortValue();
						player.getInventory().getHelmet().setDurability( newDamage );
						if ( newDamage >= 69 ) {
							player.getInventory().clear( 39 );
						}
					}
				}
			}
		}
	}

}
