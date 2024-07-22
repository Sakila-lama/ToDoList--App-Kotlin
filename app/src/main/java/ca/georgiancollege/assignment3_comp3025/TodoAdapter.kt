/**
 * File name: MainActivity.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: July, 21st, 2024
 * App description: Adapter for displaying a list of Todo items in a RecyclerView.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment3_comp3025

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment3_comp3025.databinding.ItemTodoBinding

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
            if (todo.dueDate.isNotEmpty()) {
                binding.textViewDueDate.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewDueDate.visibility = android.view.View.GONE
            }

            // Set click listener for the edit button
            binding.buttonEdit.setOnClickListener {
                onEditClick(todo)
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
