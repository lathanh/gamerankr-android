package com.gamerankr.lathanh.app.dagger2.modules;

import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.Logger;
import com.apollographql.apollo.api.internal.Optional;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BackendServiceModule {

  @Provides
  @Singleton
  public ApolloClient provideApolloClient() {
    return ApolloClient.builder()
        .serverUrl("https://www.gamerankr.com/graphql")
        .logger(new Logger() {
          @Override
          public void log(int priority, @NotNull String message,
                          @NotNull Optional<Throwable> t, @NotNull Object... args) {
            Log.println(priority, "ApolloClient", String.format(message, args));
          }
        })
        .build();
  }
}
