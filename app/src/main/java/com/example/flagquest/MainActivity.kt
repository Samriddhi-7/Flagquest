package com.example.flagquest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flagquest.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var correctAnswers = 0
    private var lives=3


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        GlobalScope.launch(Dispatchers.Main) {
            loadQuestion()
        }


    }

    suspend fun loadQuestion() {
        binding.correctAnswersTextView.text = "$correctAnswers"
        if (lives > 0) {
            try {
                val response = RetrofitClient.apiService.getAllCountries()

                if (response.isSuccessful) {
                    val countryList = response.body()
                    println("Country List: $countryList")

                    val randomCountry = countryList?.random()

                    // Generate options for the new question
                    val options = mutableListOf<String>()
                    options.add(randomCountry?.name?.common ?: "")
                    for (i in 1..3) {
                        val randomCountry = countryList?.random()
                        options.add(randomCountry?.name?.common ?: "")
                    }
                    options.shuffle()

                    runOnUiThread {
                        // Set text for buttons
                        binding.option1Button.text = options[0]
                        binding.option2Button.text = options[1]
                        binding.option3Button.text = options[2]
                        binding.option4Button.text = options[3]

                        // Reset button colors
                        binding.option1Button.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.grey
                            )
                        )
                        binding.option2Button.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.grey
                            )
                        )
                        binding.option3Button.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.grey
                            )
                        )
                        binding.option4Button.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.grey
                            )
                        )

                        // Load the random flag image
                        Picasso.get().load(randomCountry?.flags?.png).into(binding.flagImageView)
                        val optionButtons = listOf(
                            binding.option1Button,
                            binding.option2Button,
                            binding.option3Button,
                            binding.option4Button
                        )

                        optionButtons.forEachIndexed { index, button ->
                            button.setOnClickListener {
                                GlobalScope.launch(Dispatchers.Main) {
                                    if (options[index] == randomCountry?.name?.common) {
                                        // Correct Answer
                                        correctAnswers++

                                        runOnUiThread {
                                            button.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    this@MainActivity,
                                                    R.color.correctAnswer
                                                )
                                            )
                                        }
                                    } else {
                                        // Incorrect Answer
//                                        correctAnswers=0
                                        lives = lives - 1

                                        runOnUiThread {
                                            button.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    this@MainActivity,
                                                    R.color.incorrectAnswer
                                                )
                                            )
                                        }

                                        // Find and highlight the correct option
                                        val correctButton = optionButtons.firstOrNull {
                                            options.indexOf(it.text.toString()) == options.indexOf(
                                                randomCountry?.name?.common
                                            )
                                        }
                                        runOnUiThread {
                                            correctButton?.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    this@MainActivity,
                                                    R.color.correctAnswer
                                                )
                                            )
                                        }
                                        if (lives == 2) {
                                            binding.heart2.visibility = View.GONE
                                        } else if (lives == 1) {
                                            binding.heart1.visibility = View.GONE
                                        } else if (lives == 0) {
                                            binding.heart3.visibility = View.GONE
                                            // Game over
                                        }
                                    }

                                    // Load the next question
                                    loadQuestion()
                                }
                            }

                        }
                    }
                } else
                    Toast.makeText(this, "Network Issue", Toast.LENGTH_SHORT).show()
                    println("Response is not successful")
            } catch (e: Exception) {
                println("Error: ${e.message}")
                Toast.makeText(this, "Network Issue", Toast.LENGTH_SHORT).show()

            }
        } else {
// Assuming you're in the block where lives reach 0
            val score = correctAnswers

// Create an Intent to start the next activity
            val intent = Intent(this, NextActivity::class.java).apply {
                putExtra("SCORE", score)
            }

// Start the next activity
            startActivity(intent)
        }


    }
    }

