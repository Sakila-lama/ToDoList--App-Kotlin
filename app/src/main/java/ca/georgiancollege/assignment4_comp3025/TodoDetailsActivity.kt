/**
 * File name: TodoDetailsActivity.kt
 * Author: Sakila Lama
 * StudentID: 200548805
 * Date: July, 21st, 2024
 * App description: Activity for displaying and editing details of a Todo item.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment4_comp3025.databinding.ActivityTodoDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

// Activity for displaying and editing details of a Todo item
class TodoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoDetailsBinding
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the Todo object passed via Intent
        val todo = intent.getSerializableExtra("TODO") as? Todo
        Log.d("TodoDetailsActivity", "Received Todo: $todo")


        // If the Todo object is not null, populate the UI fields with its data
        todo?.let {
            binding.editTextTodoName.setText(it.name) // Set the task name
            binding.editTextNotes.setText(it.notes) // Set the notes
            binding.switchCompletedDetail.isChecked = it.isCompleted // Set the completion status

            // If dueDate is not empty, set the CalendarView's date
            if (it.dueDate.isNotEmpty()) {
                try {
                    val date = dateFormat.parse(it.dueDate)
                    date?.let {
                        binding.calendarViewDueDate.date = date.time
                    }
                } catch (e: Exception) {
                    Log.e("TodoDetailsActivity", "Error parsing date: ${e.message}")
                }
            }
        }

        // Set click listener for the update button
        binding.buttonUpdate.setOnClickListener {
            // Update todo logic
        }

        // Set click listener for the delete button
        binding.buttonDelete.setOnClickListener {
            // Delete todo logic
        }

        // Set click listener for the cancel button
        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
}