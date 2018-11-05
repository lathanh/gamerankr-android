package com.gamerankr.lathanh.app.glide;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

public final class GlideDataBinder {

  //== Instantiation =========================================================
  /** Non-instantiating class. */
  private GlideDataBinder() { }


  //== Static methods ========================================================

  /**
   * Allows ImageViews in layout files (XMLs) to set the image URL to load.
   */
  @BindingAdapter("imageUrl")
  public static void setImageUrl(ImageView imageView, String imageUrl) {
    Log.v("GlideDataBinder", String.format("imageView.id=%d, url=%s", imageView.getId(), imageUrl));
    GlideApp.with(imageView.getContext())
        .load(imageUrl)
        .into(imageView);
  }
}
