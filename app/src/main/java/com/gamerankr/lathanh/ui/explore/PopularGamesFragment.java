package com.gamerankr.lathanh.ui.explore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.apollographql.apollo.Logger;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.exception.ApolloException;
import com.gamerankr.fragment.GameBasic;
import com.gamerankr.lathanh.R;
import com.gamerankr.queries.PopularGamesQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment for showing the list of popular games.
 */
public class PopularGamesFragment extends Fragment {

  private InteractionListener interactionListener;


  //== Instantiation =========================================================

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public PopularGamesFragment() {
  }

  @SuppressWarnings("unused")
  public static PopularGamesFragment newInstance() {
    return new PopularGamesFragment();
  }


  //== Instance methods ======================================================

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_popular_games, container, false);

    // Set the adapter
    final List<GameRecyclerViewAdapter.GameItem> popularGameItems = new ArrayList<>();
    Context context = view.getContext();
    RecyclerView recyclerView = (RecyclerView) view;
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    final GameRecyclerViewAdapter adapter =
        new GameRecyclerViewAdapter(popularGameItems, interactionListener);
    recyclerView.setAdapter(adapter);

    // Popular games view model
    PopularGamesViewModel viewModel =
        ViewModelProviders.of(this).get(PopularGamesViewModel.class);
    viewModel.getPopularGames().observe(this, new Observer<List<GameBasic>>() {
      @Override
      public void onChanged(@Nullable List<GameBasic> gameBasics) {
        for (GameBasic game : gameBasics) {
          GameRecyclerViewAdapter.GameItem gameItem =
              new GameRecyclerViewAdapter.GameItem(adapter, game.id(), game.title(), game.url());
          popularGameItems.add(gameItem);
        }

        adapter.notifyDataSetChanged();
      }
    });

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof InteractionListener) {
      interactionListener = (InteractionListener) context;
    } else {
      Log.e("PopularGamesFrag", context.toString() + " must implement InteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    interactionListener = null;
  }


  //== Nested classes ========================================================

  public static class PopularGamesViewModel extends ViewModel {
    private MutableLiveData<List<GameBasic>> games;

    public PopularGamesViewModel() {
    }

    public LiveData<List<GameBasic>> getPopularGames() {
      if (games == null) {
        games = new MutableLiveData<>();
        loadPopularGames();
      }
      return games;
    }

    private void loadPopularGames() {
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
      ApolloQueryCall<PopularGamesQuery.Data> query =
          apolloClient.query(
              PopularGamesQuery.builder().build());
      query.enqueue(new ApolloCall.Callback<PopularGamesQuery.Data>() {
        @Override
        public void onResponse(@NotNull Response<PopularGamesQuery.Data> response) {
          List<PopularGamesQuery.Game> popularGames = response.data().games();
          List<GameBasic> gameBasicList = new ArrayList<>(popularGames.size());
          for (PopularGamesQuery.Game popularGame : popularGames) {
            GameBasic game = popularGame.fragments().gameBasic();
            gameBasicList.add(game);
          }

          games.postValue(gameBasicList);
        }

        @Override
        public void onFailure(@NotNull ApolloException e) {
          // TODO-XXXX
          Log.e("PopularGamesFrag", "onFailure", e);
        }
      });
    } // loadPopularGames()
  } // class PopularGamesViewModel

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface InteractionListener {
    void onGameItemChosen(GameRecyclerViewAdapter.GameItem item);
  }
}
