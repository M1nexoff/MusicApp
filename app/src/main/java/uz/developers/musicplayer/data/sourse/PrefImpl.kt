package uz.developers.musicplayer.data.sourse

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefImpl @Inject constructor(
    @ApplicationContext context: Context
): Pref {
    private  var sharedPreferences: SharedPreferences
    init {
        sharedPreferences=context.getSharedPreferences("ChessGame",Context.MODE_PRIVATE)
    }

}



