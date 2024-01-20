package cl.nvh.estudio.app_iplabank

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cl.nvh.estudio.app_iplabank.dataBase.BaseDatos
import cl.nvh.estudio.app_iplabank.dataBase.Registro
import cl.nvh.estudio.app_iplabank.dataBase.RegistroDao
import java.time.LocalDate

class viewModel: ViewModel() {

    //pantalla loginUI
    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    // pantalla registroUI
    private val _id= MutableLiveData<String>()
    val id:LiveData<String> = _id

    private val _nombre= MutableLiveData<String>()
    val nombre:LiveData<String> = _nombre

    private val _rut= MutableLiveData<String>()
    val rut:LiveData<String> = _rut

    private val _email= MutableLiveData<String>()
    val email:LiveData<String> = _email

    private val _telefono= MutableLiveData<String>()
    val telefono:LiveData<String> = _telefono

    private var _latitud= MutableLiveData<Float>()
    val latitud:LiveData<Float> = _latitud

    private var _longitud= MutableLiveData<Float>()
    val longitud:LiveData<Float> = _longitud

    private val _imagenFr= MutableLiveData<String>()
    val imagenFr:LiveData<String> = _imagenFr

    private val _imagenTr= MutableLiveData<String>()
    val imagenTr:LiveData<String> = _imagenTr


    var fechaCreacion:LocalDate = LocalDate.now()

    fun limpiar(){
        _usuario.value = ""
        _password.value = ""
    }

    fun onLoginChanged(user: String, pass: String) {
        _usuario.value = user
        _password.value = pass
    }

    private fun isValidEmail(email: String): Boolean {
        return if(!email.isEmpty()){
            email === email
        }else{
            false
        }
    }

    private fun isValidPassword(password: String): Boolean = password.length > 4

    suspend fun agregarSolicitud(contexto:Context){
        var registro = nombre.value?.let {
            telefono.value?.let { it1 ->
                rut.value?.let { it2 ->
                    email.value?.let { it3 ->
                        id.value?.let { it4 ->
                            imagenFr.value?.let { it5 ->
                                imagenTr.value?.let { it6 ->
                                    longitud.value?.let { it7 ->
                                        latitud.value?.let { it8 ->
                                            Registro(id = it4.toInt(), nombre = it, rut = it2,
                                                email= it3, telefono = it1.toInt(), latitud = it8,
                                                longitud = it7, imagenFrontal = it5,
                                                imagenTrasera = it6, fechaCreacion = fechaCreacion.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        val BD:RegistroDao = BaseDatos.getInstance(contexto).RegistroDao()
        if (registro != null) {
            BD.getAll(registro)
        }
    }

    fun onNombreChanged(nombre:String){
        _nombre.value = nombre
    }
    fun onRutChanged(rut:String){
        _rut.value = rut
    }
    fun onEmailChanged(email:String){
        _email.value = email
    }
    fun onTelefonoChanged(telefono:String){
        _telefono.value = telefono
    }
    fun onLatitudChanged(lat:Float){
        _latitud.value = lat
    }
    fun onLongitudChanged(lon:Float){
        _longitud.value = lon
    }
    fun onImagenFrChanged(imFro:String){
        _imagenFr.value = imFro
    }
    fun onImagenTrChanged(imTra:String){
        _imagenTr.value = imTra
    }
}

