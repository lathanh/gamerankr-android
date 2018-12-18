package com.gamerankr.lathanh.app.dagger2.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.gamerankr.lathanh.api.AuthenticationStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Dagger module that provides backend services.
 */
@Module
public class BackendServiceModule {

  //-- Dependencies ----------------------------------------------------------

  private final Context context;


  //== Instantiation =========================================================

  public BackendServiceModule(Context context) {
    this.context = context;
  }


  //== Instance methods ======================================================

  @Provides
  Context providesContext() {
    return context;
  }

  @Provides
  @Singleton
  SharedPreferences providesSharedPreferences(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides
  public AuthenticationStore provideAuthenticationStore(SharedPreferences sharedPreferences) {
    Log.v("BackendServiceModule", "provideAuthenticationStore()");
    return new AuthenticationStore(sharedPreferences);
  }

  @Provides
  @Singleton
  public ApolloClient provideApolloClient(AuthenticationStore authenticationStore) {
    Log.v("BackendServiceModule", "provideApolloClient()");

    String authToken = authenticationStore.getAuthenticationToken();

    OkHttpClient httpClient =
        authToken == null ?
            // Logged-out client
            new OkHttpClient() :
            // Logged-in client
            new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                  Request original = chain.request();
                  Request.Builder builder =
                      original.newBuilder().method(original.method(), original.body());
                  builder.header("api-token", authToken);
                  return chain.proceed(builder.build());
                })
                .build();

    return ApolloClient.builder()
        .serverUrl("https://www.gamerankr.com/graphql")
        .okHttpClient(httpClient)
        .logger((priority, message, t, args) ->
            Log.println(priority, "ApolloClient", String.format(message, args)))
        .build();
  }
}
