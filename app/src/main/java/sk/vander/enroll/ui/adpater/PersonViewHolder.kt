package sk.vander.enroll.ui.adpater

import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import sk.vander.enroll.db.entity.Person
import sk.vander.lib.ui.bindView
import sk.vander.lib.ui.widget.adapter.ViewHolder

/**
 * @author marian on 24.9.2017.
 */
class PersonViewHolder(root: View) : ViewHolder<PersonItem, Person>(root) {
  val text: TextView by bindView<TextView>(android.R.id.text1)

  override fun bind(item: PersonItem) {
    text.text = item.person.toString()
    disposable.addAll(
        itemView.clicks().subscribe { itemEvent.onNext(item.person) }
    )
  }
}