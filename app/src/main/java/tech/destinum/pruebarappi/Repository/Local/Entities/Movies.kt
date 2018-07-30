package tech.destinum.pruebarappi.Repository.Local.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(@PrimaryKey(autoGenerate = false)
                 var movie_id: Int,
                 var title: String,
                 var vote_average: Double,
                 var poster_path: String,
                 var overview: String,
                 var released_date: String)