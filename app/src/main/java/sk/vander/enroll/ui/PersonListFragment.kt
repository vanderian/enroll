package sk.vander.enroll.ui

import sk.vander.enroll.R
import sk.vander.lib.ui.BaseFragment

/**
 * @author marian on 24.9.2017.
 */
class PersonListFragment: BaseFragment<PersonListViewModel>(PersonListViewModel::class) {
  override fun layout(): Int = R.layout.screen_list
}