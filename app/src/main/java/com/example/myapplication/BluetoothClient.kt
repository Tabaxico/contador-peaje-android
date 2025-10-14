package com.example.myapplication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID

class BluetoothClient(
    private val deviceName: String,
    private val token: String = "CAR"
) {
    private val sppUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    @SuppressLint("MissingPermission")
    private fun findPairedDeviceByName(name: String): BluetoothDevice? =
        adapter?.bondedDevices?.firstOrNull { it.name == name }

    @SuppressLint("MissingPermission")
    suspend fun connectAndListen(onToken: suspend () -> Unit): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val dev = findPairedDeviceByName(deviceName)
                ?: return@withContext Result.failure(IllegalStateException("Dispositivo $deviceName no emparejado"))
            val sock = dev.createRfcommSocketToServiceRecord(sppUUID)
            adapter?.cancelDiscovery()
            sock.connect()
            socket = sock

            val reader = BufferedReader(InputStreamReader(sock.inputStream))
            while (true) {
                val line = reader.readLine() ?: break
                if (line.trim().equals(token, ignoreCase = true)) onToken()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            close()
            Result.failure(e)
        }
    }

    fun close() { try { socket?.close() } catch (_: Exception) {} }
}
