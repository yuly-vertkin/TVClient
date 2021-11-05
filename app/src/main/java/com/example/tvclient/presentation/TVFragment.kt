package com.example.tvclient.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.*
import com.example.tvclient.R
import com.example.tvclient.databinding.FragmentTvBinding
import com.example.tvclient.databinding.ItemListBinding
import com.example.tvclient.domain.ChannelCategory
import com.example.tvclient.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val maxItemsStr = sharedPreferences.getString(getString(R.string.max_items_key), "")
        val maxItems = if (!maxItemsStr.isNullOrEmpty()) maxItemsStr.toInt() else Int.MAX_VALUE
        tvViewModel.updateMaxItems(maxItems)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTvBinding.inflate(inflater)
        with(binding) {
            lifecycleOwner = this@TVFragment
            viewModel = tvViewModel
            list.adapter = TVListAdapter { tvViewModel.onClick(root, it) }
            swipeRefresh.setOnRefreshListener { updateList() }
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val item = menu.findItem(R.id.action_search).actionView as SearchView
        setupSearchView(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_refresh -> {
                binding.swipeRefresh.isRefreshing = true
                updateList()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateList() {
        lifecycleScope.launch {
            tvViewModel.update()
            delay(3000)
            binding.swipeRefresh.isRefreshing = false
        }
    }

//    private fun findSearchView(): SearchView? {
//        return (requireActivity() as TVClientActivity).toolbar?.menu
//            ?.findItem(R.id.action_search)?.actionView as? SearchView
//    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                activity?.hideKeyboard()
                query?.let { query ->
                    val list = (binding.list.adapter as ListAdapter<ChannelCategory, TVListAdapter.VH>).currentList
                    val item: ChannelCategory? = list.find { it.name.contains(query, true) }
                    item?.let { Toast.makeText(requireContext(), "${it.name}", LENGTH_LONG).show() }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}

class TVListAdapter(val clickListener: (String) -> Unit) : ListAdapter<ChannelCategory, TVListAdapter.VH>(DiffCalback) {
        object DiffCalback : DiffUtil.ItemCallback<ChannelCategory>() {
            override fun areItemsTheSame(oldItem: ChannelCategory, newItem: ChannelCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChannelCategory, newItem: ChannelCategory): Boolean {
                return oldItem.name == newItem.name
            }
        }

    class VH(private val itemBinding: ItemListBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ChannelCategory, clickListener: (name: String) -> Unit) {
            itemBinding.name = item.name
            itemBinding.executePendingBindings()
            itemView.setOnClickListener { clickListener(item.name) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}