package sk.vander.enroll.ui

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonListViewModel @Inject constructor() : ViewModel() {
  val state: BehaviorSubject<ListState> = BehaviorSubject.create()
  val navigation: PublishSubject<Navigation> = PublishSubject.create()

  fun bindIntents(intents: List<Observable<out Any>>): Disposable =
      Observable.merge(intents)
          .flatMapCompletable { handleState(it) }
          .subscribe()


  private fun handleState(event: Any): Completable {
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