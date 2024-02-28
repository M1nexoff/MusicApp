package uz.developers.musicplayer.presenter.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.developers.musicplayer.presenter.viewModel.impl.SplashViewModelImpl
import uz.developers.musicplayer.R
import uz.developers.musicplayer.databinding.ScreenSplashBinding
import uz.developers.musicplayer.presenter.viewModel.SplashViewModel


@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val viewModel:SplashViewModel by viewModels<SplashViewModelImpl>()
    private val binding by viewBinding ( ScreenSplashBinding::bind )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateToScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToMusicScreen())
        }
    }
}
