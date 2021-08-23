package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class SecondFragmentDirections {
  private SecondFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSecondFragmentToFirstFragment() {
    return new ActionOnlyNavDirections(R.id.action_SecondFragment_to_FirstFragment);
  }

  @NonNull
  public static NavDirections actionSecondFragmentToPermissionChecker() {
    return new ActionOnlyNavDirections(R.id.action_SecondFragment_to_permissionChecker);
  }
}
