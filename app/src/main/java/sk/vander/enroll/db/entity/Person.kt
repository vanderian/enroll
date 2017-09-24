package sk.vander.enroll.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author marian on 24.9.2017.
 */
@Entity
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long
)