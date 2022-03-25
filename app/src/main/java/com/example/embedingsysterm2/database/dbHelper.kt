package com.example.embedingsysterm2.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class dbHelper(var context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version)
{
    private var createUser="create table Users (" +
            "uid integer primary key autoincrement," +
            "name text," +
            "mobile text," +
            "password text)"

    private var createLoginHistory="create table LoginHistory (" +
            "hid integer primary key autoincrement," +
            "uid text," +
            "logindate text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createUser)
        db?.execSQL(createLoginHistory)
        Toast.makeText(context,"Create Successed",Toast.LENGTH_LONG).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists Users")
        db?.execSQL("drop table if exists LoginHistory")
        onCreate(db)
    }
    private fun Toast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }
}