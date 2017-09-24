package sk.vander.enroll.ui

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment
import sk.vander.lib.ui.viewmodel.ViewEvent

/**
 * @author marian on 24.9.2017.
 */
class PersonListFragment : BaseFragment<PersonListViewModel, ListState>(PersonListViewModel::class) {
  @BindView(R.id.recycler_people) lateinit var recycler: RecyclerView
  @BindView(R.id.fab_enroll) lateinit var fab: FloatingActionButton

  override fun layout(): Int = R.layout.screen_list

  override fun intents(): List<Observable<out ViewEvent>> = listOf(
      fab.clicks().map { EventFab }
  )

  override fun render(state: ListState) {
  }

}