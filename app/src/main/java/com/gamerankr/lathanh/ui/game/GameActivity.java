package com.gamerankr.lathanh.ui.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gamerankr.lathanh.R;

public class GameActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.game_activity);

    if (savedInstanceState == null) {
      String gameId = getIntent().getStringExtra("GAME_ID");

      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, GameFragment.newInstance(gameId))
          .commitNow();
    }
  }
}
