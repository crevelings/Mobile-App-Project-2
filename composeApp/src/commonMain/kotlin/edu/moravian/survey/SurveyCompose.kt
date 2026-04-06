package edu.moravian.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.moravian.survey.update
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.Res
import surveytaker.composeapp.generated.resources.other

/**
 * Renders a list of [SurveyElement]s as a scrollable column. Each element is rendered using the
 * [SurveyElement.Render] composable, which handles the specific rendering logic for each type of
 * element. The [onAnswer] callback is invoked whenever an answer is provided for any question, with
 * the updated list of [SurveyElement]s reflecting the new answer.
 *
 * The default of null for [onAnswer] allows the survey to be rendered in read-only mode, where
 * answers cannot be changed.
 */
@Composable
fun Survey.Render(
    modifier: Modifier = Modifier,
    showError: Boolean = true,
    onAnswer: ((Survey) -> Unit)? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        items(this@Render, key = { it.id }) { element ->
            element.Render(
                showError = showError,
                onAnswer = onAnswer?.let { { updated -> onAnswer(update(updated as Question<*>)) } },
            )
            if (element != last()) {
                HorizontalDivider()
            }
        }
    }
}

/**
 * Renders a single [SurveyElement] based on its type. The [onAnswer] callback is invoked whenever
 * an answer is provided for the element, with the updated [SurveyElement] reflecting the new
 * answer. If [onAnswer] is null, the element is rendered in a read-only mode, where answers cannot
 * be changed.
 *
 * Optionally, an [error] message can be provided to display validation feedback for the element.
 * This can be used to indicate to the user that their answer is invalid or incomplete when they
 * attempt to submit the survey.
 */
@Composable
fun SurveyElement.Render(
    modifier: Modifier = Modifier,
    showError: Boolean = true,
    onAnswer: ((SurveyElement) -> Unit)? = null,
) {
    when (this) {
        is Instruction -> InstructionElement(this, modifier, showError)

        is QuestionWithSingleOption -> QuestionWithSingleOptionElement(
            this,
            modifier,
            showError,
            onAnswer?.let { { onAnswer(this.copy(answer = it)) } },
        )

        is QuestionWithMultiOptions -> QuestionWithMultiOptionsElement(
            this,
            modifier,
            showError,
            onAnswer?.let { { onAnswer(this.copy(answer = it)) } },
        )

        is QuestionWithMultiOptionsAndOther -> QuestionWithMultiOptionsAndOtherElement(
            this,
            modifier,
            showError,
            onAnswer?.let { { onAnswer(this.copy(answer = it)) } },
        )
    }
}

/**
 * Renders an [Instruction] element as bold text. This element is non-interactive and serves to
 * provide information or guidance to the user.
 */
