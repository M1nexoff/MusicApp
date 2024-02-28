package uz.developers.musicplayer.presenter.viewModel.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.developers.musicplayer.presenter.viewModel.SplashViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor() : ViewModel(), SplashViewModel {

    override val navigateToScreen = MutableLiveData<Unit>()

    init {
        determineInitialScreen()
    }

    private fun determineInitialScreen() {
        viewModelScope.launch {
            delay(1500)
            navigateToScreen.value = Unit
        }
    }
}
