package com.gamerankr.lathanh.ui.game;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.Logger;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.exception.ApolloException;
import com.gamerankr.queries.GameQuery;

import org.jetbrains.annotations.NotNull;

public class GameViewModel extends ViewModel {

  MutableLiveData<GameQuery.Game> game;

  public LiveData<GameQuery.Game> getGame(String gameId) {
    if (game == null) {
      game = new MutableLiveData<>();
      loadGame(gameId);
    }
    return game;
  }

  private void loadGame(String gameId) {
    ApolloClient apolloClient =
        ApolloClient.builder()
            .serverUrl("https://www.gamerankr.com/graphql")
            .logger(new Logger() {
              @Override
              public void log(int priority, @NotNull String message,
                              @NotNull Optional<Throwable> t, @NotNull Object... args) {
                Log.println(priority, "PopularGamesFrag", String.format(message, args));
              }
            })
            .build();

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
