package com.example.embedingsysterm2.database

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.embedingsysterm2.date.user
import org.json.JSONObject
import java.sql.Date
import java.text.SimpleDateFormat

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

    public fun insertUser2LoginHistory(User:user):Int
    {
        val db = this.writableDatabase
        val date=Date(System.currentTimeMillis())
        val dateStr=SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss").format(date)
//        Toast(dateStr)
        val Values = ContentValues().apply {
            put("uid", User.Id.toString())
            put("logindate", dateStr)
        }
        return db.insert("LoginHistory", null, Values).toInt()
    }

    public fun getUserLoginHistory(User:user):MutableList<String>
    {
        val db = this.writableDatabase
        var queryList_str: MutableList<String> = arrayListOf()
        val cursor = db.query("Users NATURAL JOIN LoginHistory",
            null, "uid=?", arrayOf(User.Id.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            do {
                var hid:String=String()
                var name: String = String()
                var logindate:String= String()
                cursor.getColumnIndex("hid")?.let {
                    hid = cursor.getString(it)
                }
                cursor.getColumnIndex("name")?.let {
                    name = cursor.getString(it)
                }
                cursor.getColumnIndex("logindate")?.let {
                    logindate = cursor.getString(it)
                }
                queryList_str.add("hid:" + hid + "\nname:"+name+"\nlogindate:" + logindate)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return queryList_str
    }
    public fun getUserLoginId(User:user):Int? {
        val db = this.writableDatabase
        val res = db.query(
            "Users", arrayOf("uid", "password", "mobile", "name"),
            "name=? and password=?", arrayOf(User.name, User.password), null, null, null, null
        )
        if (res.moveToFirst())
        {
            res.getColumnIndex("uid")?.let {
                return res.getInt(it)
            }
        }
        return null;
    }
    public fun getUserFromJSON(JsonStr:String):user
    {
        var User:user=user()
        var obj=JSONObject(JsonStr)
        User.name=obj.getString("name")
        User.password=obj.getString("password")
        if(User.name!=null&&User.password!=null)
        {
            getUserLoginId(User)?.let {
                User.Id=it
            }
        }
        return User
    }
}