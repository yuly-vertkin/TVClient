package com.example.tvclient.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.tvclient.R
import com.example.tvclient.databinding.FragmentTvDetailBinding

private const val TAG = "TVClientDetailFragment"

class TVDetailFragment : Fragment() {

    private lateinit var binding: FragmentTvDetailBinding
    private val viewModel: TvDetailViewModeL by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTvDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.detail.setOnClickListener {
            SimpleDialog.show( this@TVDetailFragment,
                titleId = R.string.dialog_title,
                textId = R.string.dialog_text,
                onCancelClick = { Log.d(TAG,"Cancel pressed") }
            ) {
                Log.d(TAG,"OK pressed")
            }
        }
    }

    override fun onDestroyView() {
        setFragmentResult(TV_DETAIL_REQUEST_KEY, bundleOf(TV_DETAIL_RESULT_KEY to "TVDetailFragment success"))
        super.onDestroyView()
    }
}