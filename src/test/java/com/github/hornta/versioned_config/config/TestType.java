package com.github.hornta.versioned_config.config;

import com.github.hornta.versioned_config.Type;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class TestType {
  @Test
  public void Test_CorrectType_Boolean() {
    Assert.assertTrue(Type.BOOLEAN.isCorrectType(false));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(1.0));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(1));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(1L));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(""));
    Assert.assertFalse(Type.BOOLEAN.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_Color() {
    Assert.assertFalse(Type.COLOR.isCorrectType(false));
    Assert.assertTrue(Type.COLOR.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.COLOR.isCorrectType(1.0));
    Assert.assertFalse(Type.COLOR.isCorrectType(1));
    Assert.assertFalse(Type.COLOR.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.COLOR.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.COLOR.isCorrectType(1L));
    Assert.assertFalse(Type.COLOR.isCorrectType(""));
    Assert.assertFalse(Type.COLOR.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_Double() {
    Assert.assertFalse(Type.DOUBLE.isCorrectType(false));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(Color.BLUE));
    Assert.assertTrue(Type.DOUBLE.isCorrectType(1.0));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(1));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(1L));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(""));
    Assert.assertFalse(Type.DOUBLE.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_Integer() {
    Assert.assertFalse(Type.INTEGER.isCorrectType(false));
    Assert.assertFalse(Type.INTEGER.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.INTEGER.isCorrectType(1.0));
    Assert.assertTrue(Type.INTEGER.isCorrectType(1));
    Assert.assertFalse(Type.INTEGER.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.INTEGER.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.INTEGER.isCorrectType(1L));
    Assert.assertFalse(Type.INTEGER.isCorrectType(""));
    Assert.assertFalse(Type.INTEGER.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_ItemStack() {
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(false));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(1.0));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(1));
    Assert.assertTrue(Type.ITEM_STACK.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(1L));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(""));
    Assert.assertFalse(Type.ITEM_STACK.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_List() {
    Assert.assertFalse(Type.LIST.isCorrectType(false));
    Assert.assertFalse(Type.LIST.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.LIST.isCorrectType(1.0));
    Assert.assertFalse(Type.LIST.isCorrectType(1));
    Assert.assertFalse(Type.LIST.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertTrue(Type.LIST.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.LIST.isCorrectType(1L));
    Assert.assertFalse(Type.LIST.isCorrectType(""));
    Assert.assertFalse(Type.LIST.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_Long() {
    Assert.assertFalse(Type.LONG.isCorrectType(false));
    Assert.assertFalse(Type.LONG.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.LONG.isCorrectType(1.0));
    Assert.assertFalse(Type.LONG.isCorrectType(1));
    Assert.assertFalse(Type.LONG.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.LONG.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertTrue(Type.LONG.isCorrectType(1L));
    Assert.assertFalse(Type.LONG.isCorrectType(""));
    Assert.assertFalse(Type.LONG.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_String() {
    Assert.assertFalse(Type.STRING.isCorrectType(false));
    Assert.assertFalse(Type.STRING.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.STRING.isCorrectType(1.0));
    Assert.assertFalse(Type.STRING.isCorrectType(1));
    Assert.assertFalse(Type.STRING.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.STRING.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.STRING.isCorrectType(1L));
    Assert.assertTrue(Type.STRING.isCorrectType(""));
    Assert.assertFalse(Type.STRING.isCorrectType(Vector.getRandom()));
  }

  @Test
  public void Test_CorrectType_Vector() {
    Assert.assertFalse(Type.VECTOR.isCorrectType(false));
    Assert.assertFalse(Type.VECTOR.isCorrectType(Color.BLUE));
    Assert.assertFalse(Type.VECTOR.isCorrectType(1.0));
    Assert.assertFalse(Type.VECTOR.isCorrectType(1));
    Assert.assertFalse(Type.VECTOR.isCorrectType(new ItemStack(Material.ACACIA_BOAT)));
    Assert.assertFalse(Type.VECTOR.isCorrectType(Collections.EMPTY_LIST));
    Assert.assertFalse(Type.VECTOR.isCorrectType(1L));
    Assert.assertFalse(Type.VECTOR.isCorrectType(""));
    Assert.assertTrue(Type.VECTOR.isCorrectType(Vector.getRandom()));
  }
}
