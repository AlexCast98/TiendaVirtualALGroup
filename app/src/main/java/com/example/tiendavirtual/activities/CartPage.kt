package com.example.tiendavirtual.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendavirtual.R
import com.example.tiendavirtual.adapters.CartItemsAdapter
import com.example.tiendavirtual.models.Items
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore

class CartPage : AppCompatActivity(), CartItemsAdapter.CartItemsAdapterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var subtotalItem: TextView
    private lateinit var subTotalPrice: TextView
    private lateinit var deliveryPrice: TextView
    private lateinit var totalPrice: TextView
    private lateinit var volver: MaterialToolbar

    private var subTotal = 0
    private var subTotalItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_page)
        volver = findViewById(R.id.cartActualToolbar)
        recyclerView = findViewById(R.id.rvCartItems)
        subtotalItem = findViewById(R.id.tvLastSubTotalItems)
        subTotalPrice = findViewById(R.id.tvLastSubTotalprice)
        deliveryPrice = findViewById(R.id.delivery)
        totalPrice = findViewById(R.id.tvLastTotalPrice)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        cargarDatosBaseDeDatos()
    }

    private fun cargarDatosBaseDeDatos() {
        val cartItemsRef = FirebaseFirestore.getInstance().collection("compras").get()
        val cartItems = ArrayList<Items>()
        cartItemsRef
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombre = document.getString("producto")
                    val precio = document.getString("precio")
                    val imagen = document.getLong("imagenId")
                    Log.d("imagenCartPage", imagen.toString())
                    val cantidad = document.getLong("cantidad")
                    subTotal += precio.toString().toInt() * cantidad.toString().toInt()
                    subTotalItems += cantidad.toString().toInt()
                    val itemsCart = Items(nombre.toString(), imagen.toString().toInt(), precio.toString(), cantidad.toString())
                    cartItems.add(itemsCart)
                }
                subtotalItem.text = "SubTotal Items(${subTotalItems})"
                subTotalPrice.text = "$${subTotal}"
                deliveryPrice.text = "$${subTotalItems * 10000}"
                totalPrice.text = "$${subTotal + subTotalItems * 10000}"

                val adapterProduct = CartItemsAdapter(cartItems, this)
                adapterProduct.setCartItemsAdapterListener(this)
                recyclerView.adapter = adapterProduct
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al obtener productos: $exception")
            }
        volver.setOnClickListener {
            finish()
        }
    }

    override fun onDataSetChanged() {
        // Esta función se llama cuando los datos en el adaptador cambian
        // Puedes realizar aquí cualquier acción que desees en respuesta a los cambios de datos
        // Por ejemplo, puedes volver a cargar los datos si es necesario
        subTotal = 0
        subTotalItems = 0
        cargarDatosBaseDeDatos()
    }

}
