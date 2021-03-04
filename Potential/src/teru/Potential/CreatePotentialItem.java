package teru.Potential;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import nashi.NRPG.Items.CreateItem;

public class CreatePotentialItem implements Listener {
	
	public static boolean support;

	public static class MaterialRange {
		private int min;
		private int max;
		private double up;
		private double down;

		public MaterialRange(int min, int max, double up, double down) {
			this.min = min;
			this.max = max;
			this.up = up;
			this.down = down;
		}

		public int getMin() {
			return this.min;
		}

		public int getMax() {
			return this.max;
		}

		public double getUp() {
			return this.up;
		}

		public double getDown() {
			return this.down;
		}
	}

	public static class ItemData {
		private double number;
		private String potential;

		public ItemData(String potential, int number) {
			this.potential = potential;
			this.number = number / 100.0D;
		}

		public double getNumber() {
			return this.number;
		}

		public String getPotential() {
			return this.potential;
		}
	}

	public static NamespacedKey p_available = new NamespacedKey(Main.getPlugin(), "Potential_available");
	public static NamespacedKey p_material = new NamespacedKey(Main.getPlugin(), "Potential_material");
	public static NamespacedKey p_materialRank = new NamespacedKey(Main.getPlugin(), "Potential_materialrank");
	public static NamespacedKey p_rankdown = new NamespacedKey(Main.getPlugin(), "Potential_rankdown");
	public static NamespacedKey p_result = new NamespacedKey(Main.getPlugin(), "Potential_result");

	public static NamespacedKey p_rank = new NamespacedKey(Main.getPlugin(), "Potential_rank");
	public static NamespacedKey p_equipSlot = new NamespacedKey(Main.getPlugin(), "Potential_equipslot");

	public static NamespacedKey p_attrUUID1 = new NamespacedKey(Main.getPlugin(), "Potential_attrUUID1");
	public static NamespacedKey p_attrUUID2 = new NamespacedKey(Main.getPlugin(), "Potential_attrUUID2");
	public static NamespacedKey p_attrUUID3 = new NamespacedKey(Main.getPlugin(), "Potential_attrUUID3");
	
	public static NamespacedKey p_LockItem = new NamespacedKey(Main.getPlugin(), "Potential_LockItem");
	public static NamespacedKey p_lockResult = new NamespacedKey(Main.getPlugin(), "Potential_LockResult");
	public static NamespacedKey p_lockline = new NamespacedKey(Main.getPlugin(), "Potential_LockLine");

	private static ItemStack resultItem;

	public static HashMap<String, String> totalPotentialList = new HashMap<String, String>();
	public static HashMap<String, MaterialRange> materialRange = new HashMap<String, MaterialRange>();
	
	public static String GUIname = setStringColor(Main.pMessages.getString("Messages.GUITitle"));
	public static String LorePrefix = setStringColor(Main.pMessages.getString("Messages.Lores.NRPG_LorePrefix"));
	public static String LorePrefix2 = setStringColor(Main.pMessages.getString("Messages.Lores.Potential_LorePrefix"));
	public static String LoreLine = setStringColor(Main.pMessages.getString("Messages.Lores.LoreLine"));
	public static String rankPrefix = setStringColor(Main.pMessages.getString("Messages.Lores.ItemRankLorePrefix"));
	public static String rankCommon = setStringColor(Main.pMessages.getString("Messages.ItemRankLore.Common"));
	public static String rankUncommon = setStringColor(Main.pMessages.getString("Messages.ItemRankLore.Uncommon"));
	public static String rankRare = setStringColor(Main.pMessages.getString("Messages.ItemRankLore.Rare"));
	public static String rankLegendary = setStringColor(Main.pMessages.getString("Messages.ItemRankLore.Legendary"));
	public static String resultItemname = setStringColor(Main.pMessages.getString("Messages.Lores.ResultItem"));
	
