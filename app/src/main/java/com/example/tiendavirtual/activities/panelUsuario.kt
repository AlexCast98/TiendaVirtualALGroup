package com.example.tiendavirtual.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiendavirtual.R
import com.example.tiendavirtual.adapters.CategoriaAdapter
import com.example.tiendavirtual.adapters.ProductosAdapters
import com.example.tiendavirtual.models.Categoria
import com.example.tiendavirtual.models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class panelUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_usuario)
        val shimmerLayout: ShimmerFrameLayout = findViewById(R.id.shimmer)
        val shimmerLayoutCategorias: ShimmerFrameLayout = findViewById(R.id.shimmerCategorias)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategorias)
        val recyclerViewProductos: RecyclerView = findViewById(R.id.recyclerViewProductos)
        val btn_comprar = findViewById<TextView>(R.id.buyNow)
        val btn_seeAll = findViewById<TextView>(R.id.tvSeeAll)
        val cart : ImageView = findViewById(R.id.cart)
        val usuarioName: TextView = findViewById(R.id.userName)
        val carritoCantidad: TextView = findViewById(R.id.carritoCantidad)
//        val search: EditText = findViewById(R.id.search)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val db = FirebaseFirestore.getInstance()
        val categoriasRef = db.collection("categorias")
        categoriasRef.get()
            .addOnSuccessListener { querySnapshot ->
                val categorias = ArrayList<Categoria>() // Suponiendo que tienes una clase Categoria para representar los datos
                shimmerLayoutCategorias.startShimmer()
                for (document in querySnapshot) {
                    val nombre = document.getString("nombre")
                    val urlFoto = document.getString("imagen")

                    // Crea una instancia de Categoria y agrégala a la lista
                    val categoria = Categoria(nombre.toString(), urlFoto.toString())
                    categorias.add(categoria)
                }
                shimmerLayoutCategorias.stopShimmer()
                shimmerLayoutCategorias.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                // Configura tu RecyclerView o ListView para mostrar las categorías
                val adapter = CategoriaAdapter(categorias,this) // Debes crear un adaptador personalizado
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
//        val productoAdapter = ProductosAdapters(this, productos) // Asegúrate de tener la lista de productos
//        recyclerViewProductos.adapter = productoAdapter

        val layoutManagerProduct = GridLayoutManager(this,2)
        recyclerViewProductos.layoutManager = layoutManagerProduct
        val productosRef = FirebaseFirestore.getInstance().collection("productos")
            .get()
        val productos = ArrayList<Producto>()
        productosRef
            .addOnSuccessListener { result ->
                shimmerLayout.startShimmer()
                for (document in result) {
                    val idProducto = document.getString("id")
                    val nombre = document.getString("nombre")
                    val imagen = document.getString("imagen")
                    val precio = document.getString("precio")
                    val categoriaId = document.getString("categoria")
                    val descripcion = document.getString("descripcion")
                    val producto = Producto(idProducto.toString(),nombre.toString(),precio.toString(),imagen.toString(),categoriaId.toString(),descripcion.toString())
                    productos.add(producto)
                }
                val adapterProduct = ProductosAdapters(productos,this) // Debes crear un adaptador personalizado

                recyclerViewProductos.adapter = adapterProduct
                // Aquí puedes configurar un adaptador y RecyclerView para mostrar los productos
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                recyclerViewProductos.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error al obtener productos: $exception")
            }
        cart.setOnClickListener {
            val cart = Intent(this,CartPage::class.java)
            startActivity(cart)
        }
        val cartAll = FirebaseFirestore.getInstance().collection("compras")
            .get()
        cartAll
            .addOnSuccessListener { result ->
           carritoCantidad.text = result.size().toString()
        }
        btn_comprar.setOnClickListener {
            val allProducts = Intent(this,ProductAll::class.java)
            startActivity(allProducts)
        }
        btn_seeAll.setOnClickListener {
            val allProducts = Intent(this,ProductAll::class.java)
            startActivity(allProducts)
        }
        val auth = FirebaseAuth.getInstance()

        // Verificar si el usuario está autenticado

        val currentUser: FirebaseUser? = auth.currentUser
        Log.d("Usuario",currentUser.toString())
        if (currentUser != null) {
            // El usuario está autenticado, puedes obtener su nombre
            val userName = currentUser.displayName
            Log.d("Usuario",userName.toString())
            // Hacer algo con el nombre, por ejemplo, mostrarlo en un TextView
            usuarioName.text = userName

        }else{
            usuarioName.text = "Usuario"
        }

    }
}