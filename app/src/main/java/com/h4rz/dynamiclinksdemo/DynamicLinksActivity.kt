package com.h4rz.dynamiclinksdemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DynamicLinksActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, toString: String) {
            val intent = Intent(context, DynamicLinksActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_links)
    }
}