package sk.vander.enroll.ui

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonListViewModel @Inject constructor() : ViewModel() {
  val state: BehaviorSubject<ViewState> = BehaviorSubject.create()

  fun bindIntents(intents: List<Observable<ViewEvent>>): Disposable =
      Observable.merge(intents)
//        .scan<State>(State.initial, { state, modifier -> modifier.invoke(state) })
          .flatMapCompletable { handleState(it) }
          .subscribe()

  private fun handleState(event: ViewEvent): Completable {
    when (event) {
      is EventFab -> state.onNext(ShowFragment(PersonDetailFragment()))
    }
    return Completable.complete()
  }
}