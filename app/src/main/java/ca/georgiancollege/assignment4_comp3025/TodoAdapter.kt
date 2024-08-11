/**
 * File name: MainActivity.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: August,11th,2024
 * App description: s file contains the MainActivity class which is the entry point of the app.
 *  * It manages the display of a list of Todo items, allowing users to view, edit, and update their status.
 * Version information: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment4_comp3025.databinding.ItemTodoBinding
import com.google.firebase.firestore.FirebaseFirestore

/**
 * TodoAdapter class is responsible for adapting the list of Todo items into views for the RecyclerView.
 * It binds the Todo data to the UI components and handles user interactions such as editing tasks
 * and toggling their completion status.
 */

// Adapter for displaying a list of Todo items in a RecyclerView
class TodoAdapter
    (
    private val todoList: List<Todo>,
    private val onEditClick: (Todo) -> Unit  // Callback for handling edit button clicks
    ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>()

{
    /**
     * ViewHolder class for holding and binding views for each Todo item in the RecyclerView.
     * @param binding The data binding for the item view layout.
     */
    inner class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)
    {
        /**
         * Binds the data from the Todo object to the UI components in the view.
         * @param todo The Todo item containing the task name, due date, and completion status.
         */
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

                // Update the status in Firestore by finding the correct document
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("todos")
                    .whereEqualTo("name", todo.name) // Find the document based on the name
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            firestore.collection("todos")
                                .document(document.id)
                                .set(todo) // Update the document with the new data
                                .addOnSuccessListener {
                                    Log.d("TodoAdapter", "Todo completion status updated in Firestore")
                                }
                                .addOnFailureListener { e ->
                                    Log.e("TodoAdapter", "Error updating completion status in Firestore", e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("TodoAdapter", "Error finding document in Firestore", e)
                    }

            }
        }
    }

    /**
     * Creates and returns a new ViewHolder instance.
     * @param parent The parent view group into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder instance containing the inflated view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    /**
     * Binds the ViewHolder with the data at the specified position.
     * @param holder The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    /**
     * Returns the total number of items in the list.
     * @return The number of items in the list.
     */
    override fun getItemCount() = todoList.size
}
