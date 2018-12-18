package com.gamerankr.lathanh;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.gamerankr.fragment.UserBasic;
import com.gamerankr.lathanh.api.AuthenticationStore;
import com.gamerankr.lathanh.app.dagger2.components.ControllerComponent;
import com.gamerankr.lathanh.app.dagger2.components.DaggerControllerComponent;
import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.lathanh.ui.authentication.LoginActivity;
import com.gamerankr.queries.MeQuery;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

  //== Constants ==============================================================

  /** RequestCode for starting LoginActivity for result. */
  private static final int REQUEST_CODE_LOGIN = 100;


  //== Instance fields ========================================================

  //-- Dependencies -----------------------------------------------------------

  @Inject
  AuthenticationStore authenticationStore;

  @Inject
  ApolloClient apolloClient;

  //-- Operating fields -------------------------------------------------------

  private GameRankrNavigationListener onNavigationItemSelectedListener =
      new GameRankrNavigationListener(this);


  //== Instance methods =======================================================

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    loadMe();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_LOGIN) {
     if (resultCode == 1) {
       startActivity(new Intent(this, MainActivity.class));
       finish();
     }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  //-- Private methods --------------------------------------------------------

  private void loadMe() {
    //todo-xxx: replace with Dagger Android injection
    ControllerComponent component =
        DaggerControllerComponent.builder()
            .backendServiceModule(new BackendServiceModule(this))
            .build();
    component.inject(this);

    if (authenticationStore.getAuthenticationToken() == null) {
      ((TextView)findViewById(R.id.message)).setText("Current user: <not logged in>");

      View loginButton = findViewById(R.id.login_button);
      loginButton.setVisibility(View.VISIBLE);
      loginButton.setOnClickListener(view ->
          startActivityForResult(
              new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN));
    } else {
      ApolloQueryCall<MeQuery.Data> query =
          apolloClient.query(
              MeQuery.builder().build());
      query.enqueue(new ApolloCall.Callback<MeQuery.Data>() {
        @Override
        public void onResponse(@NotNull Response<MeQuery.Data> response) {
          UserBasic user = response.data().user().fragments().userBasic();
          Log.v("loadMe",
              String.format("loadMe(): onResponse. id=%s, real_name=%s",
                  user.id(), user.real_name()));

          MainActivity.this.runOnUiThread(() -> {
            ((TextView)findViewById(R.id.message)).setText("Current user: " + user.real_name());

            View loginButton = findViewById(R.id.logout_button);
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(view -> {
              authenticationStore.setAuthenticationToken(null);
              startActivity(new Intent(MainActivity.this, MainActivity.class));
              finish();
            });

          });
        }

        @Override
        public void onFailure(@NotNull ApolloException e) {
          // TODO-XXXX
          Log.w("loadMe", "loadMe(): onFailure", e);
        }
      });
    }
  } // loadMe()

}
