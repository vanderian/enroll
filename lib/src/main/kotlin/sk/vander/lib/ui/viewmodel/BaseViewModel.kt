package sk.vander.lib.ui.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * @author marian on 24.9.2017.
 */
abstract class BaseViewModel<T>: ViewModel() {
  val state: BehaviorSubject<T> = BehaviorSubject.create()
  val navigation: PublishSubject<Navigation> = PublishSubject.create()

  abstract fun handleEvent(event: ViewEvent): Completable

  fun bindIntents(intents: List<Observable<out ViewEvent>>): Disposable =
      Observable.merge(intents)
          .flatMapCompletable { handleEvent(it) }
          .subscribe()
}