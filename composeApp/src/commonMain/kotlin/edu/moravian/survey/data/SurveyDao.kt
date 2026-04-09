package edu.moravian.survey.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Insert
    suspend fun insertSurveyResult(result: SurveyResult): Long

    @Insert
    suspend fun insertAnswers(answers: List<SurveyAnswer>)

    @Query("SELECT * FROM SurveyResult ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<SurveyResult>>

    @Query("SELECT * FROM SurveyResult ORDER BY timestamp DESC LIMIT 1")
    fun getLatestResult(): Flow<SurveyResult?>

    @Query("SELECT * FROM SurveyResult WHERE id = :id")
    suspend fun getResultById(id: Long): SurveyResult?

    @Query("SELECT * FROM answers WHERE surveyId = :surveyId")
    suspend fun getAnswersForSurvey(surveyId: Long): List<SurveyAnswer>

    @Transaction
    suspend fun saveSurvey(result: SurveyResult, answers: List<SurveyAnswer>) {
        val id = insertSurveyResult(result)
        val answersWithId = answers.map { it.copy(surveyId = id) }
        insertAnswers(answersWithId)
    }
}
