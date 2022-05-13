package com.example.embedingsysterm2.ui.popview


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import com.example.embedingsysterm2.R


class RegisterView :PopupWindow () {
    private var mContext: Context?=null
    lateinit var view:View
    lateinit var btnRegister : Button
    lateinit var btnReturn : Button
    lateinit var name : EditText
    lateinit var mobile : EditText
    lateinit var password1 : EditText
    lateinit var password2 : EditText
    public fun CreateRegisterPopWindow(mContext:FragmentActivity,handlerOnClick:View.OnClickListener)
    {

        this.mContext=mContext

        this.view = LayoutInflater.from(mContext).inflate(R.layout.register_popview,null)
        name=view.findViewById(R.id.text_name)
        mobile=view.findViewById(R.id.text_mobile)
        password1=view.findViewById(R.id.text_password1)
        password2=view.findViewById(R.id.text_password2)
        btnReturn=view.findViewById(R.id.returnbtn)
        btnRegister=view.findViewById(R.id.Register)
        btnReturn.setOnClickListener {
            dismiss()
        }
        btnRegister.setOnClickListener(handlerOnClick)
        // 设置外部可点击
        this.setOutsideTouchable(true);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        /*
       * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
       * 对象,这样这可以以同样的方式改变这个Activity的属性.
       */
        mContext?.let {
            var dialogWindow = mContext.window
            var windowManger= mContext.windowManager

            var windowDPara=windowManger.defaultDisplay
            var windowPara=dialogWindow.attributes

            this.height=RelativeLayout.LayoutParams.WRAP_CONTENT
            this.width=(windowDPara.width*0.8).toInt()

            this.isFocusable=true
        }
    }

}
