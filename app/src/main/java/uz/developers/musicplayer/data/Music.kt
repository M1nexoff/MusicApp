package uz.developers.musicplayer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val name: String,
    val author: String,
    val img: String,
    val data: String
) : Parcelable