package sk.vander.lib.ui.viewmodel

import android.content.Intent
import android.support.v4.app.Fragment

/**
 * @author marian on 24.9.2017.
 */
interface Navigation

object GoBack : Navigation
data class ToFragment(val fragment: Fragment) : Navigation
data class ToActivityResult(val intent: Intent, val requestCode: Int) : Navigation
