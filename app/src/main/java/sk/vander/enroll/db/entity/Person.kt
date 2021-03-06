package sk.vander.enroll.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.location.Location
import android.net.Uri
import sk.vander.enroll.ui.CreateState
import java.util.*

/**
 * @author marian on 24.9.2017.
 */
@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val date: String?,
    val photo: Uri?,
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val deviceId: String
) {
  constructor(state: CreateState, loc: Location, uuid: UUID) :
      this(0, state.name, state.surname, state.date, state.photo, System.currentTimeMillis(),
          loc.latitude, loc.longitude, uuid.toString())
}