package sk.vander.enroll.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.lib.annotations.ApplicationScope

/**
 * @author marian on 21.9.2017.
 */
@Module
object DataModule {

  @JvmStatic @Provides @ApplicationScope
  fun provideDatabase(context: Context): Database =
      Room.databaseBuilder(context, Database::class.java, "data.db")
          .build()

  @JvmStatic @Provides @ApplicationScope
  fun providePersonDao(db: Database): PersonDao = db.personDao()

}

