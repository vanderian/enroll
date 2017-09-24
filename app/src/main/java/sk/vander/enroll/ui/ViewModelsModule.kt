package sk.vander.enroll.ui

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.vander.lib.annotations.ViewModelKey

/**
 * @author marian on 21.9.2017.
 */
@Module
abstract class ViewModelsModule {

  @Binds @IntoMap @ViewModelKey(PersonListViewModel::class)
  abstract fun providePersonListViewModel(viewModel: PersonListViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(PersonCreateViewModel::class)
  abstract fun providePersonCreateViewModel(viewModel: PersonCreateViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(PersonDetailViewModel::class)
  abstract fun providePersonDetailViewModel(viewModel: PersonDetailViewModel): ViewModel

}