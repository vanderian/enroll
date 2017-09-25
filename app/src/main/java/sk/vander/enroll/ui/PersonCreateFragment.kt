package sk.vander.enroll.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.focusChanges
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import com.squareup.picasso.Picasso
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Maybe
import io.reactivex.Observable
import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment
import sk.vander.lib.ui.viewmodel.EventFab
import java.io.File
import java.util.*

/**
 * @author marian on 24.9.2017.
 */
class PersonCreateFragment : BaseFragment<PersonCreateViewModel, CreateState, CreateIntents>(PersonCreateViewModel::class) {
  private val dialog: Maybe<String> by lazy {
    Maybe.create<String> { emitter ->
      val cal = Calendar.getInstance()
      val d = DatePickerDialog(context,
          DatePickerDialog.OnDateSetListener { _, y, m, d -> emitter.onSuccess("$d.$m.$y") },
          cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH])
      d.setOnDismissListener { emitter.onComplete() }
      emitter.setCancellable { d.dismiss() }
      d.show()
    }
  }

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
  @BindView(R.id.view_progress) lateinit var progress: View
  override fun layout(): Int = R.layout.screen_create

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    collapsingToolbar.title = getString(R.string.label_new_person)
    toolbar.inflateMenu(R.menu.menu_detail)
  }

  override fun viewIntents(): CreateIntents = object : CreateIntents {
    override fun name(): Observable<EventName> =
        editName.afterTextChangeEvents().map { EventName(it.editable().toString()) }

    override fun surname(): Observable<EventSurname> =
        editSurname.afterTextChangeEvents().map { EventSurname(it.editable().toString()) }

    override fun date(): Observable<EventDate> =
        editDate.focusChanges().filter { it }.doOnNext { editDate.clearFocus() }
            .flatMapMaybe { dialog }.doOnNext { editDate.setText(it) }.map { EventDate(it) }

    override fun takePhoto(): Observable<Pair<File, Uri>> =
        fab.clicks().map { PersonCreateViewModel.createFiles(context) }

    override fun save(): Observable<Boolean> =
        toolbar.itemClicks().filter { it.itemId == R.id.action_save }
            .compose(RxPermissions(activity).ensure(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
            .filter { it }
  }

  override fun render(state: CreateState) {
    toolbar.menu.findItem(R.id.action_save).isVisible = state.saveEnabled
    state.photo?.apply { Picasso.with(context).load(this).into(image) }
    progress.visibility = if (state.loading) View.VISIBLE else View.GONE
  }
}