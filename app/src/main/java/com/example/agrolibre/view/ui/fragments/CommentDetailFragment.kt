package com.example.agrolibre.view.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.agrolibre.R
import com.example.agrolibre.databinding.CommentDetailFragmentBinding
import com.example.agrolibre.databinding.CommentsFragmentBinding
import com.example.agrolibre.domain.data.network.Repo
import com.example.agrolibre.model.Comment
import com.example.agrolibre.viewmodel.CommentDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class CommentDetailFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = CommentDetailFragment()
    }

    private lateinit var viewModel: CommentDetailViewModel
    private var _binding: CommentDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val repo = Repo()
    private val userAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CommentDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.btnSaveComment.setOnClickListener {

            var comment = userAuth.currentUser!!.displayName?.let { it1 -> Comment(it1,binding.ratingBar.rating.toString(),
                binding.etCommetsContent.text.toString(),"https://cdn-icons-png.flaticon.com/512/149/149071.png",)}
            if(userAuth.currentUser!!.photoUrl != null){
                comment!!.userImageUrl = userAuth.currentUser!!.photoUrl.toString()
            }
            if (repo.setComment(comment!!)){
                Snackbar.make(view,"¡Gracias por su opinión!",Snackbar.LENGTH_LONG).setAction("Aceptar",
                    View.OnClickListener {
                        findNavController().navigate(R.id.commentsFragment)
                    }).show()
                findNavController().navigate(R.id.commentsFragment)

            }else{
                Toast.makeText(activity, "Ocurrío un error intente de nuevo",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommentDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}