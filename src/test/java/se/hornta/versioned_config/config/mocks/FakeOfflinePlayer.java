package se.hornta.versioned_config.config.mocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class FakeOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {
  @Override
  public boolean isOnline() {
    return false;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public UUID getUniqueId() {
    return UUID.fromString("084859ed-3139-40a5-a17a-8d94ba29597c");
  }

  @Override
  public boolean isBanned() {
    return false;
  }

  @Override
  public boolean isWhitelisted() {
    return false;
  }

  @Override
  public void setWhitelisted(boolean b) {

  }

  @Override
  public Player getPlayer() {
    return null;
  }

  @Override
  public long getFirstPlayed() {
    return 0;
  }

  @Override
  public long getLastPlayed() {
    return 0;
  }

  @Override
  public boolean hasPlayedBefore() {
    return false;
  }

  @Override
  public Location getBedSpawnLocation() {
    return null;
  }

  @Override
  public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {

  }

  @Override
  public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {

  }

  @Override
  public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {

  }

  @Override
  public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {

  }

  @Override
  public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {

  }

  @Override
  public int getStatistic(Statistic statistic) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

  }

  @Override
  public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

  }

  @Override
  public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

  }

  @Override
  public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

  }

  @Override
  public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

  }

  @Override
  public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

  }

  @Override
  public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

  }

  @Override
  public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {

  }

  @Override
  public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {

  }

  @Override
  public void setStatistic(Statistic statistic, EntityType entityType, int i) {

  }

  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> result = new LinkedHashMap<>();

    result.put("UUID", getUniqueId().toString());

    return result;
  }

  @Override
  public boolean isOp() {
    return false;
  }

  @Override
  public void setOp(boolean b) {

  }
}
