package sk.vander.enroll.ui

import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.widget.ImageView
import butterknife.BindView
import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment

/**
 * @author marian on 24.9.2017.
 */
class PersonDetailFragment : BaseFragment<PersonDetailViewModel>(PersonDetailViewModel::class) {
  @BindView(R.id.collapsing_toolbar) lateinit var collapsingToolbar: CollapsingToolbarLayout
  @BindView(R.id.toolbar_image_person) lateinit var image: ImageView
  @BindView(R.id.input_name) lateinit var inputName: TextInputLayout
  @BindView(R.id.input_surname) lateinit var inputSurname: TextInputLayout
  @BindView(R.id.input_date) lateinit var inputDate: TextInputLayout
  @BindView(R.id.input_edit_name) lateinit var editName: TextInputEditText
  @BindView(R.id.input_edit_surname) lateinit var editSurname: TextInputEditText
  @BindView(R.id.input_edit_date) lateinit var editDate: TextInputEditText

  override fun layout(): Int = R.layout.screen_detail
}