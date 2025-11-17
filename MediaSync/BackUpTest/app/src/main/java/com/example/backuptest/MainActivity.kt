package com.example.backuptest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val permissions = arrayOf(
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.READ_MEDIA_VIDEO
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.userinterface)

        requestPermissions(permissions, 2000)

        val run = findViewById<Button>(R.id.button)
        run.setOnClickListener {
            backupAllMedia()
        }
    }


    fun getAllImages(): List<Uri> {
        val images = mutableListOf<Uri>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID
        )

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                images.add(uri)
            }
        }

        return images
    }


    fun getAllVideos(): List<Uri> {
        val videos = mutableListOf<Uri>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID
        )

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Video.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val uri = Uri.withAppendedPath(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                videos.add(uri)
            }
        }

        return videos
    }


    fun uploadFileToServer(uri: Uri) {
        Thread {
            try {
                val inputStream = contentResolver.openInputStream(uri)
                if (inputStream == null) return@Thread

                val requestBody = inputStream.readBytes().toRequestBody()

                val request = Request.Builder()
                    .url("http://YOUR_LAN_IPADDRESS:12000/upload")   // <-- Change accordingly
                    .addHeader("filename", getFileName(uri))
                    .post(requestBody)
                    .build()

                val client = OkHttpClient()
                val response = client.newCall(request).execute()

                println("Upload response: ${response.body?.string()}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
    fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                return it.getString(nameIndex)
            }
        }
        return "unknown_file"
    }

    fun backupAllMedia() {
        val images = getAllImages()
        val videos = getAllVideos()

        // Upload images
        for (uri in images) {
            uploadFileToServer(uri)
        }

        // Upload videos
        for (uri in videos) {
            uploadFileToServer(uri)
        }
    }



}
