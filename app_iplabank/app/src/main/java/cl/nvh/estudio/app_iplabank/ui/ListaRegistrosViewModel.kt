package cl.nvh.estudio.app_iplabank.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.nvh.estudio.app_iplabank.Aplicacion
import cl.nvh.estudio.app_iplabank.dataBase.Registro
import cl.nvh.estudio.app_iplabank.dataBase.RegistroDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaRegistrosViewModel(private val registroDao: RegistroDao) : ViewModel() {

    var listRegistros by mutableStateOf(listOf<Registro>())

    fun insertRegistros(registro: Registro){
        viewModelScope.launch(Dispatchers.IO) {
            registroDao.insert(registro)
            getRegistros()
        }
    }

    fun getRegistros(): List<Registro> {
        viewModelScope.launch(Dispatchers.IO) {
            listRegistros = registroDao.getAll(reg)
        }
        return listRegistros
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                ListaRegistrosViewModel(aplicacion.registroDao)
            }
        }
    }
}
