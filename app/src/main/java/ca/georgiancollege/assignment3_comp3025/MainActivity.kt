package ca.georgiancollege.assignment3_comp3025

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment3_comp3025.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter = TodoAdapter(todoList) { todo ->
            val intent = Intent(this, TodoDetailsActivity::class.java)
            intent.putExtra("TODO", todo)
            startActivity(intent)
        }

        binding.recyclerViewTodos.adapter = todoAdapter
        binding.recyclerViewTodos.layoutManager = LinearLayoutManager(this)

        loadTodos()
    }

    private fun loadTodos() {
        todoList.add(Todo("Sample Task 1", "Notes for Task 1", "2024-07-11", false))
        todoList.add(Todo("Sample Task 2", "Notes for Task 2", "", false))
        todoAdapter.notifyDataSetChanged()
    }

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