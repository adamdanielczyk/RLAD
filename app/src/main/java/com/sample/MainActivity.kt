package com.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.core.actions.Actions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Actions.openSearchIntent(this))
        finish()
    }
}