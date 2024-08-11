/**
 * File name: MainActivity.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: July, 21st, 2024
 * App description: MainActivity class displaying a list of Todo items.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment4_comp3025.databinding.ActivityMainBinding

// MainActivity class displaying a list of Todo items
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // Binding object for the activity's layout
    private lateinit var todoAdapter: TodoAdapter // Adapter for the RecyclerView
    private val todoList = mutableListOf<Todo>() // List to hold Todo items

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the adapter with the todoList and a click listener for the edit button
        todoAdapter = TodoAdapter(todoList) { todo ->
            val intent = Intent(this, TodoDetailsActivity::class.java)
            intent.putExtra("TODO", todo) // Pass the selected Todo item to TodoDetailsActivity
            startActivity(intent)
        }

        // Set up the RecyclerView with the adapter and a layout manager
        binding.recyclerViewTodos.adapter = todoAdapter
        binding.recyclerViewTodos.layoutManager = LinearLayoutManager(this)

        loadTodos()
    }

    // Function to load initial Todo items into the list
    private fun loadTodos() {
        todoList.add(Todo("Sample Task 1", "Notes for Task 1", "2024-07-11", false))
        todoList.add(Todo("Sample Task 2", "Notes for Task 2", "", false))
        todoAdapter.notifyDataSetChanged()
    }

    // Function to add a new Todo item to the list
    private fun addNewTodo() {
        val newTodo = Todo(
            name = "New Task",
            notes = "Notes for new task",
            dueDate = "", // or some default date
            isCompleted = false
        )
        todoList.add(newTodo)
        todoAdapter.notifyItemInserted(todoList.size - 1)
    }
}