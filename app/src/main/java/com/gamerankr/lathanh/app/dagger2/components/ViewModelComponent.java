package com.gamerankr.lathanh.app.dagger2.components;

import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.lathanh.ui.explore.PopularGamesFragment;
import com.gamerankr.lathanh.ui.game.GameViewModel;
import com.gamerankr.lathanh.ui.me.MyGamesFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for providing dependencies to ViewModels.
 */
@Singleton
@Component(modules = { BackendServiceModule.class })
public interface ViewModelComponent {

  void inject(GameViewModel gameViewModel);

  void inject(PopularGamesFragment.PopularGamesViewModel popularGamesViewModel);

  void inject(MyGamesFragment.MyGamesViewModel myGamesViewModel);

}
