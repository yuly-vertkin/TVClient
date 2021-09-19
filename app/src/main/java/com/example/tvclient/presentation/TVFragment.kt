package com.example.tvclient.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import com.example.tvclient.R
import com.example.tvclient.databinding.FragmentTvBinding
import com.example.tvclient.databinding.ItemListBinding
import com.example.tvclient.domain.ChannelCategory
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "TVClientFragment"

@AndroidEntryPoint
class TVFragment : Fragment() {
    private val tvViewModel: TVFragmentViewModel by viewModels()
    private lateinit var binding: FragmentTvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(TV_DETAIL_REQUEST_KEY) { requestKey, bundle ->
            val result = bundle.getString(TV_DETAIL_RESULT_KEY)
            Log.d(TAG, "TVDetailFragment result: $result")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTvBinding.inflate(inflater)
        with(binding) {
            lifecycleOwner = this@TVFragment
            viewModel = tvViewModel
            list.adapter = TVListAdapter()
            return root
        }
    }
}

class VH(private val itemBinding: ItemListBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: ChannelCategory) {
        itemBinding.name = item.name
        itemBinding.executePendingBindings()
    }
}

class TVListAdapter : ListAdapter<ChannelCategory, VH>(DiffCalback) {
        object DiffCalback : DiffUtil.ItemCallback<ChannelCategory>() {
            override fun areItemsTheSame(oldItem: ChannelCategory, newItem: ChannelCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChannelCategory, newItem: ChannelCategory): Boolean {
                return oldItem.name == newItem.name
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { view ->
            val action = TVFragmentDirections.nextAction(item.name)
            view.findNavController().navigate(action)
// without safe args
//            val bundle = bundleOf(TvDetailViewModeL.NAME to item.name)
//            view.findNavController().navigate(R.id.action_TVFragment_to_TVDetailFragment, bundle)
        }
    }
}