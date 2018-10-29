package com.gamerankr.lathanh.ui.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gamerankr.lathanh.R;
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
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.game_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    holder.mItem = mValues.get(position);
    holder.mIdView.setText(mValues.get(position).id);
    holder.mContentView.setText(mValues.get(position).content);

    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mListener) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(holder.mItem);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    final View mView;
    final TextView mIdView;
    final TextView mContentView;
    GameItem mItem;

    ViewHolder(View view) {
      super(view);
      mView = view;
      mIdView = view.findViewById(R.id.item_number);
      mContentView = (TextView) view.findViewById(R.id.content);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }

  /**
   * A dummy item representing a piece of content.
   */
  public static class GameItem {
    public final String id;
    public final String content;
    public final String details;

    public GameItem(String id, String content, String details) {
      this.id = id;
      this.content = content;
      this.details = details;
    }

    @Override
    public String toString() {
      return content;
    }
  }

}
