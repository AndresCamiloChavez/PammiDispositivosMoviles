package com.example.agrolibre.view.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agrolibre.R
import com.example.agrolibre.databinding.FragmentHomeBinding
import com.example.agrolibre.databinding.ProductsFragmentBinding
import com.example.agrolibre.model.Ubication
import com.example.agrolibre.viewmodel.ProductsViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

/*import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker*/

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? =null
    private val binding get() = _binding!!
    private val viewModel by lazy{ ViewModelProvider(this).get(ProductsViewModel::class.java)}
    private lateinit var map: MapView
    private val list = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        dataCarousel()
//        mapFragment.getMapAsync(this)

       /* binding.mapView.setTileSource(TileSourceFactory.MAPNIK)

        val location = Ubication()
        val geoPoint = GeoPoint(location.latitude, location.longitude)
        val mapController = binding.mapView.controller
        mapController.setZoom(16.0)
        mapController.setCenter(geoPoint)

        val marker = Marker(binding.mapView)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "AgroLibre"
        binding.mapView.overlays.add(marker)*/


    }

    fun dataCarousel(){
        viewModel.fetchProductsData().observe(viewLifecycleOwner, Observer {
            for ( item in it){
                list.add(
                    CarouselItem(
                        imageUrl = item.imageUrl,
                        caption = item.name
                    )
                )
            }
            binding.carousel.setData(list)
            binding.carousel.start()

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(pO: GoogleMap){
        val ubication = Ubication()
        val zoom = 16f
        val centerMap = LatLng(ubication.latitude, ubication.longitude)
        pO.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, zoom))

    }

}