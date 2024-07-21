package com.progetto.passwordmanager.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Utenze")
data class ServiceCredential(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "Service") val Service: String,
    @ColumnInfo(name = "User") val User: String,
    @ColumnInfo(name = "Password") val Password: String
)