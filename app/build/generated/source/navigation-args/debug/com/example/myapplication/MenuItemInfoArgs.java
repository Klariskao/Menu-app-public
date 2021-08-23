package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class MenuItemInfoArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private MenuItemInfoArgs() {
  }

  private MenuItemInfoArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static MenuItemInfoArgs fromBundle(@NonNull Bundle bundle) {
    MenuItemInfoArgs __result = new MenuItemInfoArgs();
    bundle.setClassLoader(MenuItemInfoArgs.class.getClassLoader());
    if (bundle.containsKey("menuItemIDArgument")) {
      String menuItemIDArgument;
      menuItemIDArgument = bundle.getString("menuItemIDArgument");
      if (menuItemIDArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIDArgument\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("menuItemIDArgument", menuItemIDArgument);
    } else {
      throw new IllegalArgumentException("Required argument \"menuItemIDArgument\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getMenuItemIDArgument() {
    return (String) arguments.get("menuItemIDArgument");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("menuItemIDArgument")) {
      String menuItemIDArgument = (String) arguments.get("menuItemIDArgument");
      __result.putString("menuItemIDArgument", menuItemIDArgument);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    MenuItemInfoArgs that = (MenuItemInfoArgs) object;
    if (arguments.containsKey("menuItemIDArgument") != that.arguments.containsKey("menuItemIDArgument")) {
      return false;
    }
    if (getMenuItemIDArgument() != null ? !getMenuItemIDArgument().equals(that.getMenuItemIDArgument()) : that.getMenuItemIDArgument() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getMenuItemIDArgument() != null ? getMenuItemIDArgument().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MenuItemInfoArgs{"
        + "menuItemIDArgument=" + getMenuItemIDArgument()
        + "}";
  }

  public static class Builder {
    private final HashMap arguments = new HashMap();

    public Builder(MenuItemInfoArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder(@NonNull String menuItemIDArgument) {
      if (menuItemIDArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIDArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIDArgument", menuItemIDArgument);
    }

    @NonNull
    public MenuItemInfoArgs build() {
      MenuItemInfoArgs result = new MenuItemInfoArgs(arguments);
      return result;
    }

    @NonNull
    public Builder setMenuItemIDArgument(@NonNull String menuItemIDArgument) {
      if (menuItemIDArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIDArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIDArgument", menuItemIDArgument);
      return this;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getMenuItemIDArgument() {
      return (String) arguments.get("menuItemIDArgument");
    }
  }
}
