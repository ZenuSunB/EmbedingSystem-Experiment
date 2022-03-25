package com.example.embedingsysterm2.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class dbHelper(var context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version)
{
    public var createDB="create table AdminUsr (" +
            "id integer primary key autoincrement," +
            "name text," +
            "password text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createDB)
        Toast.makeText(context,"Create Successed",Toast.LENGTH_LONG).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists AdminUsr")
        onCreate(db)
    }
    private fun Toast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }
}