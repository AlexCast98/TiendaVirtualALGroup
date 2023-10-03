package com.example.tiendavirtual.models

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precio: String = "",
    val imagen: String = "",
    val categoriaId: String = "",
    val descripcion: String = ""
)