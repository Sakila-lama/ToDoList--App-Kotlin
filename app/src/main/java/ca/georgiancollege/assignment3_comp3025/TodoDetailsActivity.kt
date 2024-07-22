package ca.georgiancollege.assignment3_comp3025

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment3_comp3025.databinding.ActivityTodoDetailsBinding

class TodoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val todo = intent.getSerializableExtra("TODO") as? Todo

        todo?.let {
            binding.editTextTodoName.setText(it.name)
            binding.editTextNotes.setText(it.notes)
            binding.switchCompletedDetail.isChecked = it.isCompleted

            if (it.dueDate.isNotEmpty()) {
                binding.calendarViewDueDate.date = it.dueDate.toLong()
            }
        }

        binding.buttonUpdate.setOnClickListener {
            // Update todo logic
        }

        binding.buttonDelete.setOnClickListener {
            // Delete todo logic
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
}