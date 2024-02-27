package com.example.flagquest

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.flagquest.databinding.ActivityNextBinding

class NextActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the score from the intent
        val score = intent.getIntExtra("SCORE", 0)


        binding.playAgainButton.setBackgroundColor( ContextCompat.getColor(
            this@NextActivity,
            R.color.grey
        ))
        binding.quitButton.setBackgroundColor( ContextCompat.getColor(
            this@NextActivity,
            R.color.grey
        ))

        // Display the score
        binding.scoreTextView.text = "Score:"
        binding.score.text = "$score"

        // Set up click listeners for buttons
        binding.playAgainButton.setOnClickListener {
            // Navigate back to MainActivity to play again
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close this activity
        }

        binding.quitButton.setOnClickListener {
            // Close the app
            finishAffinity()
        }
    }
}