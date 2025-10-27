package com.example.fixitapp.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fixitapp.R
import com.example.fixitapp.model.Service

class ServiceAdapter(private var serviceList: List<Service>) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.tvTipo.text = service.tipoServicio
        holder.tvFecha.text = "Fecha: ${service.fecha}"
        holder.tvEstado.text = "Estado: ${service.estado}"
    }

    override fun getItemCount(): Int = serviceList.size

    fun updateList(newList: List<Service>) {
        serviceList = newList
        notifyDataSetChanged()
    }
}
