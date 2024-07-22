package ca.georgiancollege.assignment3_comp3025

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment3_comp3025.databinding.ItemTodoBinding

class TodoAdapter
    (
    private val todoList: List<Todo>,
    private val onEditClick: (Todo) -> Unit
    ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>()

{
    inner class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.textViewTaskName.text = todo.name
            binding.textViewDueDate.text = todo.dueDate
            binding.switchCompleted.isChecked = todo.isCompleted

            if (todo.dueDate.isNotEmpty()) {
                binding.textViewDueDate.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewDueDate.visibility = android.view.View.GONE
            }

            binding.buttonEdit.setOnClickListener {
                onEditClick(todo)
            }

            binding.switchCompleted.setOnCheckedChangeListener { _, isChecked ->
                todo.isCompleted = isChecked
                binding.textViewTaskName.alpha = if (isChecked) 0.5f else 1.0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount() = todoList.size
}
