package com.example.fixitapp.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixitapp.R
import com.example.fixitapp.model.Service

class ServiceAdapter(
    private val services: List<Service>,
    private val onDeleteClick: (Service) -> Unit,
    private val onItemClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.tvNombre.text = service.nombreCliente
        holder.tvTipo.text = service.tipoServicio

        holder.itemView.setOnClickListener {
            onItemClick(service)
        }

        holder.btnEliminar.setOnClickListener {
            onDeleteClick(service)
        }
    }

    override fun getItemCount(): Int = services.size
}

