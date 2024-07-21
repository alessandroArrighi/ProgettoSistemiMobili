package com.progetto.passwordmanager.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.progetto.passwordmanager.R
import com.progetto.passwordmanager.ServiceCredentialApplication
import com.progetto.passwordmanager.ServiceCredentialViewModel
import com.progetto.passwordmanager.ServiceCredentialViewModelFactory
import com.progetto.passwordmanager.data.model.ServiceCredentialModel
import com.progetto.passwordmanager.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val serviceCredentialViewModel: ServiceCredentialViewModel by viewModels {
        ServiceCredentialViewModelFactory((application as ServiceCredentialApplication).repository)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        serviceCredentialViewModel.updateServiceCredentialRemote()

        binding.btnCreateService.setOnClickListener {
            val intent = Intent(this, AddServiceCredentialActivity()::class.java)
            this.startActivityForResult(intent, 0)
        }

        recyclerView = findViewById(R.id.recyclerView)

        val adapter = ServiceCredentialAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        this.checkEmptyList(adapter.itemCount)

        serviceCredentialViewModel.let {
            lifecycleScope.launch {
                it
                    .uiServiceCredentialModelList
                    .flowWithLifecycle(lifecycle)
                    .collect(::updateServiceCredentialList)
            }
        }

        serviceCredentialViewModel.getAll()

        adapter.setOnClickListener(object : ServiceCredentialAdapter.OnClickListener {
            override fun onClick(
                position: Int,
                serviceCredentialModel: ServiceCredentialModel,
                button: ImageButton
            ) {
                val popupMenu = PopupMenu(this@MainActivity, button)

                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.title) {
                        "update" -> {
                            val intent = Intent(this@MainActivity,
                                AddServiceCredentialActivity()::class.java)
                            intent.putExtra("oldService", serviceCredentialModel.service)
                            intent.putExtra("oldUser", serviceCredentialModel.user)
                            intent.putExtra("oldPassword", serviceCredentialModel.password)
                            this@MainActivity.startActivityForResult(intent, 1)
                        }
                        "delete" -> {
                            serviceCredentialViewModel.delServiceCredential(
                                ServiceCredentialModel(
                                    service = serviceCredentialModel.service,
                                    user = serviceCredentialModel.user,
                                    password = serviceCredentialModel.password
                                )
                            )
                        }
                        "copy" -> {
                            val clipboard =
                                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData
                                .newPlainText("label", serviceCredentialModel.password)

                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(
                                this@MainActivity,
                                resources.getString(R.string.copyPasswordToast),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    true
                }
                popupMenu.show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                val service = data?.getStringExtra("resultService")
                val user = data?.getStringExtra("resultUser")
                val password = data?.getStringExtra("resultPassword")
                if(service != null && user != null && password != null) {
                    serviceCredentialViewModel.insert(
                        ServiceCredentialModel(
                            service = service,
                            user = user,
                            password = password
                        )
                    )
                }
            }
        } else {
            if(requestCode == 1) { // requestCode == 1 per l'activity avviata dal mod
                if(resultCode == RESULT_OK) {
                    val service = data?.getStringExtra("resultService")
                    val user = data?.getStringExtra("resultUser")
                    val password = data?.getStringExtra("resultPassword")
                    val oldService = data?.getStringExtra("oldService")
                    val oldUser = data?.getStringExtra("oldUser")
                    val oldPassword = data?.getStringExtra("oldPassword")

                    if(service != null && user != null && password != null
                        && oldService != null && oldUser != null && oldPassword != null) {
                        serviceCredentialViewModel.modServiceCredential(
                            serviceCredentialModel = ServiceCredentialModel(
                                service = service,
                                user = user,
                                password = password
                            ),
                            oldServiceCredentialModel = ServiceCredentialModel(
                                service = oldService,
                                user = oldUser,
                                password = oldPassword
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateServiceCredentialList(serviceCredentialList: List<ServiceCredentialModel>) {
        (recyclerView.adapter as ServiceCredentialAdapter)
            .updateServiceCredentialList(
                serviceCredentialList = serviceCredentialList
            )

        this.checkEmptyList(serviceCredentialList.size)
    }

    private fun checkEmptyList(listSize: Int) {
        val emptyListText = findViewById<TextView>(R.id.emptyListText)

        if (listSize <= 0) {
            recyclerView.visibility = View.GONE
            emptyListText.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyListText.visibility = View.GONE
        }
    }
}