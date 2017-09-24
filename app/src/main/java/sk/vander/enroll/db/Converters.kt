package sk.vander.enroll.db

import android.arch.persistence.room.TypeConverter
import android.net.Uri

/**
 * @author marian on 24.9.2017.
 */
object Converters {
  @JvmStatic @TypeConverter
  fun uriToString(uri: Uri?): String = uri.toString()

  @JvmStatic @TypeConverter
  fun stringToUri(uri: String): Uri? = Uri.parse(uri)
}