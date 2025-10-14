package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var tvMes: TextView
    private lateinit var etDeviceName: EditText
    private lateinit var etToken: EditText
    private lateinit var btnConectar: Button
    private lateinit var btnSimular: Button

    private lateinit var vm: MainViewModel

    private val permsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { updateStatus("Permisos: $it") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvMes = findViewById(R.id.tvMes)
        etDeviceName = findViewById(R.id.etDeviceName)
        etToken = findViewById(R.id.etToken)
        btnConectar = findViewById(R.id.btnConectar)
        btnSimular = findViewById(R.id.btnSimular)

        val dao = AppDatabase.get(this).carPassDao()
        val repo = Repository(dao)
        vm = ViewModelProvider(this, MainViewModelFactory(repo))[MainViewModel::class.java]

        lifecycleScope.launch { vm.monthlyCount.collect { tvMes.text = "Mes actual: $it autos" } }
        vm.refreshMonthly()

        btnConectar.setOnClickListener { conectar() }
        btnSimular.setOnClickListener { vm.addEventNow() }

        ensurePermissions()
    }

    private fun ensurePermissions() {
        val needed = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPerm(Manifest.permission.BLUETOOTH_CONNECT)) needed += Manifest.permission.BLUETOOTH_CONNECT
            if (!hasPerm(Manifest.permission.BLUETOOTH_SCAN)) needed += Manifest.permission.BLUETOOTH_SCAN
        } else {
            if (!hasPerm(Manifest.permission.ACCESS_FINE_LOCATION)) needed += Manifest.permission.ACCESS_FINE_LOCATION
        }
        if (needed.isNotEmpty()) permsLauncher.launch(needed.toTypedArray())
    }

    private fun hasPerm(p: String) = ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED
    private fun updateStatus(msg: String) { tvStatus.text = "Estado: $msg" }

    private fun conectar() {
        val name = etDeviceName.text.toString().trim().ifEmpty { "HC-05" }
        val token = etToken.text.toString().trim().ifEmpty { "CAR" }
        val client = BluetoothClient(name, token)

        updateStatus("Conectando a $name…")
        lifecycleScope.launch {
            val result = client.connectAndListen(onToken = { vm.addEventNow() })
            if (result.isSuccess) {
                updateStatus("Desconectado")
            } else {
                updateStatus("Error: ${result.exceptionOrNull()} ")
            }
        }
    }
}