package sk.vander.enroll.ui

import android.net.Uri
import android.view.View
import io.reactivex.Observable
import sk.vander.enroll.db.entity.Person
import sk.vander.enroll.ui.adpater.PersonItem
import sk.vander.lib.ui.viewmodel.EventFab
import sk.vander.lib.ui.viewmodel.ViewIntents
import sk.vander.lib.ui.viewmodel.ViewState
import java.io.File

/**
 * @author marian on 24.9.2017.
 */


//list
data class ListState(
    val items: List<PersonItem> = emptyList(),
    val loading: Boolean = true,
    val empty: Boolean = false
) : ViewState

interface ListIntents: ViewIntents {
  fun create(): Observable<EventFab>
  fun itemClick(): Observable<Person>
}

//create
data class EventName(val text: String)
data class EventSurname(val text: String)
data class EventDate(val text: String)

interface CreateIntents: ViewIntents {
  fun takePhoto(): Observable<Pair<File, Uri>>
  fun save(): Observable<Boolean>
  fun name(): Observable<EventName>
  fun surname(): Observable<EventSurname>
  fun date(): Observable<EventDate>
}

data class CreateState(
    val name: String = "",
    val surname: String = "",
    val date: String = "",
    val photo: Uri? = null,
    val saveEnabled: Boolean = false,
    val loading: Boolean = false
) : ViewState

//detail
interface DetailIntents: ViewIntents {
  fun delete(): Observable<EventFab>
  fun args(): Observable<Long>
}

data class DetailState(
    val person: Person? = null
): ViewState

//misc
fun Boolean.visibility() = if (this) View.VISIBLE else View.GONE