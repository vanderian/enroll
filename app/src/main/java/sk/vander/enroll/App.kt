package sk.vander.enroll

import android.content.Context
import android.os.Build
import com.patloew.rxlocation.RxLocation
import dagger.Provides
import sk.vander.lib.BaseApp
import sk.vander.lib.BaseAppModule
import sk.vander.lib.annotations.ApplicationScope
import java.lang.Exception
import java.util.*


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

    @JvmStatic @Provides @ApplicationScope
    fun provideUUID(context: Context): UUID {
      val id = "35" + (Build.BOARD.length % 10)
      +(Build.BRAND.length % 10)
      +(Build.CPU_ABI.length % 10)
      +(Build.DEVICE.length % 10)
      +(Build.MANUFACTURER.length % 10)
      +(Build.MODEL.length % 10)
      +(Build.PRODUCT.length % 10)

      val serial: String =
          try {
            android.os.Build::class.java.getField("SERIAL").get(null).toString()
          } catch (ex: Exception) {
            "serial"
          }

      return UUID(id.hashCode().toLong(), serial.hashCode().toLong())
    }
  }
}