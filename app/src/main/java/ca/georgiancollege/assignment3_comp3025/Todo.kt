package ca.georgiancollege.assignment3_comp3025

import java.io.Serializable

class Todo (
    var name: String,
    var dueDate: String,
    var isCompleted: Boolean
) : Serializable