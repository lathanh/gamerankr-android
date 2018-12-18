package com.gamerankr.lathanh.api;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Store for GameRankr API authentication credentials.
 */
public class AuthenticationStore {

  //-- Dependencies -----------------------------------------------------------

  private final SharedPreferences sharedPreferences;


  //== Instantiation ==========================================================

  public AuthenticationStore(SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
  }


  //== Instance methods =======================================================

  /**
   * @return The authentication token for the current user; {@code null} if there is no current
   *         user (i.e., the user is not signed in).
   */
  public String getAuthenticationToken() {
    return sharedPreferences.getString("auth.token", null);
  }


  /**
   * Store the authentication token for the current user (effectively "login").
   * @param authToken Authentication token for the current user; or {@code null} to clear out the
   *        token (effectively "logout")
   */
  public void setAuthenticationToken(String authToken) {
    Log.v("AuthStore", "Saving authToken=" + authToken);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("auth.token", authToken);
    editor.apply();
  }
}
