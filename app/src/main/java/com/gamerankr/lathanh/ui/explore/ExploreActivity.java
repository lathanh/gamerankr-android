package com.gamerankr.lathanh.ui.explore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.gamerankr.lathanh.GameRankrNavigationListener;
import com.gamerankr.lathanh.R;
import com.gamerankr.lathanh.ui.game.GameActivity;

public class ExploreActivity extends AppCompatActivity
    implements PopularGamesFragment.InteractionListener {

  private GameRankrNavigationListener onNavigationItemSelectedListener =
      new GameRankrNavigationListener(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_explore);

    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setSelectedItemId(R.id.navigation_explore);
    navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, PopularGamesFragment.newInstance())
          .commitNow();
    }
  }

  @Override
  public void onGameItemChosen(GameRecyclerViewAdapter.GameItem item) {
    Intent intent = new Intent(this, GameActivity.class);
    intent.putExtra("GAME_ID", item.id);
    startActivity(intent);
  }
}
