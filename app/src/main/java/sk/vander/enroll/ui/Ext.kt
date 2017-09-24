package sk.vander.enroll.ui

import android.net.Uri
import android.view.View
import sk.vander.enroll.db.entity.Person
import sk.vander.enroll.ui.adpater.PersonItem
import sk.vander.lib.ui.viewmodel.ViewEvent
import sk.vander.lib.ui.viewmodel.ViewState
import java.io.File

/**
 * @author marian on 24.9.2017.
 */


//list
object EventFab : ViewEvent

data class EventPerson(val person: Person) : ViewEvent
data class ListState(
    val items: List<PersonItem> = emptyList(),
    val loading: Boolean = true,
    val empty: Boolean = false

) : ViewState

//detail
data class EventHasText(val has: Boolean) : ViewEvent

data class EventForm(val name: String, val surname: String, val date: String) : ViewEvent
data class EventName(val text: String) : ViewEvent
data class EventSurname(val text: String) : ViewEvent
data class EventDate(val text: String) : ViewEvent
data class EventFiles(val file: File, val uri: Uri) : ViewEvent

data class DetailState(
    val name: String = "",
    val surname: String = "",
    val date: String = "",
    val photo: Uri? = null,
    val saveEnabled: Boolean = false,
    val loading: Boolean = false
) : ViewState

//misc
fun Boolean.visibility() = if (this) View.VISIBLE else View.GONE