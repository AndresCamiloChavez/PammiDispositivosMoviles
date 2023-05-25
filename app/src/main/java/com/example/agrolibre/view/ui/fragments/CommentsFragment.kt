package com.example.agrolibre.view.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrolibre.R
import com.example.agrolibre.databinding.CommentsFragmentBinding
import com.example.agrolibre.view.adapter.AdapterComments
import com.example.agrolibre.viewmodel.CommentsViewModel

class CommentsFragment : Fragment() {


    companion object {
        fun newInstance() = CommentsFragment()
    }
    private val viewModel by lazy{ViewModelProvider(this).get(CommentsViewModel::class.java)}
    private var _binding: CommentsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterComments


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CommentsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterComments()
        binding.rvComments.layoutManager = LinearLayoutManager(activity)
        binding.rvComments.adapter = adapter
        observeData()

        binding.floatingButtonToDetailComment.setOnClickListener {
            findNavController().navigate(R.id.commentDetailFragment, null)
        }
    }
    fun observeData(){
        binding.shimmerViewContainer.startShimmer()
        viewModel.fetchCommentsData().observe(viewLifecycleOwner, Observer {
            binding.shimmerViewContainer.hideShimmer()
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            adapter.setListComments(it)
            adapter.notifyDataSetChanged()
        })
    }

}