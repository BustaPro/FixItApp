package com.example.fixitapp.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fixitapp.R
import com.example.fixitapp.databinding.ItemServiceBinding
import com.example.fixitapp.model.Service

class ServiceAdapter(
    private val services: List<Service>,
    private val onDeleteClick: (Service) -> Unit,
    private val onItemClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    init { setHasStableIds(true) }

    inner class ServiceViewHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServiceViewHolder(binding)
    }

    override fun getItemId(position: Int): Long = services[position].id.toLong()

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        val b = holder.binding

        // --- BINDING SEGURO Y LIGERO ---
        b.tvNombre.text = service.nombreCliente
        b.tvTipo.text = service.tipoServicio

        b.ivIcon.setImageResource(getIconFor(service.tipoServicio))

        // click item
        b.root.setOnClickListener {
            onItemClick(service)
        }

        // click eliminar
        b.btnEliminar.setOnClickListener {
            onDeleteClick(service)
        }
    }

    override fun getItemCount(): Int = services.size

    private fun getIconFor(tipo: String): Int {
        val t = tipo.lowercase()

        return when {
            "repar" in t || "electro" in t ->
                R.drawable.man

            "limp" in t || "aseo" in t ->
                R.drawable.broom

            "mant" in t || "hogar" in t ->
                R.drawable.repairhousebyiconsvgco

            else -> R.drawable.man
        }
    }
}
