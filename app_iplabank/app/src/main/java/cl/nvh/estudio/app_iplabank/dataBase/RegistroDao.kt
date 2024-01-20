package cl.nvh.estudio.app_iplabank.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RegistroDao {

    @Query("SELECT * FROM Registro ORDER BY fechaCreacion DESC")
    suspend fun getAll(reg: Registro):List<Registro>

    @Query("SELECT * FROM Registro where id = :id")
    suspend fun findById(id:Int): Registro?

    @Insert
    suspend fun insert(registro: Registro)

    @Update
    suspend fun update(registro:Registro)

    @Delete
    suspend fun delete(registro: Registro)
}
