package com.example.agrolibre.view.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.agrolibre.R
import com.example.agrolibre.databinding.ActivityAddProductBinding
import com.example.agrolibre.databinding.ProductsFragmentBinding
import com.example.agrolibre.domain.data.network.Repo
import com.example.agrolibre.model.Product
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.jar.Manifest

class AddProductActivity : AppCompatActivity() {

    private val GALERY_INTENT = 1
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var imgR: Uri
    private val repo = Repo()

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        if(uri != null){
            imgR = uri
            binding.imgFotoSubir.setImageURI(uri)
            //seleciono una imagen
        }else{
            //no selecciono nada
        }
    }


    val storage: StorageReference = FirebaseStorage.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnUpProduct.setOnClickListener {
           upProduct()
        }

    }
    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    this,
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
            Toast.makeText(this, "Necesita activar permisos", Toast.LENGTH_SHORT).show()
        }
    }


    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            imgR = result.data?.data!!
            binding.imgFotoSubir.setImageURI(imgR)
        }

    }

    private fun pickPhothoFromGalery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }

    private fun upProduct(){
        val filePath = storage.child("fotos").child(imgR?.lastPathSegment!!)
        binding.progress.visibility = View.VISIBLE

        binding.btnUpProduct.isEnabled = false
        binding.btnSelectImage.isEnabled = false
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
                    binding.progress.visibility = View.GONE
                    if(repo.setProduct(Product(myUri,binding.etNameUpProduct.text.toString(),
                            binding.etPriceUpProduct.text.toString(),
                            binding.etDescriptionUpProduct.text.toString()))){
                        Toast.makeText(this, "¡El producto se ha publicado!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }

                    binding.btnUpProduct.isEnabled = true
                    binding.btnSelectImage.isEnabled = true
                    //ojito con el error aquí de precio
                }
            }
        }
    }
}