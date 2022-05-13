package com.example.embedingsysterm2.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.embedingsysterm2.JNI_interface.LedJniObject
import com.example.embedingsysterm2.JNI_interface.LedTubeObject
import com.example.embedingsysterm2.R
import com.example.embedingsysterm2.database.dbHelper
import com.example.embedingsysterm2.databinding.LoginViewBinding
import com.example.embedingsysterm2.date.user
import com.example.embedingsysterm2.sensorSocket.LatticeScreen
import com.example.embedingsysterm2.sensorSocket.NfcDetect
import com.example.embedingsysterm2.sensorSocket.SteppingMotor
import com.example.embedingsysterm2.sensorSocket.TrafficLight
import com.example.embedingsysterm2.zxing.activity.CaptureActivity
import kotlin.concurrent.thread

class LoginFragment : Fragment() {

    private var _binding:LoginViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var btnLogin: Button
    private lateinit var btnOpencamera: Button
    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginList: ListView
    private var Led:LedJniObject?=null;
    private var LedTube:LedTubeObject?=null;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        SteppingMotor.open()
        NfcDetect.init()
        var handler= object : NfcDetect.NfcCallback {
            override fun onMesgDect(obj: ByteArray?) {
                obj?.let{
                    println("+++++"+obj);
                }
            }
        }
        NfcDetect.setHandler(handler)
        NfcDetect.open()

        LatticeScreen.open()
        TrafficLight.open()
        _binding = LoginViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnLogin=root.findViewById(R.id.Login)
        btnOpencamera=root.findViewById(R.id.btn_scan_barcode)
        nameEditText = root.findViewById(R.id.text_name)
        loginList = root.findViewById(R.id.querylist)
        passwordEditText = root.findViewById(R.id.text_password)
        passwordEditText.setInputType(129);//设置为隐藏密码
        Led=LedJniObject()
        Led?.open()
        LedTube= LedTubeObject()
        this.context?.let {
            val dbHelper = dbHelper(it, "UsersStore.db", 3)
            btnLogin?.setOnClickListener {
                val db = dbHelper.writableDatabase
                var User:user=user()
                val name:String=nameEditText.text.toString()
                val password:String=passwordEditText.text.toString()
                val res=db.query("Users",arrayOf("uid","password","mobile","name"),
                    "name=? and password=?",arrayOf(name,password),null,null,null,null)
                User.name=name
                User.password=password
                val userId=dbHelper.getUserLoginId(User)
                res.close()
                if(userId!=null)
                {
                    User.Id=userId
                    Toast.makeText(context, User.name+",登陆成功", Toast.LENGTH_SHORT).show()
                    dbHelper.insertUser2LoginHistory(User)
                    var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        dbHelper.getUserLoginHistory(User)
                    )
                    loginList.setAdapter(adapter)
                    thread {
                        LatticeScreen.write(null)
                    }
                    thread{
                        for(i in 0..17)
                        {
                            TrafficLight.light(i%3);
                            SystemClock.sleep(200)
                        }
                    }

                    thread{
                        NfcDetect.read()
                    }
                    thread{
                        SteppingMotor.turnRight()
                        SystemClock.sleep(5000)
                        SteppingMotor.turnLeft()
                        SystemClock.sleep(5000)
                        SteppingMotor.stop()
                    }
                    thread {

                        for (j in 0..5) {
                            for (i in 0..3) {
                                Led?.light(i);
                                SystemClock.sleep(100);
                                Led?.light(i);
                            }
                        }
                        for (j in 0..15) {
                            for (i in 0..3) {
                                Led?.light(i);
                            }
                            SystemClock.sleep(100);
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show()
                }
            }
            btnOpencamera?.setOnClickListener{
                val openCameraIntent = Intent(this.context, CaptureActivity::class.java)
                startActivityForResult(openCameraIntent, 0)
            }



        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Led?.close()
        LatticeScreen.stop()
        NfcDetect.stop()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val bundle = data!!.extras
            val scanResult = bundle!!.getString("result")
            this.context?.let{
                val dbHelper = dbHelper(it, "UsersStore.db", 3)
                scanResult?.let{
                   val User=dbHelper.getUserFromJSON(scanResult)
                    if(User.Id!=null&&User.Id!=-1)
                    {
                        Toast.makeText(context, User.name+",登陆成功", Toast.LENGTH_SHORT).show()
                        dbHelper.insertUser2LoginHistory(User)
                        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            dbHelper.getUserLoginHistory(User)
                        )
                        loginList.setAdapter(adapter)
                    }
                    else
                    {
                        Toast.makeText(context, "登陆失败", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }
    }

}