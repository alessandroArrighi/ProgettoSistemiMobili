package com.progetto.passwordmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(ServiceCredential::class), version = 1, exportSchema = false)
public abstract class ServiceCredentialRoomDatabase : RoomDatabase() {
    abstract fun serviceCredentialDao(): ServiceCredentialDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ServiceCredentialRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ServiceCredentialRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ServiceCredentialRoomDatabase::class.java,
                    "Utenze"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}