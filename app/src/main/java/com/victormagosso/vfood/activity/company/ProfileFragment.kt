package com.victormagosso.vfood.activity.company

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.victormagosso.vfood.R
import com.victormagosso.vfood.config.FirebaseConfig
import com.victormagosso.vfood.helper.Base64Custom
import com.victormagosso.vfood.model.company.Company
import com.victormagosso.vfood.repository.CompanyRepository
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ProfileFragment : Fragment() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val GALLERY_SELECTION = 200
    private val storageReference: StorageReference = FirebaseConfig().getFirebaseStorage()
    private val currentUserUID = FirebaseConfig().getFirebaseAuth().currentUser!!.uid
    var company = Company()
    private val companyRepository = CompanyRepository()
    private var imgUrl = ""

    var editCompanyName: EditText? = null
    private var imageChangeImg: ImageView? = null
    var editMinTime: EditText? = null
    var editMaxTime: EditText? = null
    var editAddress: EditText? = null
    var editCategory: EditText? = null
    var editPhone: EditText? = null
    var btnSaveChanges: Button? = null
    var imageCompanyImg: ImageView? = null

    var uEmail = FirebaseConfig().getFirebaseAuth().currentUser?.email
    var base64Custom = Base64Custom()
    var uid = base64Custom.encodeToBase64(uEmail!!)
    private var dCreated: String? = ""
    private var cDocument: String? = ""

    var dbRef = FirebaseConfig().getFirebaseDatabase()
        .child("companies")
        .child(uid!!)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var profileView: View = inflater.inflate(R.layout.fragment_profile, container, false)

        imageChangeImg = profileView?.findViewById(R.id.changeImg)
        imageCompanyImg = profileView?.findViewById(R.id.imgCompany)
        editCompanyName = profileView?.findViewById(R.id.editCompanyName)
        editMinTime = profileView?.findViewById(R.id.editMinTime)
        editMaxTime = profileView?.findViewById(R.id.editMaxTime)
        editCategory = profileView?.findViewById(R.id.editCategory)
        editAddress = profileView?.findViewById(R.id.editAddress)
        editPhone = profileView?.findViewById(R.id.editPhone)
        btnSaveChanges = profileView?.findViewById(R.id.btnSaveChanges)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value != null) {
                    var company: Company = snapshot.getValue(Company::class.java)!!
                    editCompanyName?.text = Editable.Factory.getInstance().newEditable(company.cCompanyName)
                    editCategory?.text = Editable.Factory.getInstance().newEditable(company.cCategory)
                    editPhone?.text = Editable.Factory.getInstance().newEditable(company.cPhone)
                    editAddress?.text = Editable.Factory.getInstance().newEditable(company.cAddress)
                    editMinTime?.text = Editable.Factory.getInstance().newEditable(company.cTime?.take(2))
                    editMaxTime?.text = Editable.Factory.getInstance().newEditable(company.cTime?.takeLast(2))
                    dCreated = company.dDate
                    cDocument = company.cDocument

                    imgUrl = company.cCompanyImg!!
                    if (imgUrl != "") {
                        Picasso
                            .get()
                            .load(imgUrl)
                            .into(imgCompany)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("not")
            }
        })

        imageChangeImg?.setOnClickListener {
            var i: Intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            if (i.resolveActivity(activity?.packageManager!!) != null) {
                startActivityForResult(i, GALLERY_SELECTION)
            }
        }

        btnSaveChanges?.setOnClickListener {
                var companyName: String = editCompanyName?.text.toString()
                var category: String = editCategory?.text.toString()
                var min: String = editMinTime?.text.toString()
                var max: String = editMaxTime?.text.toString()
                var address: String = editAddress?.text.toString()
                var phone: String = editPhone?.text.toString()

                if (!companyName.isNullOrEmpty() &&
                    !category.isNullOrEmpty() &&
                    !min.isNullOrEmpty() &&
                    !max.isNullOrEmpty() &&
                    !address.isNullOrEmpty() &&
                    !phone.isNullOrEmpty()
                ) {
                    company.cId = base64Custom.encodeToBase64(uEmail!!)
                    company.cCompanyName = companyName
                    company.cEmail = uEmail
                    company.cCategory = category
                    company.cTime = "$min - $max"
                    company.cAddress = address
                    company.cPhone = phone
                    company.dDate = dCreated
                    company.cDocument = cDocument
                    company.cCompanyImg = imgUrl
                    companyRepository.saveCompany(company)

                    Toast.makeText(activity?.applicationContext, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Por favor, preencha os campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        var logout = profileView?.findViewById<Button>(R.id.btnLogoutCompany)

        logout?.setOnClickListener {
            auth.signOut()
            activity?.finish()
        }

        return profileView
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
                                    activity?.contentResolver,
                                    imgLocal
                                )
                            } else {
                                var imgLocal: Uri = data?.data!!
                                var source =
                                    ImageDecoder.createSource(activity?.contentResolver!!, imgLocal)
                                ImageDecoder.decodeBitmap(source)
                            }
                            if (img != null) {
                                imageCompanyImg?.setImageBitmap(img)
                                var baos = ByteArrayOutputStream()
                                img.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                                var imgData: ByteArray? = baos.toByteArray()

                                var imgRef: StorageReference = storageReference
                                    .child("images")
                                    .child("companies")
                                    .child("profile")
                                    .child(uid!!)
                                    .child("$currentUserUID.jpeg")

                                var upload: UploadTask = imgRef.putBytes(imgData!!)

                                upload.addOnFailureListener(object : OnFailureListener {
                                    override fun onFailure(p0: Exception) {
                                        Toast.makeText(
                                            activity?.applicationContext,
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
                                        activity?.applicationContext,
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