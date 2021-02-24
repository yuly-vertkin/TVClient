package com.example.mytest.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mytest.R
import com.example.mytest.databinding.FragmentTvBinding
import com.example.mytest.databinding.FragmentTvDetailBinding

class TVDetailFragment : Fragment() {
    companion object {
        const val NAME = "name"
    }

    private var name: String = ""
    private lateinit var binding: FragmentTvDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME) ?: "none"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentTvDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.detail).text = name
    }
}