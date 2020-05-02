package com.github.hornta.versioned_config.config;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class FakeOfflinePlayer implements OfflinePlayer {
  @Override
  public boolean isOnline() {
    return false;
  }

  @Override
  public String getName() {
    return "hornta";
  }

  @Override
  public UUID getUniqueId() {
    return null;
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
    return null;
  }

  @Override
  public boolean isOp() {
    return false;
  }

  @Override
  public void setOp(boolean b) {

  }
}
