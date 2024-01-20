package cl.nvh.estudio.app_iplabank

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority

class LocationRepositorio (
    val fusedLocationProviderClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    fun conseguirUbicacion(
        cuandoExito: (u: Location) -> Unit,
        cuandoError: (e: Exception) -> Unit
    ) {
        val tarea = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        )
        tarea.addOnSuccessListener { cuandoExito(it) }
        tarea.addOnFailureListener { cuandoError(it) }
    }
}
