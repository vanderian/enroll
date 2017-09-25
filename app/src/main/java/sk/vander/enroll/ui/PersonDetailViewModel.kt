package sk.vander.enroll.ui

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.lib.ui.viewmodel.ActivityResult
import sk.vander.lib.ui.viewmodel.BaseViewModel
import sk.vander.lib.ui.viewmodel.EventFab
import sk.vander.lib.ui.viewmodel.GoBack
import javax.inject.Inject

/**
 * @author marian on 25.9.2017.
 */
class PersonDetailViewModel @Inject constructor(
    private val personDao: PersonDao
) : BaseViewModel<DetailState, DetailIntents>(DetailState()) {

  override fun collectIntents(intents: DetailIntents, activityResult: Observable<ActivityResult>): Disposable =
      Observable.merge(intents.args(), intents.delete())
          .flatMapCompletable { handleEvent(it) }
          .subscribe()

  fun handleEvent(event: Any): Completable {
    return when (event) {
      is Long -> personDao.queryOne(event)
          .subscribeOn(Schedulers.io())
          .map { DetailState(it) }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSuccess { state.onNext(it) }
          .toCompletable()

      is EventFab -> Completable.fromAction { personDao.delete(state.value.person!!) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnComplete { navigation.onNext(GoBack) }

      else -> Completable.complete()
    }
  }
}
