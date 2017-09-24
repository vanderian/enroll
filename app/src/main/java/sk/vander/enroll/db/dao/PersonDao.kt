package sk.vander.enroll.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import sk.vander.enroll.db.entity.Person

/**
 * @author marian on 24.9.2017.
 */
@Dao
interface PersonDao {

  @Insert
  fun insert(person: Person)

  @Delete
  fun delete(person: Person)

  @Query("SELECT * FROM people")
  fun queryAll(): Flowable<List<Person>>

  @Query("SELECT * FROM people WHERE id = :id")
  fun queryOne(id: Long): Single<Person>
}