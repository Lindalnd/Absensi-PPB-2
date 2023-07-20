package com.lindainaya.absensippb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class CameraViewModel : ViewModel() {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun captureImage(byteData: ByteArray) {
        val fileName = "foto_${System.currentTimeMillis()}.jpg"

        // Path referensi file di Firebase Storage
        val imageRef = storageRef.child("images/$fileName")

        // Upload foto ke Firebase Storage
        val uploadTask = imageRef.putBytes(byteData)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Foto berhasil diunggah
            // Dapatkan URL unduhan file
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                saveImageUrlToFirestore(imageUrl)
            }.addOnFailureListener { exception ->
                // Terjadi kesalahan saat mendapatkan URL unduhan
                exception.printStackTrace()
            }
        }.addOnFailureListener { exception ->
            // Terjadi kesalahan saat mengunggah foto
            exception.printStackTrace()
        }
    }

    private fun saveImageUrlToFirestore(imageUrl: String) {
        val data = hashMapOf(
            "imageUrl" to imageUrl
        )

        firestore.collection("photos")
            .add(data)
            .addOnSuccessListener { documentReference ->
                // Foto berhasil disimpan di Firestore
                Log.d(TAG, "Foto berhasil disimpan di Firestore dengan ID: ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                // Terjadi kesalahan saat menyimpan foto di Firestore
                exception.printStackTrace()
            }
    }

    companion object {
        private const val TAG = "CameraViewModel"
    }
}

