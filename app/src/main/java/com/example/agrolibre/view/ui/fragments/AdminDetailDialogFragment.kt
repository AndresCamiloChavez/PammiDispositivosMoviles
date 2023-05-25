package com.example.agrolibre.view.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.agrolibre.R
import com.example.agrolibre.databinding.FragmentAdminDetailDialogBinding
import com.example.agrolibre.domain.data.network.Repo
import com.example.agrolibre.model.DBHelper
import com.example.agrolibre.model.Product
import com.example.agrolibre.view.ui.activities.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AdminDetailDialogFragment : BottomSheetDialogFragment() {

    private val userAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var dbHelper: DBHelper
    private var _binding: FragmentAdminDetailDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var informacionDBHelper: DBHelper
    private val repo = Repo()
    val storage: StorageReference = FirebaseStorage.getInstance().getReference()
    private lateinit var imgR: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        informacionDBHelper = DBHelper(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminDetailDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgR = Uri.parse("https://images.assetsdelivery.com/compings_v2/pavelstasevich/pavelstasevich1811/pavelstasevich181101035.jpg")
        binding.btnSubirFotoPeril.setOnClickListener {
            requestPermission()
        }
        binding.btnSaveAdmin.setOnClickListener {
            upPerfil()
        }
    }
    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED ->{
                    pickPhothoFromGalery()
                }
                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            pickPhothoFromGalery()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if(it){
            pickPhothoFromGalery()
        }else{
            Toast.makeText(activity, "Necesita activar permisos", Toast.LENGTH_SHORT).show()
        }
    }


    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            imgR = result.data?.data!!
            binding.imgFotoPeril.setImageURI(imgR)
        }

    }

    private fun pickPhothoFromGalery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }
    private fun upPerfil(){
        val filePath = storage.child("fotosPerfil").child(imgR?.lastPathSegment!!)
        binding.progressAdmin.visibility = View.VISIBLE

        binding.btnSaveAdmin.isEnabled = false
        binding.btnSubirFotoPeril.isEnabled = false
        filePath.putFile(imgR).continueWith {
            if (!it.isSuccessful) {
                it.exception?.let { t ->
                    throw t
                }
            }
            filePath.downloadUrl
        }.addOnCompleteListener {
            if(it.isSuccessful){
                it.result!!.addOnSuccessListener{task ->
                    val myUri = task.toString()
                    Log.d("se subío la foto","valor $myUri")
                    binding.progressAdmin.visibility = View.GONE

                    var userProfileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.etNameAdmin.text.toString())
                        .setPhotoUri(Uri.parse(myUri))
                        .build()
                        userAuth.currentUser?.updateProfile(userProfileChangeRequest)?.addOnCompleteListener {
                           // Toast.makeText(activity, "Se actualizo el perfil",Toast.LENGTH_SHORT).show()
                        }?.addOnFailureListener {
                            Toast.makeText(activity, "Error",Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(activity, "Se ha editado el perfil", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.administratorFragment)
                    }

                    binding.btnSubirFotoPeril.isEnabled = true
                    binding.btnSaveAdmin.isEnabled = true
                    //ojito con el error aquí de precio

            }
        }
    }

}