package com.example.tvclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvclient.data.Response
import com.example.tvclient.databinding.FragmentTvBinding
import com.example.tvclient.domain.ChannelCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TVFragment : Fragment() {
    private lateinit var binding: FragmentTvBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTvBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.layoutManager = LinearLayoutManager(requireContext())

        val model: TVClientViewModel by viewModels()
        model.channelCategoryList.observe(getViewLifecycleOwner(), Observer<com.example.tvclient.data.Response<List<ChannelCategory>>>{ response ->
            when(response) {
                is com.example.tvclient.data.Response.Loading -> println("!!! Loading")
                is com.example.tvclient.data.Response.Success -> binding.list.adapter = ListAdapter(response.data)
                is com.example.tvclient.data.Response.Error -> println("!!! Error: ${response.error}")
            }
        })

//        lifecycleScope.launch {
//            val response = Repository().getChannelCategoryList()
//            when(response) {
//                is Response.Success -> println("Categories: ${response.data}")
//                is Response.Error -> println("Error: ${response.code} ${response.msg}")
//            }
//        }

    }

    class ListAdapter(private val items: List<ChannelCategory>) : RecyclerView.Adapter<ListAdapter.VH>() {

        class VH(view: View) : RecyclerView.ViewHolder(view) {
            val textView = view.findViewById<TextView>(R.id.text)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.textView.text = items[position].name
            holder.itemView.setOnClickListener { view ->
                val bundle = bundleOf(TVDetailFragment.NAME to items[position].name)
                view.findNavController().navigate(R.id.action_TVFragment_to_TVDetailFragment, bundle)
            }
        }

        override fun getItemCount() = items.size
    }
}