package com.gamerankr.lathanh.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.gamerankr.lathanh.GameRankrNavigationListener;
import com.gamerankr.lathanh.R;
import com.gamerankr.lathanh.api.AuthenticationStore;
import com.gamerankr.lathanh.app.dagger2.components.ControllerComponent;
import com.gamerankr.lathanh.app.dagger2.components.DaggerControllerComponent;
import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.lathanh.ui.authentication.LoginActivity;
import com.gamerankr.lathanh.ui.explore.GameRecyclerViewAdapter;
import com.gamerankr.lathanh.ui.explore.PopularGamesFragment;
import com.gamerankr.lathanh.ui.game.GameActivity;

import javax.inject.Inject;

public class MeActivity extends AppCompatActivity
    implements PopularGamesFragment.InteractionListener {

  //== Constants ==============================================================

  private static final int REQUEST_CODE_LOGIN = 100;


  //== Instance fields ========================================================

  //-- Dependencies -----------------------------------------------------------

  @Inject
  AuthenticationStore authenticationStore;

  //-- Operating fields -------------------------------------------------------

  private GameRankrNavigationListener onNavigationItemSelectedListener =
      new GameRankrNavigationListener(this);


  //== Instance methods =======================================================

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //-- Init
    super.onCreate(savedInstanceState);

    //todo-xxx: replace with Dagger Android injection
    ControllerComponent component =
        DaggerControllerComponent.builder()
            .backendServiceModule(new BackendServiceModule(this))
            .build();
    component.inject(this);

    // Init UI
    setContentView(R.layout.activity_explore);

    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setSelectedItemId(R.id.navigation_me);
    navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    //-- Redirect to login if not logged in
    if (authenticationStore.getAuthenticationToken() == null) {
      // not logged in
      startActivityForResult(new Intent(this, LoginActivity.class),
          REQUEST_CODE_LOGIN);
    } else {
      // logged in
      if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, MyGamesFragment.newInstance())
            .commitNow();
      }
    }
  } // onCreate()

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_LOGIN) {
      if (resultCode == AppCompatActivity.RESULT_OK) {
        startActivity(new Intent(this, MeActivity.class));
        finish();
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onGameItemChosen(GameRecyclerViewAdapter.GameItem item) {
    Intent intent = new Intent(this, GameActivity.class);
    intent.putExtra("GAME_ID", item.id);
    startActivity(intent);
  }
}
