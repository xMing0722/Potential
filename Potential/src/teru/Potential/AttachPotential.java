package teru.Potential;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.collect.Multimap;

import teru.Potential.CreatePotentialItem.ItemData;

public class AttachPotential{

	public static void Attach(ItemStack is, List<ItemData> potentialList) {
		ItemMeta im = is.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		EquipmentSlot equip = null;
		
		switch (CreatePotentialItem.checkSlotPotentialKey(is)) {
		case 0:
			equip = EquipmentSlot.HAND;
			break;
		case 1:
			equip = EquipmentSlot.FEET;
			break;
		case 2:
			equip = EquipmentSlot.LEGS;
			break;
		case 3:
			equip = EquipmentSlot.CHEST;
			break;
		case 4:
			equip = EquipmentSlot.HEAD;
			break;
		case 5:
			equip = EquipmentSlot.OFF_HAND;
			break;
		default:
			equip = EquipmentSlot.HAND;
			break;
		}
		
		Multimap<Attribute, AttributeModifier> amf = im.getAttributeModifiers(equip);
		Collection<AttributeModifier> g_armor = amf.get(Attribute.GENERIC_ARMOR);
		Collection<AttributeModifier> g_hp = amf.get(Attribute.GENERIC_MAX_HEALTH);
		Collection<AttributeModifier> at = amf.get(Attribute.GENERIC_ARMOR_TOUGHNESS);
		Collection<AttributeModifier> ad = amf.get(Attribute.GENERIC_ATTACK_DAMAGE);
		Collection<AttributeModifier> as = amf.get(Attribute.GENERIC_ATTACK_SPEED);
		Collection<AttributeModifier> kr = amf.get(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
		Collection<AttributeModifier> ms = amf.get(Attribute.GENERIC_MOVEMENT_SPEED);

		List<AttributeModifier> old_g_armor = new ArrayList<AttributeModifier>();
		List<AttributeModifier> old_g_hp = new ArrayList<AttributeModifier>();
		List<AttributeModifier> old_at = new ArrayList<AttributeModifier>();
		List<AttributeModifier> old_ad = new ArrayList<AttributeModifier>();
		List<AttributeModifier> old_as = new ArrayList<AttributeModifier>();
		List<AttributeModifier> old_kr = new ArrayList<AttributeModifier>();
		List<AttributeModifier> old_ms = new ArrayList<AttributeModifier>();
		
		try{
			if(pdc.has(CreatePotentialItem.p_attrUUID1, PersistentDataType.STRING)) {
				
				Set<String> attrUUIDs = new HashSet<String>();
				attrUUIDs.add(pdc.get(CreatePotentialItem.p_attrUUID1, PersistentDataType.STRING));
				attrUUIDs.add(pdc.get(CreatePotentialItem.p_attrUUID2, PersistentDataType.STRING));
				attrUUIDs.add(pdc.get(CreatePotentialItem.p_attrUUID3, PersistentDataType.STRING));
				
				for (AttributeModifier amd : g_armor) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_g_armor.add(amd);
					}
				}
				
				for (AttributeModifier amd : g_hp) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_g_hp.add(amd);
					}
				}
				
				for (AttributeModifier amd : at) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_at.add(amd);
					}
				}
				
				for (AttributeModifier amd : ad) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_ad.add(amd);
					}
				}
				
				for (AttributeModifier amd : as) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_as.add(amd);
					}
				}
				
				for (AttributeModifier amd : kr) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_kr.add(amd);
					}
				}
				
				for (AttributeModifier amd : ms) {
					if(!attrUUIDs.contains(amd.getUniqueId().toString())) {
						old_ms.add(amd);
					}
				}
				
				g_armor.clear();
				g_hp.clear();
				at.clear();
				ad.clear();
				as.clear();
				kr.clear();
				ms.clear();
			}
		}
		catch(Exception e) {
			
		}


		for (int i = 0; i < 3; i++) {
			ItemData id = potentialList.get(i);
			String str = id.getPotential();
			UUID uuid = UUID.randomUUID();
			switch (str) {
			case "armor":
				g_armor.add(new AttributeModifier(uuid, "generic.armor", id.getNumber(),
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			case "at":
				at.add(new AttributeModifier(uuid, "generic.armorToughness", id.getNumber(),
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			case "hp":
				g_hp.add(new AttributeModifier(uuid, "generic.maxHealth", id.getNumber(),
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			case "ad":
				ad.add(new AttributeModifier(uuid, "generic.attackDamage", id.getNumber() + 1.0D,
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			case "as":
				as.add(new AttributeModifier(uuid, "generic.attackSpeed", id.getNumber(),
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			case "kr":
				kr.add(new AttributeModifier(uuid, "generic.knockbackResistance", id.getNumber(),
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			case "ms":
				ms.add(new AttributeModifier(uuid, "generic.movementSpeed", id.getNumber(),
						AttributeModifier.Operation.MULTIPLY_SCALAR_1, equip));
				break;
			}
			
			String uuidStr = uuid.toString();
			switch(i) {
				case 0:
					pdc.set(CreatePotentialItem.p_attrUUID1, PersistentDataType.STRING, uuidStr);
					break;
				case 1:
					pdc.set(CreatePotentialItem.p_attrUUID2, PersistentDataType.STRING, uuidStr);
					break;
				case 2:
					pdc.set(CreatePotentialItem.p_attrUUID3, PersistentDataType.STRING, uuidStr);
					break;
			}
		}
		
		for(AttributeModifier amd : old_g_armor) {
			g_armor.add(amd);
		}
		for(AttributeModifier amd : old_g_hp) {
			g_hp.add(amd);
		}
		for(AttributeModifier amd : old_at) {
			at.add(amd);
		}
		for(AttributeModifier amd : old_ad) {
			ad.add(amd);
		}
		for(AttributeModifier amd : old_as) {
			as.add(amd);
		}
		for(AttributeModifier amd : old_kr) {
			kr.add(amd);
		}
		for(AttributeModifier amd : old_ms) {
			ms.add(amd);
		}

		im.setAttributeModifiers(amf);
		is.setItemMeta(im);
	}
}
