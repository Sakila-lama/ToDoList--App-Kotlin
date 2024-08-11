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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment4_comp3025.databinding.ActivityTodoDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

// Activity for displaying and editing details of a Todo item
class TodoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoDetailsBinding
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val firestore = FirebaseFirestore.getInstance() // Initialize Firestore instance
    private var isDataChanged = false // To track if any data was changed


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

            // Toggle Calendar visibility based on Date Switch
            binding.switchHasDueDate.isChecked = it.hasDueDate
            binding.calendarViewDueDate.isEnabled = it.hasDueDate
            binding.switchHasDueDate.setOnCheckedChangeListener { _, isChecked ->
                binding.calendarViewDueDate.isEnabled = isChecked
                isDataChanged = true
            }
        }

            // Track changes in the fields
            binding.editTextTodoName.addTextChangedListener { isDataChanged = true }
            binding.editTextNotes.addTextChangedListener { isDataChanged = true }
            binding.switchCompletedDetail.setOnCheckedChangeListener { _, _ -> isDataChanged = true }



            // Set click listener for the update button
            binding.buttonUpdate.setOnClickListener {
                showConfirmationDialog(
                    title = "Update Todo",
                    message = "Are you sure you want to update this Todo?",
                    positiveAction = {
                        todo?.let {
                            it.name = binding.editTextTodoName.text.toString()
                            it.notes = binding.editTextNotes.text.toString()
                            it.isCompleted = binding.switchCompletedDetail.isChecked
                            it.hasDueDate =
                                binding.calendarViewDueDate.visibility == android.view.View.VISIBLE
                            it.dueDate =
                                if (it.hasDueDate) dateFormat.format(binding.calendarViewDueDate.date) else ""

                            if (it.name.isNotEmpty()) {
                                updateTodoInFirestore(it) // Save the changes to Firestore
                            } else {
                                Toast.makeText(
                                    this,
                                    "Todo name cannot be empty",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                )
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
        // Method to update the Todo in Firestore
        private fun updateTodoInFirestore(todo: Todo) {
            firestore.collection("todos")
                .whereEqualTo("name", todo.name) // Find the document based on the name
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        firestore.collection("todos")
                            .document(document.id)
                            .set(todo)
                            .addOnSuccessListener {
                                Log.d(
                                    "TodoDetailsActivity",
                                    "Todo successfully updated in Firestore"
                                )
                                setResult(RESULT_OK)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e("TodoDetailsActivity", "Error updating Todo in Firestore", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("TodoDetailsActivity", "Error updating Todo in Firestore", e)
                }
        }

        // Method to show a confirmation dialog
        private fun showConfirmationDialog(
            title: String,
            message: String,
            positiveAction: () -> Unit
        ) {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes") { _, _ -> positiveAction() }
                .setNegativeButton("No", null)
                .show()
        }
    }
}