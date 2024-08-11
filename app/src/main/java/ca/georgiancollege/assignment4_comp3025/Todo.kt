/**
 * File Name: Todo.kt
 * Author: Sakila lama
 * StudentID: 200548805
 * Date: August 11th,2024
 * App Description:This file defines the `Todo` data class, which represents a Todo item
 *  * with attributes for the task name, notes, due date, completion status, and whether a due date is set.
 *  * The class implements `Serializable` to allow for easy passing of `Todo` objects between activities.
 * Version: 1.0
 */


package ca.georgiancollege.assignment4_comp3025

import java.io.Serializable

/**
 * The `Todo` class represents a Todo item in the application.
 * @property name The name or title of the Todo item.
 * @property notes Any additional notes related to the Todo item.
 * @property dueDate The due date for the Todo item as a String.
 * @property isCompleted A Boolean indicating whether the Todo item is completed.
 * @property hasDueDate A Boolean indicating whether the Todo item has a due date set.
 */
class Todo (
    var name: String = "",
    var notes: String = "",
    var dueDate: String = "",
    var isCompleted: Boolean = false,
    var hasDueDate: Boolean = false
) : Serializable