package com.github.hornta.versioned_config.config;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class FakeJavaPlugin extends PluginBase {
  private FakeServer server;

  FakeJavaPlugin() {
    this.server = new FakeServer();
  }

  @Override
  public File getDataFolder() {
    return new File("FakeJavaPlugin");
  }

  @Override
  public PluginDescriptionFile getDescription() {
    return new PluginDescriptionFile("FakeJavaPlugin", "1.0", "test.test");
  }

  @Override
  public FileConfiguration getConfig() {
    return null;
  }

  @Override
  public InputStream getResource(String s) {
    return null;
  }

  @Override
  public void saveConfig() {

  }

  @Override
  public void saveDefaultConfig() {

  }

  @Override
  public void saveResource(String s, boolean b) {

  }

  @Override
  public void reloadConfig() {

  }

  @Override
  public PluginLoader getPluginLoader() {
    return null;
  }

  @Override
  public Server getServer() {
    return server;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public void onDisable() {

  }

  @Override
  public void onLoad() {

  }

  @Override
  public void onEnable() {

  }

  @Override
  public boolean isNaggable() {
    return false;
  }

  @Override
  public void setNaggable(boolean b) {

  }

  @Override
  public ChunkGenerator getDefaultWorldGenerator(String s, String s1) {
    return null;
  }

  @Override
  public Logger getLogger() {
    return new PluginLogger(this);
  }

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return null;
  }
}
