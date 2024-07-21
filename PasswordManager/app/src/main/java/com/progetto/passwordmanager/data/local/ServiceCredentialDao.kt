package com.progetto.passwordmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceCredentialDao {

    @Query("SELECT * FROM Utenze")
    suspend fun getUtenze(): List<ServiceCredential>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUtenza(newServiceCredential: ServiceCredential)

    @Delete
    suspend fun deleteUtenza(serviceCredential: ServiceCredential)

    @Query("UPDATE Utenze SET Service = :service, User = :user, Password = :password WHERE " +
            "Service = :oldService AND User = :oldUser AND Password = :oldPassword")
    suspend fun updateUtenza(
        service: String,
        user: String,
        password: String,
        oldService: String,
        oldUser: String,
        oldPassword: String
    )
}