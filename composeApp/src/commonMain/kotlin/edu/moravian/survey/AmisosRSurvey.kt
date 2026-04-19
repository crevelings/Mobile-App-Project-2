package edu.moravian.survey

import org.jetbrains.compose.resources.StringResource
import surveytaker.composeapp.generated.resources.Res
import surveytaker.composeapp.generated.resources.overdue
import surveytaker.composeapp.generated.resources.reminder_start
import surveytaker.composeapp.generated.resources.reminder_target

private val DEFAULT_OPTIONS = listOf("not", "mild", "moder\u00ADate", "severe", "extreme")

private val DEFAULT_SCORING = mapOf(0 to 0, 1 to 1, 2 to 2, 3 to 3, 4 to 4)

/**
 * This object defines the survey elements for the AmisosR survey, which is a survey designed to
 * assess the severity of misophonia symptoms. The ultimate goal is for the app to support many
 * different surveys.
 *
 * From: https://www.frontiersin.org/journals/psychiatry/articles/10.3389/fpsyt.2023.1112472/full
 */
val AMISOS_R_SURVEY: List<SurveyElement> =
    listOf(
        QuestionWithMultiOptions(
            "sounds",
            "In comparison to others, I am sensitive to:",
            listOf(
                "Eating sounds",
                "Nasal sounds",
                "Throat sounds",
                "Specific sounds",
                "Crinkling sounds",
                "Ambient noises",
                "Repeating clicking sounds",
            ),
        ),
        QuestionWithMultiOptionsAndOther(
            "emotions",
            "Which emotions are evoked by listening to those sounds?",
            listOf(
                "Irritation",
                "Anger",
                "Disgust",
            ),
        ),
        Instruction(
            "instructions",
            "Review your experience from hearing your misophonia sounds in the last 3 days. " +
                "Read instead of “sounds” your most disturbing misophonia sounds and instead of " +
                "“emotion” your typical emotion. Choose the answer that is most applicable for you.",
        ),
        QuestionWithSingleOption(
            "1",
            "How much time do you spend a day (thinking about) these sounds?",
            listOf(
                "0\nhour",
                "<1\nhour",
                "1-3\nhours",
                "3-8\nhours",
                ">8\nhours",
            ),
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "2",
            "To what extent do you focus on these sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "3",
            "To what extent do you experience impairment due to these sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "4",
            "How intense is your feeling of irritability/anger when you hear these sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "5",
            "To what extent do you feel helpless against these sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "6",
            "To what extent are you suffering from these sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "7",
            "To what extent are you suffering from the avoidance of these sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "8",
            "To what extent are the sounds limiting your life (work, household etc.)?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "9",
            "To what extent are you avoiding specific places or situations because of the sounds?",
            DEFAULT_OPTIONS,
            DEFAULT_SCORING,
        ),
        QuestionWithSingleOption(
            "10",
            "To what extent can you shift your attention when you are hearing these sounds?",
            listOf(
                "always",
                "usually\n(75%)",
                "some\u00ADtimes\n(50%)",
                "seldom\n(25%)",
                "never",
            ),
            DEFAULT_SCORING,
        ),
    )

// NOTE: feel free to adjust these constants for testing purposes, but the final values must be as follows
const val THREE_DAYS = 3L * 24 * 60 * 60 * 1000
const val TWELVE_HOURS = 12L * 60 * 60 * 1000

const val REMINDER_START = THREE_DAYS - TWELVE_HOURS
const val REMINDER_TARGET = THREE_DAYS
const val OVERDUE = THREE_DAYS + TWELVE_HOURS

/**
 * Returns a reminder message as a string resource based on the elapsed time since the last survey
 * was taken, possibly returning null if no reminder should be shown.
 */
fun reminderMessage(
    nowMillis: Long,
    lastTakenMillis: Long,
): StringResource? {
    val elapsed = nowMillis - lastTakenMillis
    return when {
        elapsed >= OVERDUE -> Res.string.overdue
        elapsed >= REMINDER_TARGET -> Res.string.reminder_target
        elapsed >= REMINDER_START -> Res.string.reminder_start
        else -> null
    }
}
