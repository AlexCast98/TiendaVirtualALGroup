package com.example.tiendavirtual.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.tiendavirtual.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class details_product : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_details_page)
        val db = FirebaseFirestore.getInstance()
        // Recupera los datos pasados desde la actividad anterior
        val idProducto = intent.getStringExtra("producto_id")
        val nombreProducto = intent.getStringExtra("producto_nombre")
        val precioProducto = intent.getStringExtra("producto_precio")
        val descripcionProduct = intent.getStringExtra("producto_descripcion")
        val imagenProductoResId = intent.getIntExtra("producto_imagen", 0)

        // Encuentra las vistas en el diseño XML de DetalleProductoActivity
        val nombreTextView = findViewById<TextView>(R.id.tvDetailsProductName)
        val precioTextView = findViewById<TextView>(R.id.tvDetailsProductPrice)
        val descripcionProducto = findViewById<TextView>(R.id.tvDetailsProductDescription)
        val volver = findViewById<MaterialToolbar>(R.id.detailActualToolbar)
        val imagenImageView = findViewById<ImageView>(R.id.ivDetails)
        val addProduct = findViewById<Button>(R.id.btnDetailsAddToCart)

        // Establece los datos en las vistas
        nombreTextView.text = nombreProducto
        precioTextView.text = "$${precioProducto}"
        descripcionProducto.text = descripcionProduct
        imagenImageView.setImageResource(imagenProductoResId)
        volver.setOnClickListener {
            finish()
        }
        addProduct.setOnClickListener {
            val coleccion = db.collection("compras")
            val nuevoDocumento = coleccion.document()

// Crear un objeto de datos para la compra
            val compra = hashMapOf(
                "producto" to nombreProducto,
                "cantidad" to 1,  // Cantidad de productos comprados
                "precio" to precioProducto,  // Precio unitario del producto
                "imagenId" to imagenProductoResId
            )
            coleccion.document(nombreProducto.toString())
                .set(compra)
                .addOnSuccessListener { Toast.makeText(this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()}
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al agregar el producto", Toast.LENGTH_SHORT).show()
                    Log.d("ErrorAddProduct", "Error al agregar compra", e) }
        }
    }
}