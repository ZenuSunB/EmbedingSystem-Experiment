package com.example.embedingsysterm2.database


import android.content.ContentValues

import android.os.Bundle

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.embedingsysterm2.R
import com.example.embedingsysterm2.databinding.DbContrlBinding
import com.example.embedingsysterm2.databinding.QrUserGeneratorPopviewBinding
import com.example.embedingsysterm2.date.user
import com.example.embedingsysterm2.ui.popview.QrUserGeneratorView
import com.example.embedingsysterm2.ui.popview.RegisterView
import org.json.JSONObject


class db :Fragment() {
    lateinit var btnAddData: Button//数据库添加元素按钮控件
    lateinit var btnUpdateData: Button//数据库更新元素按钮控件
    lateinit var btnDeleteData: Button//数据库删除元素按钮控件
    lateinit var btnQueryData: Button//数据库查询元素按钮控件

    lateinit var queryList: ListView
    lateinit var btnRegister: Button//用户注册元素按钮控件
    private var _binding: DbContrlBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DbContrlBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnAddData = root.findViewById(R.id.btnAddData)
        btnUpdateData = root.findViewById(R.id.btnUpdateData)
        btnDeleteData = root.findViewById(R.id.btnDeleteData)
        btnQueryData = root.findViewById(R.id.btnQueryData)

        btnRegister = root.findViewById(R.id.Register)
        queryList = root.findViewById(R.id.querylist)

        this.context?.let {
            val dbHelper = dbHelper(it, "UsersStore.db", 4)
            btnAddData.setOnClickListener {
                val db = dbHelper.writableDatabase
                val Values = ContentValues().apply {
                    put("name", "The Da Vinci Code")
                    put("mobile", "12345679")
                    put("password", "123456789")
                    put("NFCmesg", "")
                }
                db.insert("Users", null, Values)
            }
            btnUpdateData.setOnClickListener {
                val db = dbHelper.writableDatabase
                val Values = ContentValues()
                Values.put("password", "7654321")
                db.update("Users", Values, "name=?", arrayOf("The Da Vinci Code"))
            }
            btnDeleteData.setOnClickListener {
                val db = dbHelper.writableDatabase
                db.delete("Users", null, null)
            }
            btnQueryData.setOnClickListener {
                val db = dbHelper.writableDatabase
                var queryList_str: MutableList<String> = arrayListOf()
                val cursor = db.query("Users", null, null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    do {
                        var name: String = String()
                        var moblie: String = String()
                        var password: String = String()
                        var NFCmesg: String = String()
                        var uid:Int=-1;
                        cursor.getColumnIndex("uid")?.let {
                            uid = cursor.getInt(it)
                        }
                        cursor.getColumnIndex("name")?.let {
                            name = cursor.getString(it)
                        }
                        cursor.getColumnIndex("password")?.let {
                            password = cursor.getString(it)
                        }
                        cursor.getColumnIndex("mobile")?.let {
                            moblie = cursor.getString(it)
                        }
                        cursor.getColumnIndex("NFCmesg")?.let {
                            NFCmesg = cursor.getString(it)
                        }
                        queryList_str.add("{"+"\"uid\":\""+uid+"\"," +
                                "\n\"name\":\"" + name + "\"," +
                                "\n\"mobile\":\""+moblie+"\"," +
                                "\n\"password\":\"" + password+"\"," +
                                "\n\"NFCmesg\":\""+NFCmesg+"\"}")
                    } while (cursor.moveToNext())
                }
                cursor.close()
                var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    queryList_str
                )
                queryList.setAdapter(adapter)
            }
            btnRegister.setOnClickListener {
                val registerView = RegisterView()
                activity?.let {
                    registerView.CreateRegisterPopWindow(it, View.OnClickListener {
                        val name = registerView.name.getText().toString().trim()
                        val mobile = registerView.mobile.getText().toString().trim()
                        val password1 = registerView.password1.getText().toString().trim()
                        val password2 = registerView.password2.getText().toString().trim()
                        if(password1==password2)
                        {
                            val db = dbHelper.writableDatabase
                            val Values = ContentValues().apply {
                                put("name", name)
                                put("mobile", mobile)
                                put("password", password1)
                                put("NFCmesg", String())
                            }
                            db.insert("Users", null, Values)
                            Toast.makeText(this.context, "注册成功", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            Toast.makeText(this.context, "两次输入密码不一致", Toast.LENGTH_LONG).show()
                        }
                    })
                    registerView.showAtLocation(
                        root.findViewById(R.id.linearLayout),
                        Gravity.CENTER,
                        0,
                        0
                    )
                }
            }
            queryList.setOnItemClickListener { adapterView, view, position, id ->
                val listView = adapterView as ListView
                val listAdapter = listView.adapter
                val choosed: String = listAdapter.getItem(position)  as String
                val obj=JSONObject(choosed)
                var User:user=user(obj.getInt("uid"),obj.getString("name"),obj.getString("mobile"),obj.getString("password"))
                val userQrCodePopView = QrUserGeneratorView()
                activity?.let {
                    userQrCodePopView.CreateRegisterPopWindow(it,choosed,User)
                    userQrCodePopView.showAtLocation(
                        root.findViewById(R.id.linearLayout),
                        Gravity.CENTER,
                        0,
                        0
                    )
                }
            }
        }
        return root
    }
}


