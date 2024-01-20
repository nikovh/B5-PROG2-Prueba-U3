package cl.nvh.estudio.app_iplabank.dataBase


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Registro(
    @PrimaryKey(autoGenerate = true) val id:Int,
    var nombre:String,
    var rut:String,
    var email:String,
    var telefono: Int,
    var latitud:Float,
    var longitud:Float,
    var imagenFrontal:String,
    var imagenTrasera:String,
    var fechaCreacion: String
)
