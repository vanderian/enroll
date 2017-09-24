package sk.vander.enroll.ui.adpater

import sk.vander.enroll.db.entity.Person
import sk.vander.lib.ui.widget.adapter.AdapterModel

/**
 * @author marian on 24.9.2017.
 */
data class PersonItem(val person: Person): AdapterModel {
  override val layoutRes: Int = android.R.layout.simple_list_item_1
  override val id: Long = person.id
}