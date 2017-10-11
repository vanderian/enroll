package sk.vander.enroll.ui.screen

import com.patloew.rxlocation.RxLocation
import org.amshove.kluent.`should be true`
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.enroll.ui.PersonCreateViewModel
import java.util.*

/**
 * @author marian on 28.9.2017.
 */
//@RunWith(JUnitPlatform::class)
class PersonCreateViewModelTest : Spek({
  given("a view model") {
    val location: RxLocation = mock()
    val personDao: PersonDao = mock()
    val uuid = UUID.randomUUID()
    val viewModel = PersonCreateViewModel(personDao, location, uuid)

    on("just success") gk g,                                                       {
      it("should be ok") {
        true.`should be true`()
      }
    }
  }
})