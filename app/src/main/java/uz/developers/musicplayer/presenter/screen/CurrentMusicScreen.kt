package uz.developers.musicplayer.presenter.screen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.developers.musicplayer.R
import uz.developers.musicplayer.data.Music
import uz.developers.musicplayer.databinding.ScreenCurrentMusicBinding
import uz.developers.musicplayer.presenter.service.MyService
import uz.developers.musicplayer.utils.MyAppManager
import uz.developers.musicplayer.data.ActionEnum
import uz.developers.musicplayer.utils.setChangeProgress

@AndroidEntryPoint
class CurrentMusicScreen : Fragment(R.layout.screen_current_music) {
    private val binding by viewBinding(ScreenCurrentMusicBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.buttonNext.setOnClickListener { startMyService(ActionEnum.NEXT) }
//        binding.buttonPrev.setOnClickListener { startMyService(ActionEnum.PREV) }
        binding.play.setOnClickListener { startMyService(ActionEnum.MANAGE) }
        binding.seekBar.setChangeProgress {  }

        MyAppManager.playMusicLiveData.observe(viewLifecycleOwner, playMusicObserver)
        MyAppManager.isPlayingLiveData.observe(viewLifecycleOwner, isPlayingObserver)
        MyAppManager.currentTimeLiveData.observe(viewLifecycleOwner, currentTimeObserver)
    }

    private val playMusicObserver = Observer<Music> {
        binding.seekBar.progress = (MyAppManager.currentTime * 100 / MyAppManager.fullTime).toInt()
        binding.musicName.text = it.title
        binding.musicAuthor.text = it.artist
        binding.textAudioPlayedTime.text = MyAppManager.currentTime.toString()
        binding.textAudioTime.text = it.duration.toString()
    }

    private val isPlayingObserver = Observer<Boolean> {
        if (it) binding.play.setImageResource(R.drawable.stop)
        else binding.play.setImageResource(R.drawable.play)
    }

    private val currentTimeObserver = Observer<Long> {
        binding.seekBar.progress = (MyAppManager.currentTime * 100 / MyAppManager.fullTime).toInt()
        binding.textAudioPlayedTime.text = MyAppManager.currentTime.toString()
    }

    private fun startMyService(action  : ActionEnum) {
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("COMMAND", action)
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
        } else requireActivity().startService(intent)
    }

}
