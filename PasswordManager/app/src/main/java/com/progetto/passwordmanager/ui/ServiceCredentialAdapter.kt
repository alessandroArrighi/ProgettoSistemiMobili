package com.progetto.passwordmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progetto.passwordmanager.R
import com.progetto.passwordmanager.data.model.ServiceCredentialModel


class ServiceCredentialAdapter(private val dataSource: MutableList<ServiceCredentialModel>) :
    RecyclerView.Adapter<ServiceCredentialAdapter.ViewHolder> () {

    interface OnClickListener {
        fun onClick(position: Int,
                    serviceCredentialModel: ServiceCredentialModel, button: ImageButton)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val service : TextView
        val user : TextView
        val password : TextView
        val button : ImageButton

        init {
            service = view.findViewById(R.id.txtService)
            user = view.findViewById(R.id.txtUser)
            password = view.findViewById(R.id.txtPassword)
            button = view.findViewById(R.id.btnCopyPassword)
        }
    }

    private var onClickListener: OnClickListener? = null

    fun updateServiceCredentialList(serviceCredentialList: List<ServiceCredentialModel>) {
        dataSource.clear()
        dataSource.addAll(serviceCredentialList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_credential, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = dataSource.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val serviceCredential = dataSource[position]
        viewHolder.service.text = serviceCredential.service
        viewHolder.user.text = serviceCredential.user
        viewHolder.password.text = serviceCredential.password

        viewHolder.button.setOnClickListener {
            onClickListener?.onClick(position, serviceCredential, viewHolder.button)
        }
    }

    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }
}