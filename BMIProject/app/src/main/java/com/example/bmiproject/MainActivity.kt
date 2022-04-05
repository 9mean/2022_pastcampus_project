package com.example.bmiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bmiproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        Log.d("TAG", "oncreate")
        val weightEditText=mainBinding.weightEditText
        val heightEditText=mainBinding.heightEditText
        val resultOkBtn=mainBinding.resultOkBtn
        resultOkBtn.setOnClickListener {

            Log.d("TAG", "버튼이 클릭됨")
            if(weightEditText.text.isEmpty()||heightEditText.text.isEmpty()){
                Toast.makeText(this,"빈값이 있습니다.",Toast.LENGTH_SHORT).show()
            }
            else{
                val weight=weightEditText.text.toString().toInt()
                val height=heightEditText.text.toString().toInt()
                Log.d("TAG", "weight: $weight height: $height")
                val intent= Intent(this,ResultActivity::class.java)
                intent.putExtra("weight",weight)
                intent.putExtra("height",height)
                startActivity(intent)
            }

        }
    }
}