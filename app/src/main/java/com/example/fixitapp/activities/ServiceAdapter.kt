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
    private val serviceList: MutableList<Service>,
    private val onDeleteClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.tvTipo.text = "Servicio: ${service.tipoServicio}"
        holder.tvFecha.text = "Fecha: ${service.fecha}"
        holder.tvEstado.text = "Estado: ${service.estado}"

        holder.btnEliminar.setOnClickListener {
            onDeleteClick(service)
        }
    }

    override fun getItemCount(): Int = serviceList.size
}
