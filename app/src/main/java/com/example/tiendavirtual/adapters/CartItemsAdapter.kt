package com.example.tiendavirtual.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendavirtual.R
import com.example.tiendavirtual.models.Items
import com.google.firebase.firestore.FirebaseFirestore

class CartItemsAdapter(private val Items: ArrayList<Items>, private val context: Context) :
    RecyclerView.Adapter<CartItemsAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private var listener: CartItemsAdapterListener? = null

    // Define la interfaz para notificar cambios
    interface CartItemsAdapterListener {
        fun onDataSetChanged()
    }

    fun setCartItemsAdapterListener(listener: CartItemsAdapterListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.tvCartProductName)
        val precioTextView: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        val imagenImageView: ImageView = itemView.findViewById(R.id.ivCartProduct)
        val cantidadTextView: TextView = itemView.findViewById(R.id.tvCartItemCount)
        val addProduct: Button = itemView.findViewById(R.id.btnCartItemAdd)
        val minusProduct: Button = itemView.findViewById(R.id.btnCartItemMinus)
        val deletProduct: ImageButton = itemView.findViewById(R.id.delet)
        var productAdd = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val cartView = inflater.inflate(R.layout.cart_product_item, parent, false)
        return ViewHolder(cartView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Items[position]

        holder.nombreTextView.text = item.nombre
        holder.precioTextView.text = "$${item.precio}"
        holder.imagenImageView.setImageResource(item.imagen)
        holder.cantidadTextView.text = item.cantidad
        holder.productAdd = item.cantidad.toInt()

        holder.addProduct.setOnClickListener {
            holder.productAdd++
            holder.cantidadTextView.text = holder.productAdd.toString()
            val docRef = db.collection("compras").document(item.nombre)
            docRef.update("cantidad", holder.productAdd)
                .addOnSuccessListener {
                    // Notificar a la actividad que los datos han cambiado
                    listener?.onDataSetChanged()
                }
                .addOnFailureListener { e ->
                    // Manejar errores aquí
                }
        }

        holder.minusProduct.setOnClickListener {
            if (holder.productAdd > 1) {
                holder.productAdd--
                holder.cantidadTextView.text = holder.productAdd.toString()
                val docRef = db.collection("compras").document(item.nombre)
                docRef.update("cantidad", holder.productAdd)
                    .addOnSuccessListener {
                        // Notificar a la actividad que los datos han cambiado
                        listener?.onDataSetChanged()
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores aquí
                    }
            }
        }
        holder.deletProduct.setOnClickListener {
            val docRef = db.collection("compras").document(item.nombre)
            docRef
                .delete()
                .addOnSuccessListener {
                    Log.d("Firestore", "Documento eliminado exitosamente.")
                    listener?.onDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al eliminar documento: $e")
                }
        }
    }

    override fun getItemCount(): Int {
        return Items.size
    }
}
