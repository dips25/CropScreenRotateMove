package com.image.croprotatemoveimage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.layout.swiiiipe.myapplication.CropActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    var launcher: ActivityResultLauncher<*>? = null
    var photoFile: File? = null

    var camera: Button? = null
    var gallery: Button? = null
    var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera = findViewById<View>(R.id.camera) as Button
        gallery = findViewById<View>(R.id.gallery) as Button

        camera!!.setOnClickListener { v: View? ->
            onCameraClick()
        }

        gallery!!.setOnClickListener { v: View? ->
            pickMedia!!.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        }

        pickMedia =
            registerForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")

                    try {
                        //                            InputStream is = getContentResolver().openInputStream(uri);
//
//                            byte[]bytes = new byte[is.available()];
//
//                            is.read(bytes,0,bytes.length);


//                            byte[] bytes = new byte[2048];
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                            File file1 = new File(file , "Pics");
//                            if (!file1.exists()) {
//
//                                file1.mkdir();
//                            }
//
//                            File file2 = new File(file1 , "sample_img.png");
//                            if (!file2.exists()) {
//
//                                file2.createNewFile();
//                            }
//
//                            FileOutputStream fos = new FileOutputStream(file2);
//                            int i = 0;
//
//                            while ((i=(is.read(bytes))) !=-1) {
//
//                                baos.write(bytes , 0 , i);
//
//
//                            }
//
//                            baos.writeTo(fos);


                        val intent = Intent(
                            this,
                            CropActivity::class.java
                        )
                        intent.putExtra("data", uri.toString())
                        startActivity(intent)
                    } catch (ex: Exception) {

                    }
                } else {

                }
            }

        launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ActivityResultCallback<Map<String, Boolean>> { isGranted: Map<String, Boolean> ->
                if (isGranted.containsValue(false)) {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
                }
            })

        checkPermissions()
    }

    private fun onCameraClick() {
        dispatchTakePictureIntent()
    }

    private fun onGalleryClick() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        startActivityForResult(galleryIntent, GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val bitmap: Bitmap? = null
        val uri: Uri? = null
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //uri = data.getData();

            val intent = Intent(
                this,
                CropActivity::class.java
            )
            intent.putExtra("data", photoFile!!.path)
            startActivity(intent)
            //imageView.setImageBitmap(imageBitmap);
        } else if (requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            try {
                val galleryUri = getGalleryImage(uri!!)

                val `is` = contentResolver.openInputStream(Uri.parse(galleryUri))

                //bitmap = BitmapFactory.decodeStream(is);
            } catch (ex: Exception) {
            }
        }

        try {
            val `is` = contentResolver.openInputStream(uri!!)
            val bytes = ByteArray(2048)
            val baos = ByteArrayOutputStream()
            var i = 0

            while ((`is`!!.read(bytes).also { i = it }) != -1) {
                baos.write(bytes, 0, i)
            }

            val intent = Intent(
                this,
                CropActivity::class.java
            )
            intent.putExtra("data", baos.toByteArray())
            startActivity(intent)
        } catch (ex: Exception) {

        }
    }

    var currentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Ensure that there's a camera activity to handle the intent

        // Create the File where the photo should go
        photoFile = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                this,
                "com.layout.swiiiipe.myapplication.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun getGalleryImage(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var imagePath: String? = null

        try {
            contentResolver.query(uri, projection, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    imagePath = cursor.getString(columnindex)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imagePath
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val perms = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.CAMERA
            )

            launcher!!.launch(perms as Nothing)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            val perms = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.CAMERA


            )

            launcher!!.launch(perms as Nothing)
        } else {
            val perms = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )

            launcher!!.launch(perms as Nothing)
        }
    }

    companion object {
        private const val GALLERY_CODE = 200
        private const val REQUEST_IMAGE_CAPTURE = 100
    }
}