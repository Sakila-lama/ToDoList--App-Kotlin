/**
 * File name: MainActivity.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: July, 21st, 2024
 * App description: Adapter for displaying a list of Todo items in a RecyclerView.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment4_comp3025.databinding.ItemTodoBinding


// Adapter for displaying a list of Todo items in a RecyclerView
class TodoAdapter
    (
    private val todoList: List<Todo>,
    private val onEditClick: (Todo) -> Unit  // Callback for handling edit button clicks
    ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>()

{
    // ViewHolder class for holding and binding views for each Todo item
    inner class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)
    {
        // Bind the data to the views
        fun bind(todo: Todo) {
            binding.textViewTaskName.text = todo.name // Set task name
            binding.textViewDueDate.text = todo.dueDate // Set due date
            binding.switchCompleted.isChecked = todo.isCompleted // Set completion status

            // Show or hide due date based on its presence
                binding.textViewDueDate.visibility = if (todo.dueDate.isNotEmpty()) android.view.View.VISIBLE else android.view.View.GONE

            // Conditionally show the Switch and Edit Button
            binding.switchCompleted.visibility = if (todo.name.isNotEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            binding.buttonEdit.visibility = if (todo.name.isNotEmpty()) android.view.View.VISIBLE else android.view.View.GONE


            // Set click listener for the edit button
            binding.buttonEdit.setOnClickListener {
                val intent = Intent(binding.root.context, TodoDetailsActivity::class.java)
                intent.putExtra("TODO", todo) // Pass the selected Todo item to TodoDetailsActivity
                binding.root.context.startActivity(intent) // Start TodoDetailsActivity
            }

            // Set change listener for the completion switch
            binding.switchCompleted.setOnCheckedChangeListener { _, isChecked ->
                todo.isCompleted = isChecked
                binding.textViewTaskName.alpha = if (isChecked) 0.5f else 1.0f

            }
        }
    }

    // Create and return a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    // Bind the ViewHolder with the data at the specified position
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    // Return the total number of items in the list
    override fun getItemCount() = todoList.size
}
