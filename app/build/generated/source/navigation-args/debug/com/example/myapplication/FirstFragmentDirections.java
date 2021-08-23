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

public class FirstFragmentDirections {
  private FirstFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionFirstFragmentToSecondFragment() {
    return new ActionOnlyNavDirections(R.id.action_FirstFragment_to_SecondFragment);
  }

  @NonNull
  public static ActionFirstFragmentToMenuItemInfo actionFirstFragmentToMenuItemInfo(
      @NonNull String menuItemIDArgument) {
    return new ActionFirstFragmentToMenuItemInfo(menuItemIDArgument);
  }

  public static class ActionFirstFragmentToMenuItemInfo implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionFirstFragmentToMenuItemInfo(@NonNull String menuItemIDArgument) {
      if (menuItemIDArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIDArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIDArgument", menuItemIDArgument);
    }

    @NonNull
    public ActionFirstFragmentToMenuItemInfo setMenuItemIDArgument(
        @NonNull String menuItemIDArgument) {
      if (menuItemIDArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIDArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIDArgument", menuItemIDArgument);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("menuItemIDArgument")) {
        String menuItemIDArgument = (String) arguments.get("menuItemIDArgument");
        __result.putString("menuItemIDArgument", menuItemIDArgument);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_FirstFragment_to_menuItemInfo;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getMenuItemIDArgument() {
      return (String) arguments.get("menuItemIDArgument");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionFirstFragmentToMenuItemInfo that = (ActionFirstFragmentToMenuItemInfo) object;
      if (arguments.containsKey("menuItemIDArgument") != that.arguments.containsKey("menuItemIDArgument")) {
        return false;
      }
      if (getMenuItemIDArgument() != null ? !getMenuItemIDArgument().equals(that.getMenuItemIDArgument()) : that.getMenuItemIDArgument() != null) {
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
      result = 31 * result + (getMenuItemIDArgument() != null ? getMenuItemIDArgument().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionFirstFragmentToMenuItemInfo(actionId=" + getActionId() + "){"
          + "menuItemIDArgument=" + getMenuItemIDArgument()
          + "}";
    }
  }
}
