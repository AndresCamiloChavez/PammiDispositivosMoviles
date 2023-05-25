package com.example.agrolibre.view.ui.fragments

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.agrolibre.R
import com.example.agrolibre.databinding.FragmentAdministratorBinding
import com.example.agrolibre.domain.data.network.Repo
import com.example.agrolibre.model.DBHelper
import com.example.agrolibre.model.Tables
import com.example.agrolibre.view.ui.activities.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class AdministratorFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var binding: FragmentAdministratorBinding
    private val userAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val repo = Repo()

    lateinit var informacionDBHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        informacionDBHelper = DBHelper(activity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdministratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        laodData(view)

        binding.progressAdmin.visibility = View.GONE
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_administratorFragment_to_adminDetailFragmentDialog2)

        }

        binding.btnExit.setOnClickListener {
            binding.progressAdmin.visibility = View.VISIBLE
            binding.btnExit.isEnabled = false
            AuthUI.getInstance().signOut(view.context).addOnSuccessListener {
                startActivity(Intent(view.context, LoginActivity::class.java))
                activity?.finish()

            }.addOnFailureListener {
                binding.progressAdmin.visibility = View.GONE
                binding.btnExit.isEnabled = true
                Toast.makeText(view.context, "Ocurrío un error ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.refreshAdmin.setOnRefreshListener {
            laodData(view)
            binding.refreshAdmin.isRefreshing = false
        }

        /*val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + Tables.information.TABLE_NAME, null)
        if (cursor.moveToFirst()) {
            do {
                binding.tvNameAdimin.text ="Nombre: " + cursor.getString(1).toString()
                binding.tvAddressAdimin.text = "Dirección: " + cursor.getString(2).toString()
                binding.tvPhoneAdmin.text = "Telefono: " + cursor.getString(4).toString()
                binding.tvEmailAdmin.text = "Correo: " + cursor.getString(3).toString()
            } while (cursor.moveToNext())
        }else{

            Toast.makeText(activity, "no funciona", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun laodData(view: View){
        if (userAuth.currentUser?.photoUrl != null) {
            Glide.with(view.context).load(userAuth.currentUser!!.photoUrl).into(binding.imgAdmin)
        } else {
            Glide.with(view.context).load("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .into(binding.imgAdmin)
        }
        binding.tvNameAdimin.text = userAuth.currentUser?.displayName
        binding.tvEmailAdmin.text = userAuth.currentUser?.email
    }
}