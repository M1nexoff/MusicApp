package uz.developers.musicplayer.domain.impl

import uz.developers.musicplayer.data.sourse.Pref
import uz.developers.musicplayer.domain.AppRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepositoryImpl @Inject constructor(private val pref: Pref) : AppRepository {

}