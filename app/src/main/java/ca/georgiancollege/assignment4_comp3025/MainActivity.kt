/**
 * File name: MainActivity.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: August 11th, 2024
 * App description:This file contains the MainActivity class which manages the main screen of the Todo app.
 *  * It displays a list of Todo items and allows the user to add new Todos, edit existing ones, and persist data to Firebase.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment4_comp3025.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * MainActivity class is responsible for displaying the list of Todo items and handling user interactions
 * such as adding new Todos, editing existing ones, and synchronizing data with Firebase Firestore.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // Binding object for the activity's layout
    private lateinit var todoAdapter: TodoAdapter // Adapter for the RecyclerView
    private val todoList = mutableListOf<Todo>() // List to hold Todo items
    private val firestore = FirebaseFirestore.getInstance()


    // Declare the ActivityResultLauncher
    private lateinit var todoDetailsLauncher: ActivityResultLauncher<Intent>

    /**
     * onCreate method is called when the activity is created. It initializes the UI components,
     * sets up the RecyclerView, and registers the ActivityResultLauncher for handling results
     * from TodoDetailsActivity.
     * @param savedInstanceState The saved instance state bundle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ActivityResultLauncher
        todoDetailsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Reload todos when returning from TodoDetailsActivity
                loadTodos()
            }
        }

        // Initialize the adapter with the todoList and a click listener for the edit button
        todoAdapter = TodoAdapter(todoList) { todo ->
            val intent = Intent(this, TodoDetailsActivity::class.java)
            intent.putExtra("TODO", todo) // Pass the selected Todo item to TodoDetailsActivity
            todoDetailsLauncher.launch(intent)  // Launch the activity for result
        }

        // Set up the RecyclerView with the adapter and a layout manager
        binding.recyclerViewTodos.adapter = todoAdapter
        binding.recyclerViewTodos.layoutManager = LinearLayoutManager(this)

        // new todo when FAB is clicked
        binding.fabAddTodo.setOnClickListener {
            addNewTodo()
        }

        loadTodos()
    }

    /**
     * loadTodos method loads the list of Todos from Firestore and updates the RecyclerView.
     */
    private fun loadTodos() {
        firestore.collection("todos")
            .get()
            .addOnSuccessListener { documents ->
                todoList.clear()
                for (document in documents) {
                    val todo = document.toObject(Todo::class.java)
                    todoList.add(todo)
                }
                todoAdapter.notifyDataSetChanged()
            }
    }

    /**
     * addNewTodo method displays a dialog for the user to enter a new Todo item, adds it to Firestore,
     * and updates the list.
     */
    private fun addNewTodo() {
        // Create an AlertDialog to capture the Todo name
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Todo")

        // Set up the input
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, _ ->
            val todoName = input.text.toString()
            if (todoName.isNotEmpty()) {
                val newTodo = Todo(
                    name = todoName,
                    notes = "",
                    dueDate = "",
                    isCompleted = false,
                    hasDueDate = false
                )
                // Add the new Todo to Firestore and update the RecyclerView
                firestore.collection("todos")
                    .add(newTodo)
                    .addOnSuccessListener {
                        todoList.add(newTodo)
                        todoAdapter.notifyItemInserted(todoList.size - 1)
                    }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

}

