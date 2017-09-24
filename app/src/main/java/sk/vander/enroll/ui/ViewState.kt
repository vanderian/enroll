package sk.vander.enroll.ui

import android.support.v4.app.Fragment
import sk.vander.enroll.db.entity.Person

/**
 * @author marian on 24.9.2017.
 */
interface ViewState

//typealias StateModifier = (ViewState) -> ViewState

//data class ShowFragment(val fragment: Fragment): ViewState<ItemClickPerson>
data class ShowFragment(val fragment: Fragment): ViewState

interface ViewEvent
class EventFab: ViewEvent
data class EventPerson(val person: Person): ViewEvent
