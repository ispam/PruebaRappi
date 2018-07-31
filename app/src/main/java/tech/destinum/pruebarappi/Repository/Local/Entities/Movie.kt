package tech.destinum.pruebarappi.Repository.Local.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "movies")
@Parcelize
data class Movie(
        @SerializedName("vote_count")
        @Expose
        var voteCount: Int? = null,
        @SerializedName("id")
        @Expose
        @PrimaryKey(autoGenerate = false)
        var id: Int? = null,
        @SerializedName("video")
        @Expose
        var video: Boolean? = null,
        @SerializedName("vote_average")
        @Expose
        var voteAverage: Double? = null,
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("popularity")
        @Expose
        var popularity: Double? = null,
        @SerializedName("poster_path")
        @Expose
        var posterPath: String? = null,
        @SerializedName("original_language")
        @Expose
        var originalLanguage: String? = null,
        @SerializedName("original_title")
        @Expose
        var originalTitle: String? = null,
        @SerializedName("backdrop_path")
        @Expose
        var backdropPath: String? = null,
        @SerializedName("adult")
        @Expose
        var adult: Boolean? = null,
        @SerializedName("overview")
        @Expose
        var overview: String? = null,
        @SerializedName("release_date")
        @Expose
        var releaseDate: String? = null) : Parcelable

