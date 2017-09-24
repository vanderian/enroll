package sk.vander.enroll.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import sk.vander.enroll.db.entity.Person
import java.io.File

/**
 * @author marian on 24.9.2017.
 */
interface Navigation
interface ViewState
interface ViewEvent

object GoBack : Navigation
data class ToFragment(val fragment: Fragment) : Navigation
data class ToActivityResult(val intent: Intent, val requestCode: Int) : Navigation

data class ActivityResult(
    val request: Int,
    val result: Int = Activity.RESULT_CANCELED,
    val data: Intent? = null
): ViewEvent

//list
object EventFab
data class EventPerson(val person: Person)
data class ListState(
    val fragment: Fragment? = null
)

//detail
data class EventHasText(val has: Boolean): ViewEvent
data class EventForm(val name: String, val surname: String, val date: String): ViewEvent
data class EventName(val text: String): ViewEvent
data class EventSurname(val text: String): ViewEvent
data class EventDate(val text: String): ViewEvent
data class EventFiles(val file: File, val uri: Uri): ViewEvent

data class DetailState(
    val name: String = "",
    val surname: String = "",
    val date: String = "",
    val photo: Uri? = null,
    val saveEnabled: Boolean = false,
    val photoState: PhotoState? = null
)

data class PhotoState(
    val file: File,
    val uri: Uri,
    val intent: Pair<Intent, Int>? = null
)

//misc
fun Intent.resolvable(context: Context) = this.resolveActivity(context.packageManager) != null
