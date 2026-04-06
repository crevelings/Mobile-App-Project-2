package edu.moravian.survey

import org.jetbrains.compose.resources.StringResource
import surveytaker.composeapp.generated.resources.*
import kotlin.collections.map
import kotlin.jvm.JvmName

/**
 * A survey is a list of [SurveyElement]s. The order of the elements in the list determines the
 * order in which they are displayed to the user.
 */
typealias Survey = List<SurveyElement>

/**
 * A list of [Question]s. This is a convenience typealias for filtering a [Survey] to only include
 * the questions.
 */
typealias SurveyQuestions = List<Question<*>>

/**
 * Convenience property to get a list of all [Question]s in a [Survey] by filtering the list of
 * [SurveyElement]s to only include those that are instances of [Question].
 */
val Survey.questions get() = filterIsInstance<Question<*>>()

/**
 * A survey consists of a list of [SurveyElement]s, which can be either [Instruction]s or
 * [Question]s. At a minimum, an element has an [id] and [text].
 */
sealed interface SurveyElement {
    /**
     * The unique identifier for this element. This is used to identify the element when saving and
     * loading survey results.
     */
    val id: String

    /**
     * The text to display for this element. For an [Instruction], this is the instruction text. For
     * a [Question], this is the question text.
     */
    val text: String

    /**
     * If there is a problem with the user's answer to this element, this property gives a
     * string resource describing the problem. If there is no problem, this property is null.
     */
    val errorMessage: StringResource? get() = null

    /**
     * Compute a "score" for this element based on the user's answer. This is used to calculate the
     * total score for the survey. By default, this returns 0, but it can be overridden by specific
     * question types.
     */
    val score: Int get() = 0
}

/**
 * An instruction is a non-interactive element that provides information or guidance to the user.
 */
data class Instruction(
    override val id: String,
    override val text: String,
) : SurveyElement

/**
 * A question is an interactive element that allows the user to provide an answer. The type of the
 * answer is determined by the generic type parameter [T].
 */
sealed interface Question<T> : SurveyElement {
    val answer: T?
}

/**
 * A question with a single option allows the user to select one option from a list of options. The
 * answer is represented as the index of the selected option in the [options] list.
 */
data class QuestionWithSingleOption(
    override val id: String,
    override val text: String,
    val options: List<String>,
    val scoring: Map<Int, Int> = emptyMap(), // maps option index to score
    override val answer: Int? = null,
) : Question<Int> {
    override val errorMessage get() =
        when (answer) {
            null -> Res.string.no_option_selected
            !in options.indices -> Res.string.invalid_option_selected
            else -> null
        }
    override val score get() = answer?.let { scoring[it] } ?: 0
}

/**
 * A question with multiple options allows the user to select multiple options from a list of
 * options. The answer is represented as a set of indices of the selected options in the [options]
 * list.
 */
data class QuestionWithMultiOptions(
    override val id: String,
    override val text: String,
    val options: List<String>,
    override val answer: Set<Int>? = null,
) : Question<Set<Int>> {
    // NOTE: We allow the user to select no options, so we only check for invalid indices, not for null/empty
    override val errorMessage get() = if (answer?.any { it !in options.indices } == true) {
        Res.string.invalid_option_selected
    } else {
        null
    }
}

/**
 * A question with multiple options and an "Other" option allows the user to select multiple options
 * from a list of options, as well as provide a custom answer if the "Other" option is selected.
 * The answer is represented as a pair of a set of indices of the selected options in the [options]
 * list and an optional string for the custom answer if the "Other" option is selected.
 */
data class QuestionWithMultiOptionsAndOther(
    override val id: String,
    override val text: String,
    val options: List<String>, // should not include "Other"
    override val answer: Pair<Set<Int>, String>? = null,
) : Question<Pair<Set<Int>, String>> {
    // NOTE: We allow the user to select no options, so we only check for invalid indices, not for null/empty
    override val errorMessage get() = if (answer?.first?.any { it !in options.indices } == true) {
        Res.string.invalid_option_selected
    } else {
        null
    }
}

/**
 * Convenience function to get a [SurveyElement] from a [Survey] by its [id]. Returns null if no
 * element with the given [id] is found.
 */
operator fun Survey.get(id: String): SurveyElement? = firstOrNull { it.id == id }

/**
 * Convenience function to update a [Question] in a [Survey] by its `id`. Returns a new [Survey]
 * with the updated question if a question with the given `id` is found, or the original [Survey]
 * if no question with the given `id` is found.
 */
fun Survey.update(updated: Question<*>): Survey = map {
    if (it.id == updated.id) {
        updated
    } else {
        it
    }
}

/**
 * Convenience property to calculate the total score for a set of [SurveyQuestions] by summing the
 * scores of all [Question]s in the survey.
 */
val SurveyQuestions.score
    @JvmName("getSurveyQuestionsScore")
    get() = sumOf { it.score }

/**
 * Convenience property to check if there are any errors in a set of [SurveyQuestions] by checking
 * if any [SurveyElement] in the survey has a non-null `errorMessage`.
 */
val SurveyQuestions.hasErrors
    @JvmName("getSurveyQuestionsErrors")
    get() = any { it.errorMessage != null }
