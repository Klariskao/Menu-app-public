package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class MenuItemInfoDirections {
  private MenuItemInfoDirections() {
  }

  @NonNull
  public static NavDirections actionMenuItemInfoToFirstFragment() {
    return new ActionOnlyNavDirections(R.id.action_menuItemInfo_to_FirstFragment);
  }

  @NonNull
  public static ActionMenuItemInfoToEditItem actionMenuItemInfoToEditItem(
      @NonNull String menuItemIdArgument) {
    return new ActionMenuItemInfoToEditItem(menuItemIdArgument);
  }

  public static class ActionMenuItemInfoToEditItem implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionMenuItemInfoToEditItem(@NonNull String menuItemIdArgument) {
      if (menuItemIdArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIdArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIdArgument", menuItemIdArgument);
    }

    @NonNull
    public ActionMenuItemInfoToEditItem setMenuItemIdArgument(@NonNull String menuItemIdArgument) {
      if (menuItemIdArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIdArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIdArgument", menuItemIdArgument);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("menuItemIdArgument")) {
        String menuItemIdArgument = (String) arguments.get("menuItemIdArgument");
        __result.putString("menuItemIdArgument", menuItemIdArgument);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_menuItemInfo_to_editItem;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getMenuItemIdArgument() {
      return (String) arguments.get("menuItemIdArgument");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionMenuItemInfoToEditItem that = (ActionMenuItemInfoToEditItem) object;
      if (arguments.containsKey("menuItemIdArgument") != that.arguments.containsKey("menuItemIdArgument")) {
        return false;
      }
      if (getMenuItemIdArgument() != null ? !getMenuItemIdArgument().equals(that.getMenuItemIdArgument()) : that.getMenuItemIdArgument() != null) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getMenuItemIdArgument() != null ? getMenuItemIdArgument().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionMenuItemInfoToEditItem(actionId=" + getActionId() + "){"
          + "menuItemIdArgument=" + getMenuItemIdArgument()
          + "}";
    }
  }
}
