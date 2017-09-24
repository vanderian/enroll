package sk.vander.enroll

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.vander.enroll.ui.PersonCreateFragment
import sk.vander.enroll.ui.PersonDetailFragment
import sk.vander.enroll.ui.PersonListFragment
import sk.vander.lib.annotations.ActivityScope
import sk.vander.lib.annotations.FragmentScope
import sk.vander.lib.ui.screen.CoordinatorModule

/**
 * @author marian on 21.9.2017.
 */
object InjectorFactoriesModules {

  @Module
  abstract class Fragments {
    @FragmentScope @ContributesAndroidInjector()
    abstract fun contributePersonCreateFragment(): PersonCreateFragment

    @FragmentScope @ContributesAndroidInjector()
    abstract fun contributePersonListFragement(): PersonListFragment

    @FragmentScope @ContributesAndroidInjector()
    abstract fun contributePersonDetalFragement(): PersonDetailFragment
  }

  @Module
  abstract class Activities {
    @ActivityScope @ContributesAndroidInjector(modules = arrayOf(ActivityModules::class))
    abstract fun contributeMainActivity(): MainActivity
  }

  @Module(includes = arrayOf(
      CoordinatorModule::class,
      Fragments::class
  ))
  abstract class ActivityModules
}