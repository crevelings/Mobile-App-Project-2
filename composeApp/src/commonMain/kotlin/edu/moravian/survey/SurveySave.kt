package edu.moravian.survey

import edu.moravian.survey.data.SurveyAnswer
import edu.moravian.survey.data.SurveyDao
import edu.moravian.survey.data.SurveyResult

/**
 * Saves the current survey result to the repository. This should be called when the user completes
 * the survey.
 */
suspend fun SurveyQuestions.save(dao: SurveyDao, timestamp: Long) {
    val result = SurveyResult(
        timestamp = timestamp,
        score = this.score
    )

    val answers = this.map { question ->
        when (question) {
            is QuestionWithSingleOption -> SurveyAnswer(
                surveyId = 0, // Will be set by DAO transaction
                questionId = question.id,
                singleOption = question.answer
            )
            is QuestionWithMultiOptions -> SurveyAnswer(
                surveyId = 0,
                questionId = question.id,
                multiOptions = question.answer
            )
            is QuestionWithMultiOptionsAndOther -> SurveyAnswer(
                surveyId = 0,
                questionId = question.id,
                multiOptions = question.answer?.first,
                otherText = question.answer?.second
            )
        }
    }

    dao.saveSurvey(result, answers)
}

/**
 * Loads the survey result with the given ID from the repository and maps it back to a Survey.
 */
suspend fun Survey.load(surveyId: Long, dao: SurveyDao): Survey {
    val answers = dao.getAnswersForSurvey(surveyId).associateBy { it.questionId }
    
    return this.map { element ->
        if (element !is Question<*>) return@map element
        
        val savedAnswer = answers[element.id] ?: return@map element
        
        when (element) {
            is QuestionWithSingleOption -> element.copy(answer = savedAnswer.singleOption)
            is QuestionWithMultiOptions -> element.copy(answer = savedAnswer.multiOptions)
            is QuestionWithMultiOptionsAndOther -> {
                val multi = savedAnswer.multiOptions ?: emptySet()
                val other = savedAnswer.otherText ?: ""
                element.copy(answer = multi to other)
            }
        }
    }
}
