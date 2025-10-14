package com.example.assignment3opsc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment3opsc.ui.groups.GroupsHostActivity
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.btnMovies).setOnClickListener {
            startActivity(Intent(this, MovieActivity::class.java))
        }
        findViewById<Button>(R.id.btnGroups).setOnClickListener {
            startActivity(Intent(this, GroupsHostActivity::class.java))
        }
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
