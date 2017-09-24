package sk.vander.enroll

import android.content.Context
import com.patloew.rxlocation.RxLocation
import dagger.Provides
import sk.vander.lib.BaseApp
import sk.vander.lib.BaseAppModule
import sk.vander.lib.annotations.ApplicationScope

/**
 * @author marian on 5.9.2017.
 */
@BuildTypeComponent
abstract class App : BaseApp() {
  override fun buildComponentAndInject() = Initializer.init(this).inject(this)

  @dagger.Module(includes = arrayOf(BaseAppModule::class))
  object Module {
    @JvmStatic @Provides @ApplicationScope
    fun provideRxLocation(context: Context): RxLocation = RxLocation(context)
  }
}