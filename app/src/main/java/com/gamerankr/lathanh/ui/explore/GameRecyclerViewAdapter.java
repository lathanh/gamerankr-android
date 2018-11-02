package com.gamerankr.lathanh.ui.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gamerankr.lathanh.databinding.GameListItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link GameItem} and makes a call to the
 * specified {@link PopularGamesFragment.InteractionListener}.
 */
public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder> {

  private final List<GameItem> gameItems;
  private final PopularGamesFragment.InteractionListener interactionListener;

  public GameRecyclerViewAdapter(List<GameItem> items, PopularGamesFragment.InteractionListener listener) {
    this.gameItems = items;
    this.interactionListener = listener;
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
    holder.gameListItemBinding.setGameItem(gameItems.get(position));
  }

  @Override
  public int getItemCount() {
    return gameItems.size();
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
  public static class GameItem {
    private final GameRecyclerViewAdapter adapter;
    public final String id;
    public final String title;
    public final String url;

    public GameItem(GameRecyclerViewAdapter adapter, String id, String title, String url) {
      this.adapter = adapter;
      this.id = id;
      this.title = title;
      this.url = url;
    }

    public void onClickListener() {
      adapter.interactionListener.onGameItemChosen(this);
    }
  }
}
