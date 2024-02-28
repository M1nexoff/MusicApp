package uz.developers.musicplayer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val id : Long,
    val artist : String?,
    val title : String?,
    val data : String?,
    val duration : Long,
    val image: String
) : Parcelable