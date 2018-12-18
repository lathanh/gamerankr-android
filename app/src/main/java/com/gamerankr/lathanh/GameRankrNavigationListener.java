package com.gamerankr.lathanh;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.gamerankr.lathanh.ui.authentication.LoginActivity;
import com.gamerankr.lathanh.ui.explore.ExploreActivity;
import com.gamerankr.lathanh.ui.me.MeActivity;

public class GameRankrNavigationListener
    implements BottomNavigationView.OnNavigationItemSelectedListener {

  private final Activity activity;

  public GameRankrNavigationListener(Activity activity) {
    this.activity = activity;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.navigation_home:
        if (activity.getClass() != MainActivity.class)
          activity.startActivity(new Intent(activity, MainActivity.class));
        return true;
      case R.id.navigation_explore:
        if (activity.getClass() != ExploreActivity.class)
          activity.startActivity(new Intent(activity, ExploreActivity.class));
        return true;
      case R.id.navigation_me:
        if (activity.getClass() != LoginActivity.class)
          activity.startActivity(new Intent(activity, MeActivity.class));
        return true;
    }
    return false;
  }
}
