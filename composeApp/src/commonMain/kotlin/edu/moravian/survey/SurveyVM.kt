package edu.moravian.survey

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for managing the state of the survey being taken.
 *
 * This survives configuration changes (like screen rotation), so the user doesn't
 * lose their answers when the screen rotates.
 *
 * Like GameVM, this must have a parameter-less constructor and extend ViewModel().
 */
class SurveyVM : ViewModel() {
    init {
        val random = (0..1000).random()
        println("🔵 ViewModel CREATED - ID: $random")
    }

    private val _survey = MutableStateFlow<Survey?>(AMISOS_R_SURVEY)
    val survey: StateFlow<Survey?> = _survey

    private val _showErrors = MutableStateFlow(false)
    val showErrors: StateFlow<Boolean> = _showErrors

    fun initialize(mostRecentSurvey: Survey?) {
        println("🟢 initialize() called with mostRecentSurvey = ${mostRecentSurvey != null}")

        if (_survey.value != null && _survey.value != AMISOS_R_SURVEY) {
            println("✅ Survey already initialized, skipping")
            return
        }
        if (mostRecentSurvey != null) {
            val sounds = mostRecentSurvey.getOrNull(0)
            val emotions = mostRecentSurvey.getOrNull(1)

            _survey.value =
                AMISOS_R_SURVEY.mapIndexed { index, element ->
                    when (index) {
                        0 -> sounds ?: element
                        1 -> emotions ?: element
                        else -> element
                    }
                }
        } else {
            println("⚠️ initialize() resetting to AMISOS_R_SURVEY - THIS LOSES ANSWERS!")
            _survey.value = AMISOS_R_SURVEY
        }
        _showErrors.value = false
    }

    /**
     * Loads all answers from the given survey into the current survey.
     */
    fun loadFullSurvey(previousSurvey: Survey) {
        _survey.value = previousSurvey
        _showErrors.value = false
    }

    fun updateSurvey(updatedSurvey: Survey) {
        println("🟡 updateSurvey() called - first question answer: ${updatedSurvey.questions.firstOrNull()?.answer}")
        _survey.value = updatedSurvey
    }

    fun attemptSubmit(): Boolean {
        println("🔴 attemptSubmit() called")
        _showErrors.value = true

        val questions = _survey.value?.questions ?: return false
        val hasErrors = questions.hasErrors
        println("🔴 Has errors: $hasErrors")
        return !questions.hasErrors
    }

    val currentQuestions: SurveyQuestions
        get() = _survey.value?.questions ?: emptyList()
}
