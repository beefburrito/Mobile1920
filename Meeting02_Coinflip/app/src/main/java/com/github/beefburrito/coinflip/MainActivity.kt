package com.github.beefburrito.coinflip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var resultText: TextView
    lateinit var flip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultText = findViewById(R.id.result_text) as TextView
        flip = findViewById(R.id.flip) as Button
        flip.setOnClickListener {
            flipCoin()
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }


    private fun flipCoin() {
        val rnds = (1..2).random()
        if (rnds == 1) {
            resultText.text = "Heads"
        } else {
            resultText.text = "Tails"
        }
    }
}
