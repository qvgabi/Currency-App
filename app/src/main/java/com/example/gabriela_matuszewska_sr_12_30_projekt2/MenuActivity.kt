// Gabriela Matuszewska
// Udało mi się zrobić wszystko

package com.example.gabriela_matuszewska_sr_12_30_projekt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuActivity : AppCompatActivity() {
    internal lateinit var currencyButton: Button
    internal lateinit var goldButton: Button
    internal lateinit var calcButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        goldButton = findViewById(R.id.goldCButton)
        currencyButton = findViewById(R.id.currencyListButton)
        calcButton = findViewById(R.id.converterButton)

        goldButton.setOnClickListener {
            val intent1 = Intent(this,GoldActivity::class.java)
            startActivity(intent1)
        }

        currencyButton.setOnClickListener {
            val intent = Intent(this,CurrencyActivity::class.java)
            startActivity(intent)
        }

        calcButton.setOnClickListener {
            val intent = Intent(this, CalculatorCurrencyActivity::class.java)
            startActivity(intent)
        }
    }
}

