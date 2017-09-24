package sk.vander.enroll

import android.os.Bundle
import sk.vander.enroll.ui.PersonListFragment
import sk.vander.lib.ui.FragmentActivity

/**
 * @author marian on 5.9.2017.
 */
class MainActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container_id, PersonListFragment())
          .commit()
    }
  }
}