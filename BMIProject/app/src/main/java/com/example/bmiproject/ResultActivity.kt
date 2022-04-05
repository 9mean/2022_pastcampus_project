package com.example.bmiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bmiproject.databinding.ActivityResultBinding
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding= ActivityResultBinding.inflate(layoutInflater)
        setContentView(resultBinding.root)
        val height=intent.getIntExtra("height",0)
        val weight=intent.getIntExtra("weight",0)
        val bmi=weight/ (height / 100.0).pow(2.0)
        val resultText=when{
            bmi>=35.0->"고도 비만"
            bmi>=30.0->"중정도 비만"
            bmi>=25.0->"경도 비만"
            bmi>=23.0->"과체중"
            bmi>=18.5->"정상체중"
            else->"저체중"
        }
        resultBinding.bmiTextView.text=bmi.toString()
        resultBinding.resultTextView.text=resultText
    }
}