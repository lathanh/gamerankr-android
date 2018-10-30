package com.gamerankr.lathanh.ui.explore;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gamerankr.lathanh.databinding.GameListItemBinding;
import com.gamerankr.lathanh.ui.explore.PopularGamesFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link GameItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder> {

  private final List<GameItem> mValues;
  private final OnListFragmentInteractionListener mListener;

  public GameRecyclerViewAdapter(List<GameItem> items, OnListFragmentInteractionListener listener) {
    mValues = items;
    mListener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    GameListItemBinding gameListItemBinding =
        GameListItemBinding.inflate(LayoutInflater.from(parent.getContext()));

    return new ViewHolder(gameListItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    holder.gameListItemBinding.setGameItem(mValues.get(position));
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private final GameListItemBinding gameListItemBinding;

    ViewHolder(GameListItemBinding gameListItemBinding) {
      super(gameListItemBinding.getRoot());
      this.gameListItemBinding = gameListItemBinding;
    }
  }

  /**
   * A dummy item representing a piece of content.
   */
  public static class GameItem extends ViewModel {
    public final String id;
    public final String title;
    public final String url;

    public GameItem(String id, String title, String url) {
      this.id = id;
      this.title = title;
      this.url = url;
    }
  }
}
