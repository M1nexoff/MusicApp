package uz.developers.musicplayer.presenter.screen

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.developers.musicplayer.R
import uz.developers.musicplayer.data.Music
import uz.developers.musicplayer.databinding.ScreenMusicBinding
import uz.developers.musicplayer.presenter.adapter.MusicsAdapter

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
            loadMusicFiles()
        } else {
            loadMusicFiles()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager=  LinearLayoutManager(requireContext())

        adapter.submitList(musicList)

        adapter.onClick={
            findNavController().navigate(MusicScreenDirections.actionMusicScreenToCurrentMusicScreen(it))
        }
    }

    private fun loadMusicFiles() {
        val contentResolver = requireActivity().contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.AudioColumns.DATA)

        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            var dataIndex: Int
            var titleIndex: Int
            var artistIndex: Int
            var pictureIndex: Int

            try {
                dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            } catch (e: Exception) {
                dataIndex = -1
                e.printStackTrace()
            }

            try {
                titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            } catch (e: Exception) {
                titleIndex = -1
                e.printStackTrace()
            }

            try {
                artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            } catch (e: Exception) {
                artistIndex = -1
                e.printStackTrace()
            }

            try {
                pictureIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)
            } catch (e: Exception) {
                pictureIndex = -1
                e.printStackTrace()
            }

            while (it.moveToNext()) {
                var data: String
                var musicName: String
                var artistName: String
                var picture: String

                try {
                    data = it.getString(dataIndex)
                } catch (e: Exception) {
                    data = ""
                    e.printStackTrace()
                }

                try {
                    musicName = it.getString(titleIndex)
                } catch (e: Exception) {
                    musicName = ""
                    e.printStackTrace()
                }

                try {
                    artistName = it.getString(artistIndex)
                } catch (e: Exception) {
                    artistName = ""
                    e.printStackTrace()
                }

                try {
                    picture = it.getString(pictureIndex)
                } catch (e: Exception) {
                    picture = ""
                    e.printStackTrace()
                }

                musicList.add(Music(musicName, artistName, picture, data))
            }
        }


        Log.d("TTT","${musicList.size}")

        for (i in 0..<musicList.size) {
            Log.d("TTT","${musicList[i].name}-> ${musicList[i].author}->${musicList[i].data}->${musicList[i].img}")
        }


    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusicFiles()
            }
        }
    }
}