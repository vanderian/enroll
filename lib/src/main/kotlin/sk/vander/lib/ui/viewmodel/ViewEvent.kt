package sk.vander.lib.ui.viewmodel

import android.app.Activity
import android.content.Intent

/**
 * @author marian on 24.9.2017.
 */
interface ViewEvent

object Init : ViewEvent

data class ActivityResult(
    val request: Int,
    val result: Int = Activity.RESULT_CANCELED,
    val data: Intent? = null
) : ViewEvent
