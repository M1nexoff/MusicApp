package uz.developers.musicplayer.presenter.viewModel.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.developers.musicplayer.presenter.viewModel.MusicViewModel
import javax.inject.Inject

@HiltViewModel
class MusicViewModelImpl @Inject constructor() : ViewModel(),MusicViewModel {

}
