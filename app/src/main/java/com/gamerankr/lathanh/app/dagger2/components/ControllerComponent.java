package com.gamerankr.lathanh.app.dagger2.components;

import com.gamerankr.lathanh.MainActivity;
import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.lathanh.ui.authentication.LoginActivity;
import com.gamerankr.lathanh.ui.me.MeActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for providing dependencies to controllers (namely, Activities and Fragments).
 */
@Singleton
@Component(modules = { BackendServiceModule.class })
public interface ControllerComponent {

  void inject(MainActivity mainActivity);

  void inject(MeActivity meActivity);

  void inject(LoginActivity loginActivity);

}
