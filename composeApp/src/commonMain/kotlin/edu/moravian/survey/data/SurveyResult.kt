package edu.moravian.survey.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single instance of a completed survey.
 */
@Entity
data class SurveyResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val score: Int
)