@Composable
private fun InstructionElement(
    instruction: Instruction,
    modifier: Modifier = Modifier,
    showError: Boolean = true,
) {
    Column(modifier = modifier) {
        Text(instruction.text, fontWeight = FontWeight.Bold)
        if (showError) {
            val errorMessage = instruction.errorMessage?.let { stringResource(it) }
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Renders a question text as a simple [Text] composable. This is used as a common component for all
 * question types to display the question text above the interactive options. If [showError] is true
 * and there is an error message associated with the element, the error message is displayed below
 * the question text in red.
 */
@Composable
private fun QuestionText(
    element: SurveyElement,
    modifier: Modifier = Modifier,
    showError: Boolean = true,
) {
    Column(modifier = modifier) {
        Text(element.text)
        if (showError) {
            val errorMessage = element.errorMessage?.let { stringResource(it) }
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Renders a single checkable button. This is used as a common component for both the checkboxes and
 * radio button groups to display each option as a toggleable button.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CheckableButton(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onChange: () -> Unit,
) {
    ToggleButton(
        checked,
        { onChange() },
        shapes = ToggleButtonShapes(
            shape = MaterialTheme.shapes.small,
            pressedShape = MaterialTheme.shapes.small,
            checkedShape = MaterialTheme.shapes.small,
        ),
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp),
        modifier = modifier,
    ) {
        Text(
            text,
            textAlign = TextAlign.Center,
            style = LocalTextStyle.current.copy(hyphens = Hyphens.Auto),
        )
    }
}

/**
 * Renders a group of checkable buttons for a list of options.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CheckableButtons(
    options: List<String>,
    modifier: Modifier = Modifier,
    isChecked: (Int) -> Boolean,
    onChange: (Int) -> Unit,
) {
    FlowRow(
        maxItemsInEachRow = 5,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        options.forEachIndexed { index, option ->
            CheckableButton(
                text = option,
                checked = isChecked(index),
                modifier = Modifier.fillMaxRowHeight().weight(1f),
            ) { onChange(index) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Checkboxes(
    options: List<String>,
    answers: Set<Int>,
    modifier: Modifier = Modifier,
    onChange: ((Set<Int>) -> Unit)? = null,
) {
    CheckableButtons(
        options = options,
        modifier = modifier,
        isChecked = { answers.contains(it) },
        onChange = onChange?.let {
            { index -> onChange(if (answers.contains(index)) (answers - index) else (answers + index)) }
        } ?: {},
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RadioButtonGroup(
    options: List<String>,
    answer: Int?,
    modifier: Modifier = Modifier,
    onChange: ((Int) -> Unit)? = null,
) {
    CheckableButtons(
        options = options,
        modifier = modifier,
        isChecked = { it == answer },
        onChange = onChange ?: {},
    )
}

@Composable
private fun OtherBox(
    text: String,
    onChange: ((String) -> Unit)? = null,
) {
    TextField(
        value = text,
        onValueChange = onChange ?: {},
        readOnly = onChange == null,
        placeholder = { Text(stringResource(Res.string.other)) },
        label = { Text(stringResource(Res.string.other)) },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun QuestionWithSingleOptionElement(
    question: QuestionWithSingleOption,
    modifier: Modifier = Modifier,
    showError: Boolean = true,
    onAnswer: ((Int) -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        QuestionText(question, showError = showError)
        RadioButtonGroup(
            options = question.options,
            answer = question.answer,
            modifier = Modifier.fillMaxWidth(),
            onChange = onAnswer,
        )
    }
}

@Preview
@Composable
private fun QuestionWithSingleOptionElementPreview() {
    QuestionWithSingleOptionElement(
        question = QuestionWithSingleOption(
            id = "q1",
            text = "What is your favorite color?",
            options = listOf("Red", "Green", "Blue"),
            answer = 2,
        ),
    ) { }
}

@Composable
private fun QuestionWithMultiOptionsElement(
    question: QuestionWithMultiOptions,
    modifier: Modifier = Modifier,
    showError: Boolean = true,
    onAnswer: ((Set<Int>) -> Unit)? = null,
) {
    val answers = question.answer ?: emptySet()
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        QuestionText(question, showError = showError)
        Checkboxes(
            options = question.options,
            answers = answers,
            modifier = Modifier.fillMaxWidth(),
            onChange = onAnswer,
        )
    }
}

@Preview
@Composable
private fun QuestionWithMultiOptionsElementPreview() {
    QuestionWithMultiOptionsElement(
        question = QuestionWithMultiOptions(
            id = "q1",
            text = "Which colors do you like?",
            options = listOf("Red", "Green", "Blue"),
            answer = setOf(0, 2),
        ),
    ) { }
}

@Composable
private fun QuestionWithMultiOptionsAndOtherElement(
    question: QuestionWithMultiOptionsAndOther,
    modifier: Modifier = Modifier,
    showError: Boolean = true,
    onAnswer: ((Pair<Set<Int>, String>) -> Unit)? = null,
) {
    val (selection, other) = question.answer ?: Pair(emptySet(), "")
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        QuestionText(question, showError = showError)
        Checkboxes(
            options = question.options,
            answers = selection,
            modifier = Modifier.fillMaxWidth(),
            onChange = onAnswer?.let { { onAnswer(it to other) } },
        )
        OtherBox(
            other,
            onChange = onAnswer?.let { { onAnswer(selection to it) } },
        )
    }
}

@Preview
@Composable
private fun QuestionWithMultiOptionsAndOtherElementPreview() {
    QuestionWithMultiOptionsAndOtherElement(
        question = QuestionWithMultiOptionsAndOther(
            id = "q1",
            text = "Which colors do you like?",
            options = listOf("Red", "Green", "Blue"),
            answer = Pair(setOf(0, 2), "Yellow"),
        ),
    ) { }
}
