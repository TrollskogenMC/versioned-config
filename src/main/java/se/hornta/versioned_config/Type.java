package se.hornta.versioned_config;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public enum Type {
  BOOLEAN,
  COLOR,
  DOUBLE,
  INTEGER,
  ITEM_STACK,
  LIST,
  LONG,
  OFFLINE_PLAYER,
  STRING,
  VECTOR;

  public boolean isCorrectType(Object object) {
    return
      this == BOOLEAN && object instanceof Boolean ||
      this == COLOR && object instanceof Color ||
      this == DOUBLE && (object instanceof Double || object instanceof Integer) ||
      this == INTEGER && object instanceof Integer ||
      this == ITEM_STACK && object instanceof ItemStack ||
      this == LIST && object instanceof List ||
      this == LONG && object instanceof Long ||
      this == OFFLINE_PLAYER && object instanceof OfflinePlayer ||
      this == STRING && object instanceof String ||
      this == VECTOR && object instanceof Vector;
  }
}
