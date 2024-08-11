/**
 * File name: TodoDetailsActivity.kt
 * Author: Sakila Lama
 * StudentID: 200548805
 * Date:August, 11th, 2024
 * App description: This file contains the TodoDetailsActivity class which is responsible for displaying
 *  * and editing details of a specific Todo item. It allows users to update the name, notes, completion status,
 *  * and due date of a Todo item and persist these changes to Firebase Firestore.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment4_comp3025.databinding.ActivityTodoDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * TodoDetailsActivity class is responsible for displaying the details of a specific Todo item,
 * allowing the user to edit its properties and save these changes to Firebase Firestore.
 */
class TodoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoDetailsBinding
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val firestore = FirebaseFirestore.getInstance() // Initialize Firestore instance
    private var isDataChanged = false // To track if any data was changed

    /**
     * onCreate method is called when the activity is created. It initializes the UI components,
     * populates them with the data from the selected Todo item, and sets up listeners for user interactions.
     * @param savedInstanceState The saved instance state bundle.
     */
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

            // Initially disable the CalendarView
            binding.calendarViewDueDate.isEnabled = false

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

            // Set the initial state of the switch
            binding.switchDueDate.isChecked = it.hasDueDate
            binding.calendarViewDueDate.isEnabled = it.hasDueDate

            // Toggle Calendar visibility based on Date Switch
            binding.switchDueDate.setOnCheckedChangeListener { _, isChecked ->
                binding.calendarViewDueDate.isEnabled = isChecked
                isDataChanged = true

            }
        }

            // Track changes in the fields
        binding.editTextTodoName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isDataChanged = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editTextNotes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isDataChanged = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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
                            it.hasDueDate = binding.switchDueDate.isChecked
                            it.dueDate = if (it.hasDueDate) dateFormat.format(binding.calendarViewDueDate.date) else ""

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
                showConfirmationDialog(
                    title = "Delete Todo",
                    message = "Are you sure you want to delete this Todo?",
                    positiveAction = {
                        todo?.let { deleteTodoFromFirestore(it) }
                    }
                )
            }

            // Set click listener for the cancel button
            binding.buttonCancel.setOnClickListener {
                if (isDataChanged) {
                    showConfirmationDialog(
                        title = "Discard Changes",
                        message = "Are you sure you want to discard your changes?",
                        positiveAction = { finish() }
                    )
                } else {
                    finish()
                }
            }
        }
    /**
     * updateTodoInFirestore method updates the given Todo item in Firebase Firestore.
     * @param todo The Todo item to be updated.
     */
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
                            Log.d("TodoDetailsActivity", "Todo successfully updated in Firestore")
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

    /**
     * deleteTodoFromFirestore method deletes the given Todo item from Firebase Firestore.
     * @param todo The Todo item to be deleted.
     */
    private fun deleteTodoFromFirestore(todo: Todo) {
        firestore.collection("todos")
            .whereEqualTo("name", todo.name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestore.collection("todos")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("TodoDetailsActivity", "Todo successfully deleted from Firestore")
                            setResult(RESULT_OK)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("TodoDetailsActivity", "Error deleting Todo from Firestore", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TodoDetailsActivity", "Error deleting Todo in Firestore", e)
            }
    }

    /**
     * showConfirmationDialog method displays a confirmation dialog with the given title, message,
     * and actions for positive and negative buttons.
     * @param title The title of the dialog.
     * @param message The message of the dialog.
     * @param positiveAction The action to perform when the positive button is clicked.
     */
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
