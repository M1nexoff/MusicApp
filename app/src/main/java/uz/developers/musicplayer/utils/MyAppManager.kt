package uz.developers.musicplayer.utils

import android.database.Cursor
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import uz.developers.musicplayer.data.Music
import uz.developers.musicplayer.data.ActionEnum


object MyAppManager {
    var selectMusicPos: Int = -1
    var cursor: Cursor? = null
    var lastCommand: ActionEnum = ActionEnum.PLAY

    var currentTime : Long = 0L
    var fullTime : Long = 0L

    val currentTimeLiveData = MutableLiveData<Long>()

    val playMusicLiveData = MutableLiveData<Music>()
    val isPlayingLiveData = MutableLiveData<Boolean>()
     fun getAlbumArt(activity: AppCompatActivity,albumId: Long): String? {
        val projection = arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART)
        val selection = "${MediaStore.Audio.Albums._ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val cursor = activity.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        var albumArt: String? = null
        cursor?.use {
            if (it.moveToFirst()) {
                albumArt = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
            }
        }

        cursor?.close()
        return albumArt
    }
}