package uz.developers.musicplayer.presenter.viewModel

import androidx.lifecycle.MutableLiveData

interface SplashViewModel {
    val navigateToScreen: MutableLiveData<Unit>
}