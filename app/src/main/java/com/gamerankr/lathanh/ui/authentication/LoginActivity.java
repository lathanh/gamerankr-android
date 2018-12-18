package com.gamerankr.lathanh.ui.authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.gamerankr.lathanh.R;
import com.gamerankr.lathanh.api.AuthenticationStore;
import com.gamerankr.lathanh.app.dagger2.components.ControllerComponent;
import com.gamerankr.lathanh.app.dagger2.components.DaggerControllerComponent;
import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import javax.inject.Inject;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

/**
 * A UI for logging in.
 */
public class LoginActivity extends AppCompatActivity {

  //== Instance fields ========================================================

  //-- Dependencies -----------------------------------------------------------

  @Inject
  AuthenticationStore authenticationStore;

  //-- Operating fields -------------------------------------------------------

  //-- UI references.
  private LoginButton fbLoginButton;

  private CallbackManager fbCallbackManager;


  //== Instance methods =======================================================

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    // Dependencies
    //todo-xxx: replace with Dagger Android injection
    ControllerComponent component =
        DaggerControllerComponent.builder()
            .backendServiceModule(new BackendServiceModule(this))
            .build();
    component.inject(this);

    // Facebook login
    fbLoginButton = findViewById(R.id.login_button);
    fbLoginButton.setReadPermissions(Arrays.asList(EMAIL));
    fbCallbackManager = CallbackManager.Factory.create();

    // There might already be a Facebook token available to us.
    checkFbAccessToken("onCreate");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (fbCallbackManager.onActivityResult(requestCode, resultCode, data)) {
      checkFbAccessToken("onActivityResult");
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  //-- Private methods --------------------------------------------------------

  private void checkFbAccessToken(String source) {
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    Log.v("LoginActivity",
        String.format("checkFbAccessToken(%s): accessToken=%s, isLoggedIn=%b", source, accessToken,
            isLoggedIn));

    if (isLoggedIn) {
      ProfilePictureView profilePictureView = findViewById(R.id.fb_profile_picture);
      profilePictureView.setProfileId(accessToken.getUserId());

      new GameRankrFacebookLogin().execute(accessToken);
    }
  }

  //== Nested classes =========================================================

  private class GameRankrFacebookLogin extends AsyncTask<AccessToken, Void, String> {
    @Override
    protected String doInBackground(AccessToken... accessTokens) {
      URL url;
      try {
        url =
            new URL("https://www.gamerankr.com/login.json?fb_auth_token=" +
                accessTokens[0].getToken());
      } catch (MalformedURLException e) {
        Log.e("LoginActivity", "GameRankrFacebookLogin", e);
        return null;
      }

      //todo-xxx: more gracefully extract token from response
      InputStreamReader inputStreamReader;
      try {
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
      } catch (IOException e) {
        Log.e("LoginActivity", "GameRankrFacebookLogin", e);
        return null;
      }

      JsonReader jsonReader = new JsonReader(inputStreamReader);
      try {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
          String name = jsonReader.nextName();

          if (name.equals("token")) {
            String token = jsonReader.nextString();
            Log.v("LoginActivity", "token=" + token);

            authenticationStore.setAuthenticationToken(token);
            return token;
          } else {
            Log.v("LoginActivity", "skipping element; name=" + name);
          }
          jsonReader.nextString();
        }
      } catch (IOException e) {
        Log.e("LoginActivity", "GameRankrFacebookLogin", e);
        return null;
      }
      Log.v("LoginActivity", "token not found");

      return null;
    } // doInBackground()

    @Override
    protected void onPostExecute(String token) {
      if (token != null) {
        setResult(AppCompatActivity.RESULT_OK);
        finish();
      }
    }
  } // class GameRankrFacebookLogin
}
