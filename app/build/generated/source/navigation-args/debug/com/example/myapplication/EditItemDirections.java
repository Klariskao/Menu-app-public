package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class EditItemDirections {
  private EditItemDirections() {
  }

  @NonNull
  public static NavDirections actionEditItemToFirstFragment() {
    return new ActionOnlyNavDirections(R.id.action_editItem_to_FirstFragment);
  }

  @NonNull
  public static NavDirections actionEditItemToPermissionChecker() {
    return new ActionOnlyNavDirections(R.id.action_editItem_to_permissionChecker);
  }
}
