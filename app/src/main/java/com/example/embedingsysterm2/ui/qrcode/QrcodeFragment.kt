package com.example.embedingsysterm2.ui.qrcode

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.embedingsysterm2.R
import com.example.embedingsysterm2.databinding.QrViewBinding
import com.example.embedingsysterm2.zxing.activity.CaptureActivity
import com.example.embedingsysterm2.zxing.encoding.EncodingHandler
import com.google.zxing.WriterException

class QrcodeFragment : Fragment() {

    private var _binding: QrViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var resultTextView: TextView? = null
    private var qrStrEditText: EditText? = null
    private var qrImgImageView: ImageView? = null
    lateinit var scanBarCodeButton :Button
    lateinit var generateQRCodeButton :Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QrViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        scanBarCodeButton=root.findViewById(R.id.btn_scan_barcode)
        generateQRCodeButton=root.findViewById(R.id.btn_add_qrcode)
        resultTextView = root.findViewById(R.id.tv_scan_result)
        qrStrEditText = root.findViewById(R.id.et_qr_string)
        qrImgImageView = root.findViewById(R.id.iv_qr_image)

        scanBarCodeButton.setOnClickListener{
            val openCameraIntent = Intent(this.context, CaptureActivity::class.java)
            startActivityForResult(openCameraIntent, 0)
        }
        generateQRCodeButton.setOnClickListener{
            try {
                val contentString = qrStrEditText?.getText().toString()
                if (contentString != "") {
                    val qrCodeBitmap: Bitmap = EncodingHandler.createQRCode(contentString, 350)
                    qrImgImageView?.setImageBitmap(qrCodeBitmap)
                } else {
                    Toast.makeText(
                        this.context,
                        "Text can not be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: WriterException) {
                e.printStackTrace()
            }

        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val bundle = data!!.extras
            val scanResult = bundle!!.getString("result")
            resultTextView!!.text = scanResult
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}