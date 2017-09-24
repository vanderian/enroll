package sk.vander.enroll.ui

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment
import sk.vander.lib.ui.viewmodel.ViewEvent

/**
 * @author marian on 25.9.2017.
 */
class PersonDetailFragment : BaseFragment<PersonDetailViewModel, DetailState>(PersonDetailViewModel::class) {
  @BindView(R.id.collapsing_toolbar) lateinit var collapsingToolbar: CollapsingToolbarLayout
  @BindView(R.id.toolbar_image_person) lateinit var image: ImageView
  @BindView(R.id.toolbar_fab) lateinit var fab: FloatingActionButton
  @BindView(R.id.text_info) lateinit var textInfo: TextView
  @BindView(R.id.view_progress) lateinit var progress: View

  override fun layout(): Int = R.layout.screen_detail

  override fun intents(): List<Observable<out ViewEvent>> = listOf(
      fab.clicks().map { EventDelete },
      Observable.just(EventId(arguments.getLong(ARG_ID)))
  )

  override fun render(state: DetailState) {
    val hasData = (state.person != null)
    fab.visibility = hasData.visibility()
    progress.visibility = hasData.not().visibility()
    state.person?.let {
      textInfo.text = it.toString()
      collapsingToolbar.title = "${it.firstName} ${it.lastName}"
      it.photo?.let { Picasso.with(context).load(it).into(image) }
    }
  }

  companion object {
    const val ARG_ID = "arg_id"
    fun newInstance(id: Long) = PersonDetailFragment().apply { arguments = Bundle().apply { putLong(ARG_ID, id) } }
  }
}