package com.example.embedingsysterm2.database
import android.content.ContentValues
import android.database.*
import android.database.sqlite.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.embedingsysterm2.R
import androidx.fragment.app.Fragment
import com.example.embedingsysterm2.databinding.DbContrlBinding
class db :Fragment(){
    lateinit var btnCreateDataBase : Button//数据库创建按钮控件
    lateinit var btnAddData : Button//数据库添加元素按钮控件
    lateinit var btnUpdateData : Button//数据库更新元素按钮控件
    lateinit var btnDeleteData : Button//数据库删除元素按钮控件
    lateinit var btnQueryData : Button//数据库查询元素按钮控件

    private var _binding: DbContrlBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{

        _binding = DbContrlBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnCreateDataBase=root.findViewById(R.id.btnCreateDataBase)
        btnAddData=root.findViewById(R.id.btnAddData)
        btnUpdateData=root.findViewById(R.id.btnUpdateData)
        btnDeleteData=root.findViewById(R.id.btnDeleteData)
        btnQueryData=root.findViewById(R.id.btnQueryData)
        this.context?.let {
            val dbHelper=dbHelper(it,"AdminUsrStore.db",1)

            btnCreateDataBase.setOnClickListener {
                dbHelper.writableDatabase
            }
            btnAddData.setOnClickListener {
                val db=dbHelper.writableDatabase
                val Values=ContentValues().apply {
                    put("name","The Da Vinci Code")
                    put("password","123456789")
                }
                db.insert("AdminUsr",null,Values)
            }
            btnUpdateData.setOnClickListener {
                val db=dbHelper.writableDatabase
                val values=ContentValues()
                values.put("password","123456789")
                db.update("AdminUsr",values,"name=?", arrayOf("The Da Vinci Code"))
            }
            btnDeleteData.setOnClickListener {
                val db=dbHelper.writableDatabase
                db.delete("AdminUsr","name=?", arrayOf("The Da Vinci Code"))
            }
            btnQueryData.setOnClickListener {
                val db=dbHelper.writableDatabase
                val cursor=db.query("AdminUsr",null,null,null,null,null,null)
                if (cursor.moveToFirst()){
                    do {
                        var name:String= String()
                        var password:String= String()
                        cursor.getColumnIndex("name")?.let {
                            name=cursor.getString(it)
                            Log.d("MainActivity","name name is "+name)
                        }
                        cursor.getColumnIndex("password")?.let {
                            password=cursor.getString(it)
                            Log.d("MainActivity","password is "+password)
                        }
                        Toast.makeText(this.context ,name+" : "+password,Toast.LENGTH_LONG).show()
                    }while (cursor.moveToNext())
                }
                cursor.close()
            }

        }

        return root
    }


}