package sk.vander.enroll.ui

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment

/**
 * @author marian on 24.9.2017.
 */
class PersonListFragment : BaseFragment<PersonListViewModel>(PersonListViewModel::class) {
  @BindView(R.id.recycler_people) lateinit var recycler: RecyclerView
  @BindView(R.id.fab_enroll) lateinit var fab: FloatingActionButton

  override fun layout(): Int = R.layout.screen_list

  private fun intents() = listOf<Observable<out Any>>(
      fab.clicks().map { EventFab }
  )

  private fun render(state: ListState) {
  }

  private fun navigate(navigation: Navigation) {
    when (navigation) {
      is GoBack -> activity.onBackPressed()
      is ToFragment -> activity.supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, navigation.fragment)
          .addToBackStack("")
          .commit()

    }
  }

  override fun onStart() {
    super.onStart()
    disposable.addAll(
        viewModel.bindIntents(intents()),
        viewModel.state.subscribe { render(it) },
        viewModel.navigation.subscribe { navigate(it) }
    )
  }

}