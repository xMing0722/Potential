package teru.Potential;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import teru.Potential.CreatePotentialItem;

public class Commands implements CommandExecutor{
	
	byte yes = 1;
	byte no = 0;
	
	byte defaultRank = 0;
	
	public static String MessagePrefix = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.Prefix"));
	public static String CheckMessage = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.CheckMessage"));
	public static String onlyPlayer = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.OnlyPlayer"));
	public static String setItem = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.SetItem"));
	public static String PotentialItem = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.PotentialItem"));
	public static String MaterialItem = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.MaterialItem"));
	public static String SetMaterial = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.SetMaterial"));
	public static String NotAPotentialItem = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.NotAPotentialItem"));
	public static String CheckItemPotential = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.CheckItemPotential"));
	public static String TopRank = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.TopRank"));
	public static String TopRank_Material = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.TopRank_Material"));
	public static String CheckMaterialPotential = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.CheckMaterialPotential"));
	public static String NotAMaterial = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.NotAMaterial"));
	public static String Rankdown = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.Rankdown"));
	public static String NotRankdown = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.NotRankdown"));
	public static String ItemSlot = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.ItemSlot"));
	public static String PotentialItemCantUseThisMaterial = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.PotentialItemCantUseThisMaterial"));
	public static String ItemPotentialRankUp = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.ItemPotentialRankUp"));
	public static String MaterialRankUp = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.MaterialRankUp"));
	public static String LockItem = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.LockItem"));
	public static String AlreadyLocked = CreatePotentialItem.setStringColor(Main.pMessages.getString("Messages.SystemMessages.AlreadyLocked"));
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if(args.length == 0) {
			return false;
		}
		if(args[0].equalsIgnoreCase("attach") && args[1] != null) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				int i = Integer.parseInt(args[1]);
				ItemStack is = p.getInventory().getItemInMainHand();
				CreatePotentialItem.setAvailablePotentialKey(is, yes);
				CreatePotentialItem.setSlotPotentialKey(is, i);
				CreatePotentialItem.setRankPotentialKey(is, (byte)1);
				p.sendMessage(MessagePrefix + setItem + PotentialItem);
				switch(i) {
					case 0:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "MainHand");
						break;
					case 1:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "Boots");
						break;
					case 2:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "Leggings");
						break;
					case 3:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "Chestplate");
						break;
					case 4:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "Helmet");
						break;
					case 5:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "OffHand");
						break;
					default:
						p.sendMessage(MessagePrefix + ItemSlot + ChatColor.GREEN + "MainHand");
						break;
				}
				
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("material")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				CreatePotentialItem.setMaterialPotentialKey(is, yes);
				CreatePotentialItem.setMaterialRankPotentialKey(is, (byte)1);
				CreatePotentialItem.setMaterialRankdownPotentialKey(is, (byte)0);
				p.sendMessage(MessagePrefix + setItem + MaterialItem);
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("rank")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				if(!is.hasItemMeta() || CreatePotentialItem.checkRankPotentialKey(is) == 0) {
					p.sendMessage(MessagePrefix + NotAPotentialItem);
				}
				else if(CreatePotentialItem.checkRankPotentialKey(is) != 0){
					ItemMeta im = is.getItemMeta();
					PersistentDataContainer pdc = im.getPersistentDataContainer();
					byte rank = pdc.get(CreatePotentialItem.p_rank, PersistentDataType.BYTE);
					int slot = CreatePotentialItem.checkSlotPotentialKey(is);
					String rankStr = "";
					String slotStr = "";
					switch(rank) {
						case 1:
							rankStr = CreatePotentialItem.rankCommon;
							break;
						case 2:
							rankStr = CreatePotentialItem.rankUncommon;
							break;
						case 3:
							rankStr = CreatePotentialItem.rankRare;
							break;
						case 4:
							rankStr = CreatePotentialItem.rankLegendary;
							break;
						default:
							rankStr = CreatePotentialItem.rankCommon;
							break;
					}
					switch(slot) {
					case 0:
						slotStr = ChatColor.GREEN + "MainHand";
						break;
					case 1:
						slotStr = ChatColor.GREEN + "Boots";
						break;
					case 2:
						slotStr = ChatColor.GREEN + "Leggings";
						break;
					case 3:
						slotStr = ChatColor.GREEN + "Chestplate";
						break;
					case 4:
						slotStr = ChatColor.GREEN + "Helmet";
						break;
					case 5:
						slotStr = ChatColor.GREEN + "Offhand";
						break;
					default:
						slotStr = ChatColor.GREEN + "MainHand";
						break;
				}
					p.sendMessage(MessagePrefix + CheckItemPotential + rankStr);
					p.sendMessage(MessagePrefix + ItemSlot + slotStr);
				}
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("up")){
			if(sender instanceof Player) {
				Player p = (Player) sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				if(!is.hasItemMeta() || CreatePotentialItem.checkRankPotentialKey(is) == 0) {
					p.sendMessage(MessagePrefix + NotAPotentialItem);
				}
				else if(CreatePotentialItem.checkRankPotentialKey(is) > 0 && CreatePotentialItem.checkRankPotentialKey(is) < 4) {
					ItemMeta im = is.getItemMeta();
					PersistentDataContainer pdc = im.getPersistentDataContainer();
					pdc.set(CreatePotentialItem.p_rank, PersistentDataType.BYTE, (byte)(pdc.get(CreatePotentialItem.p_rank, PersistentDataType.BYTE) + 1));
					p.sendMessage(MessagePrefix + ItemPotentialRankUp);
					is.setItemMeta(im);
				}
				else {
					p.sendMessage(MessagePrefix + TopRank);
				}
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("mup")){
			if(sender instanceof Player) {
				Player p = (Player) sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				if(!is.hasItemMeta() || CreatePotentialItem.checkMaterialRankPotentialKey(is) == 0) {
					p.sendMessage(MessagePrefix + NotAMaterial);
				}
				else if(CreatePotentialItem.checkMaterialRankPotentialKey(is) > 0 && CreatePotentialItem.checkMaterialRankPotentialKey(is) < 3) {
					ItemMeta im = is.getItemMeta();
					PersistentDataContainer pdc = im.getPersistentDataContainer();
					pdc.set(CreatePotentialItem.p_materialRank, PersistentDataType.BYTE, (byte)(pdc.get(CreatePotentialItem.p_materialRank, PersistentDataType.BYTE) + 1));
					p.sendMessage(MessagePrefix + MaterialRankUp);
					is.setItemMeta(im);
				}
				else {
					p.sendMessage(MessagePrefix + TopRank_Material);
				}
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("mrank")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				if(!is.hasItemMeta() || CreatePotentialItem.checkMaterialRankPotentialKey(is) == 0) {
					p.sendMessage(MessagePrefix + NotAMaterial);
				}
				else if(CreatePotentialItem.checkMaterialRankPotentialKey(is) != 0){
					ItemMeta im = is.getItemMeta();
					PersistentDataContainer pdc = im.getPersistentDataContainer();
					byte rank = pdc.get(CreatePotentialItem.p_materialRank, PersistentDataType.BYTE);
					String rankStr = "";
					switch(rank) {
						case 1:
							rankStr = CreatePotentialItem.rankUncommon;
							break;
						case 2:
							rankStr = CreatePotentialItem.rankRare;
							break;
						case 3:
							rankStr = CreatePotentialItem.rankLegendary;
							break;
						default:
							rankStr = CreatePotentialItem.rankUncommon;
							break;
					}
					p.sendMessage(MessagePrefix + CheckMaterialPotential + rankStr);
					if(pdc.get(CreatePotentialItem.p_rankdown, PersistentDataType.BYTE) == 1) {
						p.sendMessage(MessagePrefix + Rankdown);
					}
					else {
						p.sendMessage(MessagePrefix + NotRankdown);
					}
				}
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("mdown")){
			if(sender instanceof Player) {
				Player p = (Player) sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				if(!is.hasItemMeta() || !CreatePotentialItem.checkMaterialPotentialKey(is)) {
					p.sendMessage(MessagePrefix + NotAMaterial);
				}
				else {
					ItemMeta im = is.getItemMeta();
					PersistentDataContainer pdc = im.getPersistentDataContainer();
					pdc.set(CreatePotentialItem.p_rankdown, PersistentDataType.BYTE, (byte)1);
					p.sendMessage(MessagePrefix + SetMaterial + Rankdown);
					is.setItemMeta(im);
				}
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		
		if(args[0].equalsIgnoreCase("ping")) {
				sender.sendMessage(MessagePrefix + CheckMessage);
		}
		
		if(args[0].equalsIgnoreCase("reload")) {
			if(!(new File(Main.getPlugin().getDataFolder(), "potentials.yml")).exists()) {
				Main.getPlugin().saveResource("potentials.yml", false);
			}
			if(!(new File(Main.getPlugin().getDataFolder(), "messages.yml")).exists()) {
				Main.getPlugin().saveResource("messages.yml", false);
			}
			Main.pMessages = YamlConfiguration.loadConfiguration(new File(Main.getPlugin().getDataFolder() + File.separator + "messages.yml"));
			Main.getPlugin().getServer().getPluginManager().registerEvents(new CreatePotentialItem(), Main.getPlugin());
			CreatePotentialItem.init();
			LockPotential.init();
			Bukkit.getLogger().info("[Potential] Plugin is loaded");
			sender.sendMessage(MessagePrefix + "reload completed!");
		}
		
		if(args[0].equalsIgnoreCase("lock")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				ItemStack is = p.getInventory().getItemInMainHand();
				CreatePotentialItem.setLockKey(is, 1);
				p.sendMessage(MessagePrefix + setItem + LockItem);
			}
			else {
				sender.sendMessage(MessagePrefix + onlyPlayer);
			}
		}
		return true;
	}
}
