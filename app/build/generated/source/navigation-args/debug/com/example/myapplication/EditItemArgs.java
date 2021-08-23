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

public class EditItemArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private EditItemArgs() {
  }

  private EditItemArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static EditItemArgs fromBundle(@NonNull Bundle bundle) {
    EditItemArgs __result = new EditItemArgs();
    bundle.setClassLoader(EditItemArgs.class.getClassLoader());
    if (bundle.containsKey("menuItemIdArgument")) {
      String menuItemIdArgument;
      menuItemIdArgument = bundle.getString("menuItemIdArgument");
      if (menuItemIdArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIdArgument\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("menuItemIdArgument", menuItemIdArgument);
    } else {
      throw new IllegalArgumentException("Required argument \"menuItemIdArgument\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getMenuItemIdArgument() {
    return (String) arguments.get("menuItemIdArgument");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("menuItemIdArgument")) {
      String menuItemIdArgument = (String) arguments.get("menuItemIdArgument");
      __result.putString("menuItemIdArgument", menuItemIdArgument);
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
    EditItemArgs that = (EditItemArgs) object;
    if (arguments.containsKey("menuItemIdArgument") != that.arguments.containsKey("menuItemIdArgument")) {
      return false;
    }
    if (getMenuItemIdArgument() != null ? !getMenuItemIdArgument().equals(that.getMenuItemIdArgument()) : that.getMenuItemIdArgument() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getMenuItemIdArgument() != null ? getMenuItemIdArgument().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "EditItemArgs{"
        + "menuItemIdArgument=" + getMenuItemIdArgument()
        + "}";
  }

  public static class Builder {
    private final HashMap arguments = new HashMap();

    public Builder(EditItemArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder(@NonNull String menuItemIdArgument) {
      if (menuItemIdArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIdArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIdArgument", menuItemIdArgument);
    }

    @NonNull
    public EditItemArgs build() {
      EditItemArgs result = new EditItemArgs(arguments);
      return result;
    }

    @NonNull
    public Builder setMenuItemIdArgument(@NonNull String menuItemIdArgument) {
      if (menuItemIdArgument == null) {
        throw new IllegalArgumentException("Argument \"menuItemIdArgument\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("menuItemIdArgument", menuItemIdArgument);
      return this;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getMenuItemIdArgument() {
      return (String) arguments.get("menuItemIdArgument");
    }
  }
}
