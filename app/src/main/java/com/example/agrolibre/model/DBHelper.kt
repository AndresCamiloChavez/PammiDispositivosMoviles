package com.example.agrolibre.model

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.fragment.app.FragmentActivity

class DBHelper(context: FragmentActivity?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "info"
    }

    //PASO 6.1 CREACION de una tabla

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE " + Tables.information.TABLE_NAME +  " (" +
                Tables.information._id + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                Tables.information._nombre + " TEXT NOT NULL, " +
                Tables.information._Direccion + " TEXT NOT NULL, " +
                Tables.information._correo + " TEXT NOT NULL, " +
                Tables.information._telefono + " TEXT NOT NULL)")
    }
    //PASO 6 Configuracion de versiones de la BD
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val orderErase = "DROP TABLE IF EXISTS " + Tables.information.TABLE_NAME
        db!!.execSQL(orderErase)
        onCreate(db)
    }
    //PASO 7 añadir valores a la tabla
    fun insert(name:String, address:String, email:String, phone:String){
        val data = ContentValues()
        data.put(Tables.information._nombre,name)
        data.put(Tables.information._Direccion,address)
        data.put(Tables.information._correo,email)
        data.put(Tables.information._telefono,phone)
        val db = this.writableDatabase

        db.insert(Tables.information.TABLE_NAME, null, data)
        db.close()
    }
    //Paso 13 modificar esos valores ingresados en la tabla de la base de datos
    fun edit(name:String, address:String, email:String, phone:String){
        val num = 1
        val args = arrayOf(num.toString())

        val data = ContentValues()
        data.put(Tables.information._nombre,name)
        data.put(Tables.information._Direccion,address)
        data.put(Tables.information._correo,email)
        data.put(Tables.information._telefono,phone)
        val db = this.writableDatabase

        db.update(Tables.information.TABLE_NAME, data," _id = ?", args)
        db.close()
    }
}