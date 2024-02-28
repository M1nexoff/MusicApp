package uz.developers.musicplayer.presenter.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.developers.musicplayer.R
import uz.developers.musicplayer.data.Music
import uz.developers.musicplayer.databinding.ItemMusicBinding

class MusicsAdapter : ListAdapter<Music,MusicsAdapter.InnerHolder>(InnerDiffUtil) {
    var selectMusicPositionListener : ((Int) -> Unit)?= null
    var onClick : ((Music)->Unit)? = null
    var cursor : Cursor?= null
    inner class InnerHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                selectMusicPositionListener?.invoke(adapterPosition)
            }
        }
        fun bind(){
            val data = getItem(adapterPosition)
            binding.apply {
                musicName.text = data.title
                author.text = data.artist
                try {
                    Glide.with(binding.root.context)
                        .load(data.image)
                        .placeholder(R.drawable.ava)
                        .error(R.drawable.ic_launcher_background)
                        .into(image)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                root.setOnClickListener {
                    onClick?.invoke(getItem(adapterPosition))
                }
            }
        }
    }
    object InnerDiffUtil : DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
            return oldItem.id == newItem.id
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
        holder.bind()
    }
}