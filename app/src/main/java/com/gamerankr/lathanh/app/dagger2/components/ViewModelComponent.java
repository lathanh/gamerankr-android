package com.gamerankr.lathanh.app.dagger2.components;

import com.gamerankr.lathanh.app.dagger2.modules.BackendServiceModule;
import com.gamerankr.lathanh.ui.explore.PopularGamesFragment;
import com.gamerankr.lathanh.ui.game.GameViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { BackendServiceModule.class })
public interface ViewModelComponent {

  void inject(GameViewModel gameViewModel);

  void inject(PopularGamesFragment.PopularGamesViewModel popularGamesViewModel);

}
