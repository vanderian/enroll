package sk.vander.enroll.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import sk.vander.enroll.R
import sk.vander.enroll.db.entity.Person
import sk.vander.enroll.ui.adpater.PersonItem
import sk.vander.enroll.ui.adpater.PersonViewHolder
import sk.vander.lib.ui.BaseFragment
import sk.vander.lib.ui.viewmodel.EventFab
import sk.vander.lib.ui.widget.adapter.ListSource
import sk.vander.lib.ui.widget.adapter.RecyclerAdapter

/**
 * @author marian on 24.9.2017.
 */
class PersonListFragment : BaseFragment<PersonListViewModel, ListState, ListIntents>(PersonListViewModel::class) {
  private val source = ListSource<PersonItem>()
  private val adapter = RecyclerAdapter(source, { _, root -> PersonViewHolder(root) }).apply { setHasStableIds(true) }
  @BindView(R.id.recycler_people) lateinit var recycler: RecyclerView
  @BindView(R.id.fab_enroll) lateinit var fab: FloatingActionButton
  @BindView(R.id.view_progress) lateinit var progress: View
  @BindView(R.id.view_empty) lateinit var empty: View
  override fun layout(): Int = R.layout.screen_list
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recycler.layoutManager = LinearLayoutManager(context)
    recycler.adapter = adapter
  }

  override fun viewIntents(): ListIntents = object : ListIntents {
    override fun create(): Observable<EventFab> = fab.clicks().map { EventFab }
    override fun itemClick(): Observable<Person> = adapter.itemEventSource.toObservable()
  }

  override fun render(state: ListState) {
    progress.visibility = state.loading.visibility()
    empty.visibility = state.empty.visibility()
    source.setList(state.items)
  }

}