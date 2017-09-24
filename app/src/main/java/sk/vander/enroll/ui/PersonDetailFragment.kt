package sk.vander.enroll.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.focusChanges
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import com.squareup.picasso.Picasso
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.subjects.BehaviorSubject
import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment
import java.util.*

/**
 * @author marian on 24.9.2017.
 */
class PersonDetailFragment : BaseFragment<PersonDetailViewModel>(PersonDetailViewModel::class) {
  @BindView(R.id.collapsing_toolbar) lateinit var collapsingToolbar: CollapsingToolbarLayout
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.toolbar_image_person) lateinit var image: ImageView
  @BindView(R.id.toolbar_fab) lateinit var fab: FloatingActionButton
  @BindView(R.id.input_name) lateinit var inputName: TextInputLayout
  @BindView(R.id.input_surname) lateinit var inputSurname: TextInputLayout
  @BindView(R.id.input_date) lateinit var inputDate: TextInputLayout
  @BindView(R.id.input_edit_name) lateinit var editName: TextInputEditText
  @BindView(R.id.input_edit_surname) lateinit var editSurname: TextInputEditText
  @BindView(R.id.input_edit_date) lateinit var editDate: TextInputEditText
  private val activityResult = BehaviorSubject.create<ActivityResult>()

  private fun intents(): List<Observable<out ViewEvent>> {
    val dialog = Maybe.create<String> { emitter ->
      val cal = Calendar.getInstance()
      val d = DatePickerDialog(context,
          DatePickerDialog.OnDateSetListener { _, y, m, d -> emitter.onSuccess("$d.$m.$y") },
          cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH])
      d.setOnDismissListener { emitter.onComplete() }
      emitter.setCancellable { d.dismiss() }
      d.show()
    }
    val name = editName.afterTextChangeEvents().share()
    val surname = editSurname.afterTextChangeEvents().share()
    return listOf<Observable<out ViewEvent>>(
        toolbar.itemClicks().filter { it.itemId == R.id.action_save }
            .map { EventForm(editName.text.toString(), editSurname.text.toString(), editDate.text.toString()) },
        name.map { it.editable().isNullOrEmpty() }
            .zipWith(surname.map { it.editable().isNullOrEmpty() },
                { nameEmpty, surnameEmpty -> EventHasText(!nameEmpty && !surnameEmpty) }),
        editDate.focusChanges().filter { it }.doOnNext { editDate.clearFocus() }.flatMapMaybe { dialog }.map { EventDate(it) },
        fab.clicks().map { PersonDetailViewModel.createFiles(context) },
        activityResult
    )
  }

  private fun render(state: DetailState) {
    toolbar.menu.findItem(R.id.action_save).isVisible = state.saveEnabled
    state.photo?.apply { Picasso.with(context).load(this).into(image) }
    editDate.setText(state.date)
  }

  private fun navigate(navigation: Navigation) {
    when (navigation) {
      is GoBack -> activity.onBackPressed()
      is ToFragment -> activity.supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, navigation.fragment)
          .addToBackStack("")
          .commit()
      is ToActivityResult ->
        if (navigation.intent.resolvable(context)) {
          startActivityForResult(navigation.intent, navigation.requestCode)
        } else {
          Toast.makeText(context, context.getString(R.string.no_app_error, navigation.intent), Toast.LENGTH_SHORT).show()
          activityResult.onNext(ActivityResult(navigation.requestCode))
        }
    }
  }

  override fun layout(): Int = R.layout.screen_detail

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    collapsingToolbar.title = getString(R.string.label_new_person)
    toolbar.inflateMenu(R.menu.menu_detail)
  }

  override fun onStart() {
    super.onStart()
    disposable.addAll(
        viewModel.bindIntents(intents()),
        viewModel.state.subscribe { render(it) },
        viewModel.navigation.subscribe { navigate(it) }
    )
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    activityResult.onNext(ActivityResult(requestCode, resultCode, data))
  }
}