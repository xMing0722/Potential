package teru.Potential;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LockPotential implements Listener{
	
	private static ItemStack LockResultItem1;
	private static ItemStack LockResultItem2;
	private static ItemStack LockResultItem3;
	
	public static String LockResultItemName1 = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.Lores.LockResultItem1"));
	public static String LockResultItemName2 = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.Lores.LockResultItem2"));
	public static String LockResultItemName3 = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.Lores.LockResultItem3"));
	public static String NotHasPotentials = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.LockGUITitle"));
	public static String LockGUITitle = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.LockGUITitle"));
	public static String Locked = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.Locked"));
	public static String Unlocked = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.Unlocked"));
	
	public static void init() {
		ItemStack is = CreatePotentialItem.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliMzAzMDNmOTRlN2M3ODVhMzFlNTcyN2E5MzgxNTM1ZGFmNDc1MzQ0OWVhNDFkYjc0NmUxMjM0ZTlkZDJiNSJ9fX0=");
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdcResult = im.getPersistentDataContainer();
		im.setDisplayName(LockResultItemName1);
		pdcResult.set(CreatePotentialItem.p_lockResult, PersistentDataType.INTEGER, 1);
		is.setItemMeta(im);
		LockResultItem1 = is;
		
		is = CreatePotentialItem.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNjYTdkN2MxNTM0ZGM2YjllZDE2NDdmOTAyNWRkZjI0NGUwMTA3ZGM4ZGQ0ZjRmMDg1MmM4MjA4MWQ2MzUwZSJ9fX0=");
		im = is.getItemMeta();
		pdcResult = im.getPersistentDataContainer();
		im.setDisplayName(LockResultItemName2);
		pdcResult.set(CreatePotentialItem.p_lockResult, PersistentDataType.INTEGER, 2);
		is.setItemMeta(im);
		LockResultItem2 = is;
		
		is = CreatePotentialItem.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1ZTFlMmZiMmRlN2U2Mjk5YTBmNjFkZGY3ZDlhNmQxMDFmOGQ2NjRmMTk1OWQzYjY3ZGNlOGIwNDlhOGFlMSJ9fX0=");
		im = is.getItemMeta();
		pdcResult = im.getPersistentDataContainer();
		im.setDisplayName(LockResultItemName3);
		pdcResult.set(CreatePotentialItem.p_lockResult, PersistentDataType.INTEGER, 3);
		is.setItemMeta(im);
		LockResultItem3 = is;
		
	}
	
	@EventHandler
	public void openLockGUI(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!p.isSneaking() || e.getAction() != Action.LEFT_CLICK_BLOCK || e.getClickedBlock().getType() != Material.SMITHING_TABLE) {
			return;
		}
		else {
			e.setCancelled(true);
			Inventory inv = Bukkit.createInventory((InventoryHolder)p, InventoryType.BREWING, LockGUITitle);
			p.openInventory(inv);
		}
	}
	
	@EventHandler
	public void closeLockGUI(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		if(!e.getView().getTitle().equals(LockGUITitle)) {
			return;
		}
		else {
			returnLockItem(p, inv.getItem(3));
			returnLockItem(p, inv.getItem(4));
			inv.setItem(0, null);
			inv.setItem(1, null);
			inv.setItem(2, null);
			inv.setItem(3, null);
			inv.setItem(4, null);
		}
	}
	
	@EventHandler
	public void useLockGUI(InventoryClickEvent e) {
		if (!e.getView().getTitle().equals(LockGUITitle)) {
			return;
		}
		
		e.setCancelled(true);
		Inventory inv = e.getView().getTopInventory();
		
		if(e.getCurrentItem() == null) {
			return;
		}
		
		ItemStack currentItem = e.getCurrentItem().clone();
		
		if(!currentItem.hasItemMeta()) {
			return;
		}
		
		Player p = (Player)e.getWhoClicked();
		
		ItemStack equipment = inv.getItem(3);
		ItemStack Lock = inv.getItem(4);
		
		if(currentItem.getItemMeta().getPersistentDataContainer().has(CreatePotentialItem.p_lockResult, PersistentDataType.INTEGER)) {
			
			equipment = inv.getItem(3);
			Lock = inv.getItem(4);
			ItemMeta im = equipment.getItemMeta();
			PersistentDataContainer pdcEquip = im.getPersistentDataContainer();
			
			if(pdcEquip.has(CreatePotentialItem.p_lockline, PersistentDataType.INTEGER)) {
				p.getOpenInventory().close();
				p.sendMessage(Commands.MessagePrefix + Commands.AlreadyLocked);
				return;
			}
			
			boolean hasPotentials = CreatePotentialItem.checkPotentialsKey(equipment);
			if(hasPotentials) {
				switch(currentItem.getItemMeta().getPersistentDataContainer().get(CreatePotentialItem.p_lockResult, PersistentDataType.INTEGER)) {
					case 1:
						pdcEquip.set(CreatePotentialItem.p_lockline, PersistentDataType.INTEGER, 1);
						equipment.setItemMeta(im);
						p.sendMessage(Commands.MessagePrefix + Commands.setItem + LockResultItemName1);
						break;
					case 2:
						pdcEquip.set(CreatePotentialItem.p_lockline, PersistentDataType.INTEGER, 2);
						equipment.setItemMeta(im);
						p.sendMessage(Commands.MessagePrefix + Commands.setItem + LockResultItemName2);
						break;
					case 3:
						pdcEquip.set(CreatePotentialItem.p_lockline, PersistentDataType.INTEGER, 3);
						equipment.setItemMeta(im);
						p.sendMessage(Commands.MessagePrefix + Commands.setItem + LockResultItemName3);
						break;
					default:
						p.getOpenInventory().close();
						break;
				}
				
				List<String> oldlores = im.getLore();
				List<String> newlores = new ArrayList<String>();
				int count = 0;
				for(String s : oldlores) {
					if(s.startsWith(CreatePotentialItem.LorePrefix)) {
						newlores.add(s);
					}
					else if(s.startsWith(CreatePotentialItem.LoreLine)) {
						newlores.add(s);
					}
					else if(s.startsWith(CreatePotentialItem.LorePrefix2)) {
						if(pdcEquip.get(CreatePotentialItem.p_lockline, PersistentDataType.INTEGER) == count) {
							s = s + Locked;
						}
						newlores.add(s);
						count++;
					}
					else if(!s.startsWith(CreatePotentialItem.LorePrefix) && !s.startsWith(CreatePotentialItem.LoreLine) && !s.startsWith(CreatePotentialItem.LorePrefix2)) {
						newlores.add(s);
					}
				}
				im.setLore(newlores);
				equipment.setItemMeta(im);
				
			}
			else {
				p.sendMessage(Commands.MessagePrefix + NotHasPotentials);
				p.getOpenInventory().close();
				return;
			}
			inv.setItem(4, null);
			p.getOpenInventory().close();
			return;
		}
		equipment = inv.getItem(3);
		Lock = inv.getItem(4);
		int clickSlot = e.getSlot();
		boolean flag = CreatePotentialItem.checkPotentialsKey(currentItem);
		if(flag) {
			boolean flag2 = inv.getItem(3) == null ? false : true;
			if(flag2) {
				return;
			}
			ItemStack is = currentItem.clone();
			is.setAmount(1);
			inv.setItem(3, is);
			int amount = is.getAmount();
			if(amount > 1) {
				currentItem.setAmount(amount - 1);
				p.getInventory().setItem(clickSlot, currentItem);
			}
			else {
				currentItem.setAmount(amount - 1);
				p.getInventory().setItem(clickSlot, null);
			}
		}
		else {
			boolean flag3 = CreatePotentialItem.checkLockKey(inv.getItem(4));
			if(!flag3) {
				boolean flag4 = CreatePotentialItem.checkLockKey(currentItem);
				if(flag4) {
					ItemStack is2 = currentItem.clone();
					is2.setAmount(1);
					inv.setItem(4, is2);
					int amount2 = currentItem.getAmount();
					if(amount2 > 1) {
						currentItem.setAmount(amount2 - 1);
						p.getInventory().setItem(clickSlot, currentItem);
					}
					else {
						currentItem.setAmount(amount2 - 1);
						p.getInventory().setItem(clickSlot, null);
					}
				}
			}
			else {
				return;
			}
		}
		equipment = inv.getItem(3);
		Lock = inv.getItem(4);
		
		if(equipment != null && Lock != null) {
			inv.setItem(0, LockResultItem1);
			inv.setItem(1, LockResultItem2);
			inv.setItem(2, LockResultItem3);
		}
		else {
			inv.setItem(0, null);
			inv.setItem(1, null);
			inv.setItem(2, null);
		}
		
	}
	
	public static void returnLockItem(Player p, ItemStack is) {
		if (is == null) {
			return;
		}
		if (p.getInventory().firstEmpty() != -1) {
			p.getInventory().addItem(is);
		} else {
			p.getWorld().dropItem(p.getLocation(), is);
		}
	}
}
