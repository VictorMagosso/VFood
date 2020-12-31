package com.victormagosso.vfood.activity.company

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.type.DateTime
import com.victormagosso.vfood.R
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.company.Product
import com.victormagosso.vfood.service.ProductService
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddProductActivity : AppCompatActivity() {

    lateinit var editName: EditText
    var editDescription: EditText? = null
    var editPrice: EditText? = null
    var btnUploadImg: Button? = null
    var availability: CheckBox? = null
    var productImg: ImageView? = null

    private val GALLERY_SELECTION = 200
    private val storageReference: StorageReference = FirebaseConfig().getFirebaseStorage()
    private val currentUserUID = FirebaseConfig().getFirebaseAuth().currentUser!!.uid
    private var imgUrl = ""
    var uEmail = FirebaseConfig().getFirebaseAuth().currentUser?.email
    var base64Custom = Base64Custom()
    var uid = base64Custom.encodeToBase64(uEmail!!)
    var status: Boolean = true
    var product = Product()
    var dbRef = FirebaseConfig().getFirebaseDatabase()
        .child("products")
        .child(uid!!)
    private val productService = ProductService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        editName = findViewById(R.id.editProductName)
        editDescription = findViewById(R.id.editDescription)
        editPrice = findViewById(R.id.editPrice)
        btnUploadImg = findViewById(R.id.btnUploadImg)
        availability = findViewById(R.id.checkAvailable)
        productImg = findViewById(R.id.imgProduct)

//        editName.addTextChangedListener {charSequence ->
//            try {
//
//            } catch ()
//        }

        btnUploadImg?.setOnClickListener {
            var i: Intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            if (i.resolveActivity(packageManager!!) != null) {
                startActivityForResult(i, GALLERY_SELECTION)
            }
        }

    }
    fun saveProduct(view: View) {
        var name = editName?.text.toString()
        var description = editDescription?.text.toString()
        var price = editPrice?.text.toString()
        if (!name.isNullOrEmpty()) {
            if (!description.isNullOrEmpty()) {
                if (!price.isNullOrEmpty()) {
                    product.cIdProduct = name.replace(" ", "+")
                    product.cDescription = description
                    product.cName = name
                    product.nPrice = price.toDouble()
                    product.cImgUrl = imgUrl
                    product.dDate = SimpleDateFormat().format(Date())

                    if (!availability?.isChecked!!) {
                        status = false
                    }

                    product.bAvailable = status
                    productService.saveProduct(product)
                    Toast.makeText(applicationContext, "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, CompanyActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Preencha o valor.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Preencha a descrição.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Preencha o nome.", Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack(view: View) {
        startActivity(Intent(applicationContext, CompanyActivity::class.java))
        finish()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                var img: Bitmap? = null

                try {
                    when (requestCode) {
                        GALLERY_SELECTION -> {
                            img = if (Build.VERSION.SDK_INT < 28) {
                                var imgLocal: Uri = data?.data!!
                                MediaStore.Images.Media.getBitmap(
                                    contentResolver,
                                    imgLocal
                                )
                            } else {
                                var imgLocal: Uri = data?.data!!
                                var source =
                                    ImageDecoder.createSource(contentResolver!!, imgLocal)
                                ImageDecoder.decodeBitmap(source)
                            }
                            if (img != null) {
                                productImg?.setImageBitmap(img)
                                var baos = ByteArrayOutputStream()
                                img.compress(Bitmap.CompressFormat.JPEG, 40, baos)
                                var imgData: ByteArray? = baos.toByteArray()

                                var imgRef: StorageReference = storageReference
                                    .child("images")
                                    .child("companies")
                                    .child("products")
                                    .child(uid!!)
                                    .child("$currentUserUID.jpeg")

                                var upload: UploadTask = imgRef.putBytes(imgData!!)

                                upload.addOnFailureListener(object : OnFailureListener {
                                    override fun onFailure(p0: Exception) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Erro ao salvar a imagem",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }).addOnSuccessListener {
                                    imgRef.downloadUrl.addOnCompleteListener { task: Task<Uri> ->
                                        var url: Uri = task.result!!
                                        imgUrl = url.toString()
                                    }
                                    Toast.makeText(
                                        applicationContext,
                                        "Imagem alterada com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}