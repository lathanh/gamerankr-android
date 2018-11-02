package com.gamerankr.lathanh.ui.game;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamerankr.lathanh.databinding.GameFragmentBinding;
import com.gamerankr.queries.GameQuery;

public class GameFragment extends Fragment {

  private GameViewModel gameViewModel;

  private GameFragmentBinding gameFragmentBinding;


  //== Instantiation =========================================================

  public static GameFragment newInstance(String gameId) {
    Bundle arguments = new Bundle();
    arguments.putString("GAME_ID", gameId);

    GameFragment gameFragment = new GameFragment();
    gameFragment.setArguments(arguments);
    return gameFragment;
  }

  //== Instance methods ======================================================

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    gameFragmentBinding = GameFragmentBinding.inflate(inflater, container, false);
    return gameFragmentBinding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    String gameId = getArguments().getString("GAME_ID");
    gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
    gameViewModel.getGame(gameId).observe(this, new Observer<GameQuery.Game>() {
      @Override
      public void onChanged(@Nullable GameQuery.Game game) {
        gameFragmentBinding.setGame(game.fragments().gameBasic());
      }
    });
  }
}