	public static void init() {
		YamlConfiguration pTable = YamlConfiguration.loadConfiguration(new File(Main.getPlugin().getDataFolder() + File.separator + "potentials.yml"));
        
		Set<String> potentials = pTable.getConfigurationSection("PotentialList.potentials").getKeys(false);
		for (String s : potentials) {
			totalPotentialList.put(s, pTable.getString("PotentialList.potentials." + s));
		}

		Set<String> range = pTable.getConfigurationSection("PotentialList.range").getKeys(false);
		for (String s : range) {
			materialRange.put(s,
					new MaterialRange(pTable.getInt("PotentialList.range." + s + ".min"),
							pTable.getInt("PotentialList.range." + s + ".max"),
							pTable.getDouble("PotentialList.range." + s + ".up"),
							pTable.getDouble("PotentialList.range." + s + ".down")));
		}

		support = pTable.getBoolean("nrpg_support");
		
		ItemStack is = getCustomTextureHead(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFmZTc0ZjY0YWJkMWE4MTQ3YzEzNzBmY2YzMTIyOTI4MDFiZjRjMzcxZjE1NjRkODkwNGRjMWI3YmU2NGRjOCJ9fX0=");
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(resultItemname);
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_result, PersistentDataType.BYTE, Byte.valueOf((byte) 1));
		is.setItemMeta(im);
		resultItem = is;
	}
	
