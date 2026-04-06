package edu.moravian.survey

/**
 * Saves the current survey result to the repository. This should be called when the user completes
 * the survey.
 */
suspend fun SurveyQuestions.save() {
    // TODO: complete (may need to add parameter(s))
}

/**
 * Loads the survey result with the given ID from the repository and maps it back to a Survey.
 */
suspend fun Survey.load(surveyId: Long): Survey {
    // TODO: complete (may need to add parameter(s))
    return this
}
