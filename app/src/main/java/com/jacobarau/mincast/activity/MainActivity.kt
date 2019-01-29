package com.jacobarau.mincast.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.jacobarau.mincast.R

class MainActivity : Activity() {
    val TAG = "MainActivity"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)
        val id = item.itemId
//        if (id == R.id.action_add_podcast) {
//
//            return true
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
