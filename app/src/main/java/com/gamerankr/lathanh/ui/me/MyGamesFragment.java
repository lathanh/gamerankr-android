package com.gamerankr.lathanh.ui.me;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.facebook.FacebookSdk;
import com.gamerankr.fragment.GameBasic;
import com.gamerankr.fragment.RankingWithGame;
import com.gamerankr.lathanh.R;
import com.gamerankr.lathanh.app.dagger2.components.DaggerViewModelComponent;
import com.gamerankr.lathanh.app.dagger2.components.ViewModelComponent;
import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.lathanh.ui.explore.GameRecyclerViewAdapter;
import com.gamerankr.lathanh.ui.explore.PopularGamesFragment;
import com.gamerankr.queries.MyGamesQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MyGamesFragment extends Fragment {

  private PopularGamesFragment.InteractionListener interactionListener;


  //== Instantiation =========================================================

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */

  public static MyGamesFragment newInstance() {
    return new MyGamesFragment();
  }


  //== Instance methods ======================================================

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_popular_games, container, false);

    // Set the adapter
    final List<GameRecyclerViewAdapter.GameItem> gameItems = new ArrayList<>();
    Context context = view.getContext();
    RecyclerView recyclerView = (RecyclerView) view;
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    final GameRecyclerViewAdapter adapter =
        new GameRecyclerViewAdapter(gameItems, interactionListener);
    recyclerView.setAdapter(adapter);

    // Popular games view model
    MyGamesViewModel viewModel =
        ViewModelProviders.of(this).get(MyGamesViewModel.class);
    viewModel.getMyGames().observe(this, gameBasics -> {
      for (GameBasic game : gameBasics) {
        GameRecyclerViewAdapter.GameItem gameItem =
            new GameRecyclerViewAdapter.GameItem(adapter, game.id(), game.title(), game.url(),
                game.ports().get(0).small_image_url());
        gameItems.add(gameItem);
      }

      adapter.notifyDataSetChanged();
    });

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof PopularGamesFragment.InteractionListener) {
      interactionListener = (PopularGamesFragment.InteractionListener) context;
    } else {
      Log.e("MyGamesFrag", context.toString() + " must implement InteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    interactionListener = null;
  }

  //== Nested classes ========================================================

  public static class MyGamesViewModel extends ViewModel {

    //-- Dependencies --------------------------------------------------------

    @Inject
    ApolloClient apolloClient;

    //-- Operating fields ----------------------------------------------------

    private MutableLiveData<List<GameBasic>> games;

    //== Instantiation =======================================================

    public MyGamesViewModel() {
      ViewModelComponent component =
          DaggerViewModelComponent.builder()
              //todo-xxxx hacky way to get context
              .backendServiceModule(new BackendServiceModule(FacebookSdk.getApplicationContext()))
              .build();
      component.inject(this);
    }

    //== Instance methods ======================================================

    public LiveData<List<GameBasic>> getMyGames() {
      if (games == null) {
        games = new MutableLiveData<>();
        loadMyGames();
      }
      return games;
    }

    private void loadMyGames() {
      ApolloQueryCall<MyGamesQuery.Data> query =
          apolloClient.query(
              MyGamesQuery.builder().build());
      query.enqueue(new ApolloCall.Callback<MyGamesQuery.Data>() {
        @Override
        public void onResponse(@NotNull Response<MyGamesQuery.Data> response) {
          List<MyGamesQuery.Edge> myGames = response.data().my_games().edges();
          List<GameBasic> gameBasicList = new ArrayList<>(myGames.size());

          for (MyGamesQuery.Edge myGame : myGames) {
            RankingWithGame.Game game = myGame.ranking().fragments().rankingWithGame().game();
            gameBasicList.add(game.fragments().gameBasic());
          }

          games.postValue(gameBasicList);
        }

        @Override
        public void onFailure(@NotNull ApolloException e) {
          // TODO-XXXX
          Log.e("MyGamesFrag", "onFailure", e);
        }
      });
    } // loadMyGames()
  } // class MyGamesViewModel

}
