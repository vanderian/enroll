package sk.vander.enroll.ui

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.enroll.ui.adpater.PersonItem
import sk.vander.lib.ui.viewmodel.BaseViewModel
import sk.vander.lib.ui.viewmodel.Init
import sk.vander.lib.ui.viewmodel.ToFragment
import sk.vander.lib.ui.viewmodel.ViewEvent
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonListViewModel @Inject constructor(
    private val personDao: PersonDao
) : BaseViewModel<ListState>(ListState()) {

  override fun handleEvent(event: ViewEvent): Completable {
    val newState = when (event) {
      is Init -> return personDao.queryAll()
          .map { it.map { PersonItem(it) } }
          .map { ListState(it, false, it.isEmpty()) }
          .observeOn(AndroidSchedulers.mainThread())
          .doOnNext { state.onNext(it) }
          .ignoreElements()

      else -> state.value
    }
    if (state.value != newState) state.onNext(newState)

    when (event) {
      is EventFab -> navigation.onNext(ToFragment(PersonDetailFragment()))
    }

    return Completable.complete()
  }
}