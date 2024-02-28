package uz.developers.musicplayer.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.developers.musicplayer.data.Music
import uz.developers.musicplayer.databinding.ItemMusicBinding

class MusicsAdapter : ListAdapter<Music,MusicsAdapter.InnerHolder>(InnerDiffUtil) {
    var onClick : ((Music)->Unit)? = null
    inner class InnerHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(){

        }
    }
    object InnerDiffUtil : DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {

    }
}