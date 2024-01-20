package cl.nvh.estudio.app_iplabank

import android.app.Application
import androidx.room.Room
import cl.nvh.estudio.app_iplabank.dataBase.BaseDatos

class Aplicacion : Application() {

    val db by lazy { Room.databaseBuilder(this, BaseDatos::class.java, "registros.db").build() }
    val registroDao by lazy { db.RegistroDao() }
}