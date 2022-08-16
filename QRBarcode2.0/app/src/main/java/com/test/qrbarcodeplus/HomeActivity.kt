package com.test.qrbarcodeplus

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.test.qrbarcodeplus.barcodeSingleton.getInstance


class HomeActivity : AppCompatActivity(){

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10

        val bo: barcodeSingleton = getInstance()

        @JvmStatic fun get(context: Context): HomeActivity{
            return context.applicationContext as HomeActivity
        }
    }

    private lateinit var textureView: TextureView
    private lateinit var qrBtn: ImageButton
    //private lateinit var listsBtn: ImageButton
    private lateinit var settingsBtn: ImageButton
    private lateinit var nutritionBtn: ImageButton
   //private lateinit var socialBtn: ImageButton
    //private lateinit var shareBtn: ImageButton
    private lateinit var urlLink: TextView
    private lateinit var toast: Toast
    private lateinit var toast2: Toast
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textureView = findViewById(R.id.texture_view)
        settingsBtn = findViewById(R.id.imageButton6)
        qrBtn = findViewById(R.id.imageButton10)
        //listsBtn = findViewById(R.id.imageButton7)
        nutritionBtn = findViewById(R.id.imageButton12)
        //socialBtn = findViewById(R.id.imageButton15)
        //shareBtn = findViewById(R.id.imageButton20)
        urlLink = findViewById(R.id.textView12)

        qrBtn.setOnClickListener {
            val intQR = Intent(this, qrActivity::class.java)
            startActivity(intQR)
        }
/*
        listsBtn.setOnClickListener {
            val intLists = Intent(this, ListsActivity::class.java)
            startActivity(intLists)
        }
*/
        settingsBtn.setOnClickListener {
            val intSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intSettings)
        }

        nutritionBtn.setOnClickListener {
            val intNutrition = Intent(this, NutritionActivity::class.java)
            startActivity(intNutrition)
        }
/*
        socialBtn.setOnClickListener{
            val intSocial = Intent(this, SocialActivity::class.java)
            startActivity(intSocial)
        }

        shareBtn.setOnClickListener{

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Download the best QR/Barcode scanner on the Play Store. [QR/Barcode+ link]")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
*/
        if (isCameraPermissionGranted()) {
            textureView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }

    }


    private fun writeBarcodes(bc: String){
        //FIREBASE DATABASE
        val db = Firebase.firestore

        var userId: String = FirebaseAuth.getInstance().getCurrentUser()?.getUid() ?: "";

        val barcode = Barcode(bc, userId)

        bo.barcodeValue = bc;

        db.collection("codes").document("barcodes").set(barcode)
    }


    private fun startCamera() {
        val previewConfig = PreviewConfig.Builder()
                .setLensFacing(CameraX.LensFacing.BACK)
                .build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener { previewOutput ->
            val parent = textureView.parent as ViewGroup
            parent.removeView(textureView)
            parent.addView(textureView, 0)
            val surfaceTexture = previewOutput.surfaceTexture
            textureView.setSurfaceTexture(surfaceTexture)
        }

        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
                .build()
        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        //clickable false
        nutritionBtn.setEnabled(false);
        //socialBtn.setEnabled(false);

        var targetValue:String? = ""
        var targetType: Int
        var oldTV: String = ""

        //Function Type Lambda connected to QRCodeAnalyzer
        val qrCodeAnalyzer = QrCodeAnalyzer { qrCodes ->
            qrCodes.forEach {

                //Barcode number or QR Link
                targetValue = it.rawValue.toString()
                //Barcode value as String
                val barcodeStr = it.rawValue.toString()
                val QRStr = it.rawValue.toString()
                targetType = it.valueType

                if(targetType == 8) {//QR Code
                    //Only vibrate first time and on different codes
                    if(!targetValue.equals(oldTV)) {
                        vibratePhone()
                        urlLink.text = Html.fromHtml(targetValue)
                    }
                    oldTV = QRStr;//old target value
                }

                if( targetType == 5){//Barcode
                    //Only vibrate first time and on different codes
                    if(!targetValue.equals(oldTV)) {
                        //not clickable until successful scan
                        settingsBtn.setEnabled(true)
                        nutritionBtn.setEnabled(true)
                        //socialBtn.setEnabled(true)
                        vibratePhone()
                        writeBarcodes(barcodeStr)
                    }
                    oldTV = barcodeStr;
                }
            }
        }

        imageAnalysis.analyzer = qrCodeAnalyzer

        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
                ContextCompat.checkSelfPermission(baseContext, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                textureView.post { startCamera() }
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // API 26 or higher
            } else {
                vibrator.vibrate(100) // API 26 or lower
            }
        }
    }

}