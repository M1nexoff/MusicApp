package uz.developers.musicplayer.presenter.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.developers.musicplayer.R
import uz.developers.musicplayer.data.Music
import uz.developers.musicplayer.databinding.ScreenMusicBinding
import uz.developers.musicplayer.presenter.adapter.MusicsAdapter
import uz.developers.musicplayer.utils.MyAppManager
import uz.developers.musicplayer.data.ActionEnum
import uz.developers.musicplayer.presenter.service.MyService

@AndroidEntryPoint
class MusicScreen : Fragment(R.layout.screen_music) {
    private val REQUEST_PERMISSION_CODE = 123
    private val binding by viewBinding(ScreenMusicBinding::bind)
    private val adapter by lazy { MusicsAdapter() }
    private val musicList = ArrayList<Music>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )
        } else {
            loadMusicFiles()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.cursor = MyAppManager.cursor
        binding.apply {
            recycle.adapter = adapter
            recycle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter.onClick = {
                findNavController().navigate(MusicScreenDirections.actionMusicScreenToCurrentMusicScreen())
            }
        }
        adapter.submitList(musicList)
        adapter.cursor = MyAppManager.cursor

        binding.currentMusicLayout.setOnClickListener {
            findNavController().navigate(MusicScreenDirections.actionMusicScreenToCurrentMusicScreen())
        }
//        binding.buttonNextScreen.setOnClickListener { startMyService(ActionEnum.NEXT) }
//        binding.buttonPrevScreen.setOnClickListener { startMyService(ActionEnum.PREV) }
//        binding.buttonManageScreen.setOnClickListener { startMyService(ActionEnum.MANAGE) }
        adapter.selectMusicPositionListener = {
            MyAppManager.selectMusicPos = it
            startMyService(ActionEnum.PLAY)
        }

        MyAppManager.playMusicLiveData.observe(viewLifecycleOwner, playMusicObserver)
        MyAppManager.isPlayingLiveData.observe(viewLifecycleOwner, isPlayingObserver)
    }
    private fun loadMusicFiles() {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        val cursor: Cursor? = requireActivity().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            sortOrder
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                val albumArt = getAlbumArt(albumId)

                val defaultAlbumArtUri = "android.resource://${requireActivity().packageName}/${R.drawable.ava}"

                musicList.add(Music(id = id,data = data, title = title, artist = artist, duration =  duration, image =  albumArt ?: defaultAlbumArtUri))
            } while (cursor.moveToNext())
        }
        cursor?.close()
    }

    private fun getAlbumArt(albumId: Long): String? {
        val projection = arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART)
        val selection = "${MediaStore.Audio.Albums._ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())

        val cursor = requireActivity().contentResolver.query(
            EXTERNAL_CONTENT_URI,
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMusicFiles()
        }
    }

    private fun startMyService(action  : ActionEnum) {
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("COMMAND", action)
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
        } else requireActivity().startService(intent)
    }

    private val playMusicObserver = Observer<Music> { data ->
        binding.apply {
            currentMusicName.text= data.title
            textCurrentMusicAuthor.text = data.artist
        }
    }

    private val isPlayingObserver = Observer<Boolean> {
        if (it) binding.play.setImageResource(R.drawable.stop)
        else binding.play.setImageResource(R.drawable.play)
    }
}