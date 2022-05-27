package com.example.embedingsysterm2.ui.popview

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.example.embedingsysterm2.GlobalVariable.GlobalVariable
import com.example.embedingsysterm2.R
import com.example.embedingsysterm2.database.dbHelper
import com.example.embedingsysterm2.date.user
import com.example.embedingsysterm2.sensorSocket.NfcDetect
import com.example.embedingsysterm2.zxing.encoding.EncodingHandler

class QrUserGeneratorView : PopupWindow() {
    private var mContext: Context?=null
    lateinit var imageView:ImageView
    lateinit var NFCbinderBtn:Button
    lateinit var view: View

    public fun CreateRegisterPopWindow(mContext: FragmentActivity,contentString:String,User: user)
    {

        this.mContext=mContext
        this.view = LayoutInflater.from(mContext).inflate(R.layout.qr_user_generator_popview,null)
        imageView=view.findViewById(R.id.qrCodeImage)
        NFCbinderBtn=view.findViewById(R.id.NFCbinderBtn)
        imageView.setOnClickListener {
            dismiss()
        }
        NFCbinderBtn?.setOnClickListener {
            val dbHelper = dbHelper(mContext, "UsersStore.db", 4)
            dbHelper.bindNFCmesg(User,GlobalVariable.NFCid)
            Toast.makeText(mContext, "NFC 绑定成功", Toast.LENGTH_SHORT).show()
        }
        if (contentString != "") {
            val qrCodeBitmap: Bitmap = EncodingHandler.createQRCode(contentString, 800)
            imageView?.setImageBitmap(qrCodeBitmap)
        }
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

            this.height= RelativeLayout.LayoutParams.FILL_PARENT
            this.width=RelativeLayout.LayoutParams.FILL_PARENT

            this.isFocusable=true
        }
    }
}