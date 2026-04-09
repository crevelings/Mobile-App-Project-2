package edu.moravian.survey.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an answer to a single question within a survey.
 */
@Entity(
    tableName = "answers",
    foreignKeys = [
        ForeignKey(
            entity = SurveyResult::class,
            parentColumns = ["id"],
            childColumns = ["surveyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("surveyId")]
)
data class SurveyAnswer(
    @PrimaryKey(autoGenerate = true) val answerId: Long = 0,
    val surveyId: Long,
    val questionId: String,
    val singleOption: Int? = null,
    val multiOptions: Set<Int>? = null,
    val otherText: String? = null
)
