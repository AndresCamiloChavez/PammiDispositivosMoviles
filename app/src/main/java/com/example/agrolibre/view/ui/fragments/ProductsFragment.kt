package com.example.agrolibre.view.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrolibre.R
import com.example.agrolibre.databinding.ProductsFragmentBinding
import com.example.agrolibre.model.Product
import com.example.agrolibre.view.adapter.AdapterProducts
import com.example.agrolibre.viewmodel.ProductsViewModel

class ProductsFragment : Fragment(), AdapterProducts.onProductClickListener {

    companion object {
        fun newInstance() = ProductsFragment()
    }

    private val viewModel by lazy{ViewModelProvider(this).get(ProductsViewModel::class.java)}
    private lateinit var adapter: AdapterProducts
    private var _binding: ProductsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterProducts(this)
        binding.rvProducts.layoutManager = GridLayoutManager(activity,2)
        binding.rvProducts.adapter = adapter
        observeData()

        binding.toolbar.inflateMenu(R.menu.menu_top)
        binding.toolbar.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_shopProductActivity)
            true
        }

        binding.floatingAddProduct.setOnClickListener {
            findNavController().navigate(R.id.addProductActivity)
        }
    }
    //obtiene los datos del viewModel
    fun observeData(){
        binding.shimmerViewContainer.startShimmer()
        viewModel.fetchProductsData().observe(viewLifecycleOwner, Observer {
            binding.shimmerViewContainer.hideShimmer()
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onItemClick(item: Product) {
        val action = ProductsFragmentDirections.actionProductsFragmentToOrderDetailFragmentDialog(item)
        findNavController().navigate(action)

    }

    override fun onPriceClick(item: Product) {
        Toast.makeText(context, "se agregro producto ", Toast.LENGTH_SHORT).show()
    }


}