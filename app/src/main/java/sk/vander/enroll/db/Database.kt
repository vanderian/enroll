package sk.vander.enroll.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import sk.vander.enroll.db.dao.PersonDao
import sk.vander.enroll.db.entity.Person

/**
 * @author marian on 21.9.2017.
 */
@Database(entities = arrayOf(Person::class), version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
  abstract fun personDao(): PersonDao
}