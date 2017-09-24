package sk.vander.enroll.ui

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.lib.ui.viewmodel.BaseViewModel
import sk.vander.lib.ui.viewmodel.GoBack
import sk.vander.lib.ui.viewmodel.ViewEvent
import javax.inject.Inject

/**
 * @author marian on 25.9.2017.
 */
class PersonDetailViewModel @Inject constructor(
    private val personDao: PersonDao
) : BaseViewModel<DetailState>(DetailState()) {

  override fun handleEvent(event: ViewEvent): Completable {
    return when (event) {
      is EventId -> personDao.queryOne(event.id)
          .subscribeOn(Schedulers.io())
          .map { DetailState(it) }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSuccess { state.onNext(it) }
          .toCompletable()

      is EventDelete -> Completable.fromAction { personDao.delete(state.value.person!!) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnComplete { navigation.onNext(GoBack) }

      else -> Completable.complete()
    }
  }
}