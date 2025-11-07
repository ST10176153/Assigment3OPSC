package com.example.assignment3opsc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.assignment3opsc.data.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    private lateinit var tvName: TextView
    private lateinit var tvHandle: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvGroupsCount: TextView
    private lateinit var tvFavouritesCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvHandle = findViewById(R.id.tvHandle)
        tvEmail = findViewById(R.id.tvEmail)
        tvGroupsCount = findViewById(R.id.tvGroupsCount)
        tvFavouritesCount = findViewById(R.id.tvFavouritesCount)

        // Load email and profile from Firestore
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        tvEmail.text = user.email ?: ""

        loadProfile(user.uid)
        loadStats(user.email ?: "", user.uid)

        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            showEditDialog(user.uid)
        }
        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { snap ->
                val name = snap.getString("username")
                    ?: auth.currentUser?.email?.substringBefore("@")
                    ?: "User"
                tvName.text = name
                tvHandle.text = "@${name.lowercase().replace(" ", "_")}"
            }
            .addOnFailureListener {
                val fallback = auth.currentUser?.email?.substringBefore("@") ?: "User"
                tvName.text = fallback
                tvHandle.text = "@${fallback.lowercase()}"
            }
    }

    private fun showEditDialog(uid: String) {
        val view = layoutInflater.inflate(R.layout.dialog_edit_profile, null, false)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etHandle = view.findViewById<EditText>(R.id.etHandle)

        etName.setText(tvName.text)
        etHandle.setText(tvHandle.text?.removePrefix("@"))

        AlertDialog.Builder(this)
            .setTitle("Edit Profile")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val newName = etName.text.toString().trim()
                val newHandle = etHandle.text.toString().trim()
                if (newName.isEmpty()) {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val updates = hashMapOf(
                    "username" to newName,
                    "handle" to "@${newHandle.ifEmpty { newName.lowercase().replace(" ", "_") }}"
                )
                db.collection("users").document(uid).set(updates, com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener {
                        tvName.text = newName
                        tvHandle.text = updates["handle"]
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadStats(userEmail: String, uid: String) {
        // Favourites from Firestore
        db.collection("favorites")
            .whereEqualTo("userID", userEmail)
            .get()
            .addOnSuccessListener { res -> tvFavouritesCount.text = res.size().toString() }
            .addOnFailureListener { tvFavouritesCount.text = "0" }

        // Groups from Room
        lifecycleScope.launch {
            val count = withContext(Dispatchers.IO) {
                AppDatabase.get(this@ProfileActivity).groupDao().countNow()
            }
            tvGroupsCount.text = count.toString()
        }

        // ProfileActivity.kt
        private val pickImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri ?: return@registerForActivityResult
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@registerForActivityResult
            val ref = FirebaseStorage.getInstance().reference.child("profiles/$uid.jpg")
            ref.putFile(uri)
                .continueWithTask { ref.downloadUrl }
                .addOnSuccessListener { url ->
                    FirebaseFirestore.getInstance().collection("users").document(uid)
                        .update("photoUrl", url.toString())
                    // load with Picasso/Coil/Glide into ImageView
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        findViewById<Button>(R.id.btnChangePhoto).setOnClickListener {
            pickImage.launch("image/*")
        }

    }
}
