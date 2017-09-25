package sk.vander.enroll.ui

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.enroll.ui.adpater.PersonItem
import sk.vander.lib.ui.viewmodel.ActivityResult
import sk.vander.lib.ui.viewmodel.BaseViewModel
import sk.vander.lib.ui.viewmodel.ToFragment
import javax.inject.Inject

/**
 * @author marian on 24.9.2017.
 */
class PersonListViewModel @Inject constructor(
    private val personDao: PersonDao
) : BaseViewModel<ListState, ListIntents>(ListState()) {

  override fun collectIntents(intents: ListIntents, activityResult: Observable<ActivityResult>): Disposable =
      Observable.merge(
          Observable.merge(
              intents.create().map { PersonCreateFragment() },
              intents.itemClick().map { PersonDetailFragment.newInstance(it.id) }
          ).doOnNext { navigation.onNext(ToFragment(it)) },
          database().toObservable()
      ).subscribe()

  fun database(): Flowable<ListState> = personDao.queryAll()
      .map { it.map { PersonItem(it) } }
      .map { ListState(it, false, it.isEmpty()) }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnNext { state.onNext(it) }
}