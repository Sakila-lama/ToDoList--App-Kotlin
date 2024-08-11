/**
 * File Name: Todo.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: July, 21st, 2024
 * App Description: Data class representing a Todo item
 * Version: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import java.io.Serializable

class Todo (
    var name: String = "",
    var notes: String = "",
    var dueDate: String = "",
    var isCompleted: Boolean = false,
    var hasDueDate: Boolean = false
) : Serializable