	@EventHandler
	public void openGUI(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!p.isSneaking() || e.getAction() != Action.RIGHT_CLICK_BLOCK
				|| e.getClickedBlock().getType() != Material.SMITHING_TABLE) {
			return;
		} else {
			e.setCancelled(true);
			Inventory inv = Bukkit.createInventory((InventoryHolder) p, InventoryType.FURNACE,
					GUIname);
			p.openInventory(inv);
		}
	}

	@EventHandler
	public void closeGUI(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		if (!e.getView().getTitle().equals(GUIname)) {
			return;
		} else {
			returnItem(p, inv.getItem(0));
			returnItem(p, inv.getItem(1));
			inv.setItem(0, null);
			inv.setItem(1, null);
			inv.setItem(2, null);
		}
	}

	@EventHandler
	public void playerOffline(PlayerQuitEvent e) {
		Player p = (Player) e.getPlayer();
		p.closeInventory();
	}

	@EventHandler
	public void useGUI(InventoryClickEvent e) {
		if (!e.getView().getTitle().equals(GUIname)) {
			return;
		}

		e.setCancelled(true);
		Inventory inv = e.getView().getTopInventory();

		if (e.getCurrentItem() == null) {
			return;
		}

		ItemStack currentItem = e.getCurrentItem().clone();

		if (!currentItem.hasItemMeta()) {
			return;
		}

		Player p = (Player) e.getWhoClicked();
		ItemStack equipment = inv.getItem(0);
		ItemStack potential_material = inv.getItem(1);
		if (!currentItem.getItemMeta().getPersistentDataContainer().has(p_result, PersistentDataType.BYTE)) {
			boolean flag3 = checkMaterialPotentialKey(currentItem);
			if (!flag3) {
				PersistentDataContainer pdcItem = currentItem.getItemMeta().getPersistentDataContainer();
				if(support && !pdcItem.has(p_equipSlot, PersistentDataType.INTEGER)) {
					int i = CreateItem.getItemForgeSlot(currentItem);
					if(i != -1) {
						CreatePotentialItem.setAvailablePotentialKey(currentItem, (byte)1);
						CreatePotentialItem.setSlotPotentialKey(currentItem, i);
						CreatePotentialItem.setRankPotentialKey(currentItem, (byte)1);
					}
				}
			}
		}
		
		if (currentItem.getItemMeta().getPersistentDataContainer().has(p_result, PersistentDataType.BYTE)) {
			
			ItemStack is = inv.getItem(0);
			ItemMeta im = is.getItemMeta();

			ItemStack is2 = inv.getItem(1);
			boolean flag = true;
			boolean lockFlag = false;

			if (checkRankPotentialKey(is) - checkMaterialRankPotentialKey(is2) > 1) {
				p.sendMessage(Commands.MessagePrefix + Commands.PotentialItemCantUseThisMaterial);
				p.getOpenInventory().close();
				return;
			}

			List<String> oldlore = im.getLore();
			List<String> potentialLore = new ArrayList<String>();
			List<String> totalLore = new ArrayList<String>();
			List<ItemData> potentialList = new ArrayList<ItemData>();

			MaterialRange chance;
			switch (checkRankPotentialKey(is)) {
			case 1:
				chance = materialRange.get("common");
				break;
			case 2:
				chance = materialRange.get("uncommon");
				break;
			case 3:
				chance = materialRange.get("rare");
				break;
			case 4:
				chance = materialRange.get("legendary");
				break;
			default:
				chance = materialRange.get("common");
				break;
			}
			
			if(im.getPersistentDataContainer().has(p_lockline, PersistentDataType.INTEGER)) {
				flag = false;
				lockFlag = true;
			}
			
			System.out.println(lockFlag);
			
			if(!lockFlag) {
				if (checkMaterialRankdownPotentialKey(is2)) {
					if (checkRank(chance.getDown())) {
						if (checkRankPotentialKey(is) > 1) {
							im.getPersistentDataContainer().set(p_rank, PersistentDataType.BYTE,
									(byte) (checkRankPotentialKey(is) - 1));
							is.setItemMeta(im);
							flag = false;
						}
					}
				}
			}
			
			if (checkRankPotentialKey(is) - checkMaterialRankPotentialKey(is2) == 1) {
				flag = false;
			}

			if (flag) {
				if (checkRank(chance.getUp())) {
					if (checkRankPotentialKey(is) < 4) {
						im.getPersistentDataContainer().set(p_rank, PersistentDataType.BYTE,
								(byte) (checkRankPotentialKey(is) + 1));
						is.setItemMeta(im);
					}
				}
			}

			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				String potential = totalPotentialList.keySet().toArray()[r.nextInt(totalPotentialList.keySet().size())]
						.toString();
				int k = 0;
				MaterialRange mr;
				switch (checkRankPotentialKey(is)) {
				case 1:
					mr = materialRange.get("common");
					break;
				case 2:
					mr = materialRange.get("uncommon");
					break;
				case 3:
					mr = materialRange.get("rare");
					break;
				case 4:
					mr = materialRange.get("legendary");
					break;
				default:
					mr = materialRange.get("common");
					break;
				}
				k = r.nextInt(mr.max - mr.min + 1) + mr.min;
				String s = totalPotentialList.get(potential).replaceAll("#", String.valueOf(k));
				potentialList.add(new ItemData(potential, k));
				potentialLore.add(LorePrefix2 + s);
			}

			ConstructLore(inv.getItem(0), oldlore, potentialLore);
			AttachPotential.Attach(inv.getItem(0), potentialList);

			int count = potential_material.getAmount();
			if (count > 1) {
				potential_material.setAmount(count - 1);
				inv.setItem(1, potential_material);
				totalLore.removeAll(totalLore);
			} else {
				inv.setItem(1, null);
			}

			potentialList.removeAll(potentialList);
		}
		int clickSlot = e.getSlot();
		boolean flag = checkAvailablePotentialKey(currentItem);
		if (flag) {
			boolean flag2 = inv.getItem(0) == null ? false : true;
			if (flag2) {
				return;
			}
			ItemStack istack = currentItem.clone();
			istack.setAmount(1);
			inv.setItem(0, istack);
			int amount = istack.getAmount();
			if (amount > 1) {
				currentItem.setAmount(amount - 1);
				p.getInventory().setItem(clickSlot, currentItem);
			} else {
				currentItem.setAmount(amount - 1);
				p.getInventory().setItem(clickSlot, null);
			}
		} else {
			boolean flag3 = checkMaterialPotentialKey(inv.getItem(1));
			if (!flag3) {
				boolean flag4 = checkMaterialPotentialKey(currentItem);
				if (flag4) {
					inv.setItem(1, currentItem);
					p.getInventory().setItem(clickSlot, null);
				}
			} else {
				return;
			}
		}
		equipment = inv.getItem(0);
		potential_material = inv.getItem(1);
		if (equipment != null && potential_material != null) {
			inv.setItem(2, resultItem.clone());
		} else {
			inv.setItem(2, null);
		}
	}

	public static void returnItem(Player p, ItemStack is) {
		if (is == null) {
			return;
		}
		if (p.getInventory().firstEmpty() != -1) {
			p.getInventory().addItem(is);
		} else {
			p.getWorld().dropItem(p.getLocation(), is);
		}
	}

	public static void setMaterialPotentialKey(ItemStack is, byte flag) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_material, PersistentDataType.BYTE, Byte.valueOf(flag));
		is.setItemMeta(im);
	}

	public static boolean checkMaterialPotentialKey(ItemStack is) {
		if (is == null) {
			return false;
		}
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(p_material, PersistentDataType.BYTE)) {
			return true;
		} else {
			return false;
		}
	}

	public static void setAvailablePotentialKey(ItemStack is, byte flag) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_available, PersistentDataType.BYTE, Byte.valueOf(flag));
		is.setItemMeta(im);
	}

	public boolean checkAvailablePotentialKey(ItemStack is) {
		if (is == null) {
			return false;
		}
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(p_available, PersistentDataType.BYTE)) {
			return true;
		} else {
			return false;
		}
	}

	public static void setRankPotentialKey(ItemStack is, byte rank) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_rank, PersistentDataType.BYTE, Byte.valueOf(rank));
		is.setItemMeta(im);
	}

	public static byte checkRankPotentialKey(ItemStack is) {
		if (is == null) {
			return 0;
		}
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(p_rank, PersistentDataType.BYTE)) {
			return pdc.get(p_rank, PersistentDataType.BYTE);
		}
		return 0;
	}

	public static void setMaterialRankPotentialKey(ItemStack is, byte rank) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_materialRank, PersistentDataType.BYTE, Byte.valueOf(rank));
		is.setItemMeta(im);
	}

	public static byte checkMaterialRankPotentialKey(ItemStack is) {
		if (is == null) {
			return 0;
		}
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.has(p_materialRank, PersistentDataType.BYTE)) {
			return pdc.get(p_materialRank, PersistentDataType.BYTE);
		}
		return 0;
	}

	public static void setMaterialRankdownPotentialKey(ItemStack is, byte rank) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_rankdown, PersistentDataType.BYTE, Byte.valueOf(rank));
		is.setItemMeta(im);
	}

	public static boolean checkMaterialRankdownPotentialKey(ItemStack is) {
		if (is == null) {
			return false;
		}
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		if (pdc.get(p_rankdown, PersistentDataType.BYTE) == 1) {
			return true;
		}
		return false;
	}

	public static ItemStack getCustomTextureHead(String skinValue) {
		ItemStack is = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm = (SkullMeta) is.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", skinValue));
		Field profileField = null;
		try {
			profileField = sm.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(sm, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		is.setItemMeta((ItemMeta) sm);
		return is;
	}

	public static boolean checkRank(double chance) {
		Random r = new Random();
		double judge = r.nextDouble();
		return chance >= judge;
	}

	public static void setSlotPotentialKey(ItemStack is, int type) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_equipSlot, PersistentDataType.INTEGER, type);
		is.setItemMeta(im);
	}

	public static int checkSlotPotentialKey(ItemStack isSlot) {
		if (isSlot == null) {
			return -1;
		}
		ItemMeta imSlot = isSlot.getItemMeta();
		PersistentDataContainer pdcSlot = imSlot.getPersistentDataContainer();
		if (pdcSlot.has(p_equipSlot, PersistentDataType.INTEGER)) {
			int i = pdcSlot.get(p_equipSlot, PersistentDataType.INTEGER);
			return i;
		}
		else {
			return -1;
		}
	}
	
	public static boolean checkPotentialsKey(ItemStack is) {
		if(is == null) {
			return false;
		}
		PersistentDataContainer pdc = is.getItemMeta().getPersistentDataContainer();
		if(pdc.has(p_attrUUID1, PersistentDataType.STRING)) {
			return true;
		}
		return false;
	}
	
	public static void setLockKey(ItemStack is, int slot) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(p_LockItem, PersistentDataType.INTEGER, slot);
		is.setItemMeta(im);
	}
	
	public static boolean checkLockKey(ItemStack is) {
		if(is == null) {
			return false;
		}
		PersistentDataContainer pdc = is.getItemMeta().getPersistentDataContainer();
		if(pdc.has(p_LockItem, PersistentDataType.INTEGER)) {
			return true;
		}
		return false;
	}

	public static void ConstructLore(ItemStack is, List<String> oldlore, List<String> potentialLore) {
		List<String> totalLore = new ArrayList<String>();
		ItemMeta im = is.getItemMeta();
		if (im != null) {
			im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
		}
		boolean flag1 = false, flag2 = false;

		oldlore = im.getLore();
		if (oldlore == null) {
			oldlore = new ArrayList<String>();
		}
		for (String s : oldlore) {
			if (s.startsWith(LorePrefix)) {
				totalLore.add(s);
				flag1 = true;
			}
		}
		if (flag1) {
			totalLore.add(LoreLine);
		}
		switch (im.getPersistentDataContainer().get(p_rank, PersistentDataType.BYTE)) {
			case 1:
				totalLore.add(LorePrefix2 + rankPrefix + rankCommon);
				break;
			case 2:
				totalLore.add(LorePrefix2 + rankPrefix + rankUncommon);
				break;
			case 3:
				totalLore.add(LorePrefix2 + rankPrefix + rankRare);
				break;
			case 4:
				totalLore.add(LorePrefix2 + rankPrefix + rankLegendary);
				break;
		}
		if(im.getPersistentDataContainer().has(p_lockline, PersistentDataType.INTEGER)) {
			potentialLore.remove(2);
			switch(im.getPersistentDataContainer().get(p_lockline, PersistentDataType.INTEGER)) {
				case 1:
					for(String s5 : oldlore) {
						if(s5.contains(LockPotential.Locked)) {
							String temp = s5.replace(LockPotential.Locked, "");
							totalLore.add(temp);
						}
					}
					totalLore.add(potentialLore.get(0));
					totalLore.add(potentialLore.get(1));
					break;
				case 2:
					totalLore.add(potentialLore.get(0));
					for(String s5 : oldlore) {
						if(s5.contains(LockPotential.Locked)) {
							String temp = s5.replace(LockPotential.Locked, "");
							totalLore.add(temp);
						}
					}
					totalLore.add(potentialLore.get(1));
					break;
				case 3:
					totalLore.add(potentialLore.get(0));
					totalLore.add(potentialLore.get(1));
					for(String s5 : oldlore) {
						if(s5.contains(LockPotential.Locked)) {
							String temp = s5.replace(LockPotential.Locked, "");
							totalLore.add(temp);
						}
					}
					break;
			}
		}
		else {
			for(String s2 : potentialLore) {
				totalLore.add(s2);
			}
		}
		for (String s3 : oldlore) {
			if (!s3.startsWith(LorePrefix) && !s3.startsWith(LorePrefix2) && !s3.equals(LoreLine)) {
				flag2 = true;
			}
		}
		if (flag2) {
			totalLore.add(LoreLine);
		}
		for (String s4 : oldlore) {
			if (!s4.startsWith(LorePrefix) && !s4.startsWith(LorePrefix2) && !s4.equals(LoreLine)) {
				if (s4.startsWith(LorePrefix2 + rankPrefix)) {
					continue;
				}
				totalLore.add(s4);
			}
		}

		im.setLore(totalLore);
		is.setItemMeta(im);
	}
	
	public static String setStringColor(String s) {
		if(s == null)
			return "";
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
