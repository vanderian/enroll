package sk.vander.enroll.ui

import io.reactivex.Completable
import sk.vander.lib.ui.viewmodel.BaseViewModel
import sk.vander.lib.ui.viewmodel.ToFragment
import sk.vander.lib.ui.viewmodel.ViewEvent
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonListViewModel @Inject constructor() : BaseViewModel<ListState>() {

  override fun handleEvent(event: ViewEvent): Completable {
    val newState = when (event) {
      else -> state.value
    }
    if (state.value != newState) state.onNext(newState)

    when (event) {
      is EventFab -> navigation.onNext(ToFragment(PersonDetailFragment()))
    }

    return Completable.complete()
  }
}