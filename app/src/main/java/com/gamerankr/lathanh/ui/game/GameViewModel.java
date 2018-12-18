package com.gamerankr.lathanh.ui.game;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.facebook.FacebookSdk;
import com.gamerankr.lathanh.app.dagger2.components.DaggerViewModelComponent;
import com.gamerankr.lathanh.app.dagger2.components.ViewModelComponent;
import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.queries.GameQuery;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class GameViewModel extends ViewModel {

  //-- Dependencies ----------------------------------------------------------

  @Inject
  ApolloClient apolloClient;

  //-- Operating fields -----------------------------------------------------

  MutableLiveData<GameQuery.Game> game;

  //== Instantiation =========================================================

  public GameViewModel() {
    ViewModelComponent component =
        DaggerViewModelComponent.builder()
            //todo-xxxx hacky way to get context
            .backendServiceModule(new BackendServiceModule(FacebookSdk.getApplicationContext()))
            .build();
    component.inject(this);
  }


  //== Instance methods ======================================================

  public LiveData<GameQuery.Game> getGame(String gameId) {
    if (game == null) {
      game = new MutableLiveData<>();
      loadGame(gameId);
    }
    return game;
  }

  private void loadGame(String gameId) {

    ApolloQueryCall<GameQuery.Data> query =
        apolloClient.query(
            GameQuery.builder()
                .id(gameId)
                .build());
    query.enqueue(new ApolloCall.Callback<GameQuery.Data>() {
      @Override
      public void onResponse(@NotNull Response<GameQuery.Data> response) {
        GameQuery.Game gameData = response.data().game();
        game.postValue(gameData);
      }

      @Override
      public void onFailure(@NotNull ApolloException e) {
        // TODO-XXXX
        Log.e("PopularGamesFrag", "onFailure", e);
      }
    });
  } // loadGame()
}
