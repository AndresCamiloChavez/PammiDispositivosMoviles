package com.example.agrolibre.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrolibre.R
import com.example.agrolibre.model.Product
import com.example.agrolibre.view.adapter.base.BaseViewHolder
import java.lang.IllegalArgumentException

class AdapterProducts( private val itemClickListener:onProductClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var dataList = mutableListOf<Product>()


    interface onProductClickListener{
        fun onItemClick(item: Product)
        fun onPriceClick(item: Product)// para futura compra
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ProductsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))
    }
    fun setListData(data:MutableList<Product>){
        dataList = data
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is ProductsViewHolder -> holder.bind(dataList[position], position)
            else -> throw IllegalArgumentException("Se olvido  pasar el viewholder ne el bind")
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ProductsViewHolder(itemView: View):BaseViewHolder<Product>(itemView){
        override fun bind(item: Product, position: Int) {

            itemView.setOnClickListener{
                itemClickListener.onItemClick(item)
            }

            Glide.with(itemView.context).load(item.imageUrl).into(itemView.findViewById(R.id.imgProductItem))
            itemView.findViewById<TextView>(R.id.tvProductNameItem).text = item.name
            itemView.findViewById<TextView>(R.id.tvProductPriceItem).text = "$ "+item.price.toString()
        }

    }


    /*private var dataList = mutableListOf<Product>()

    fun setListData(data:MutableList<Product>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product: Product = dataList[position]
        holder.bindView(product)
    }

    override fun getItemCount(): Int {
        return if(dataList.size>0){
            dataList.size
        }else{
            0
        }

    }

    //al estar dentro es una clase Hija (El padre se destruye también la hija)
    inner class ProductsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(product: Product){
            Glide.with(itemView.context).load(product.imageUrl).into(itemView.findViewById(R.id.imgProductItem))
            itemView.findViewById<TextView>(R.id.tvProductNameItem).text = product.name
            itemView.findViewById<TextView>(R.id.tvProductPriceItem).text = "$ "+product.price.toString()
        }
    }
*/
}