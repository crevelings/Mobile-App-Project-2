package edu.moravian.survey

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

/**
 * The destination for the survey screen that can be filled out.
 */
@Serializable
data object SurveyScreen

/**
 * Displays the survey screen, which consists of a column with the survey view and a submit button.
 */
@Composable
fun SurveyScreen(
    onCompleted: () -> Unit,
) {
    // TODO: complete (may need to add parameter(s))
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // TODO: complete
    }
}

/**
 * Displays the given survey in a scrollable column. The survey will be rendered using the
 * [Survey.Render] function, and the column will have a border around it. The [onAnswer] callback
 * will be called whenever the user answers a question, and it will be passed the updated survey.
 * The [showErrors] parameter will be passed to the [Survey.Render] function to indicate whether
 * errors should be shown for unanswered questions.
 */
@Composable
fun ColumnScope.SurveyView(
    survey: Survey,
    showErrors: Boolean = false,
    onAnswer: ((Survey) -> Unit)? = null,
) {
    survey.Render(
        Modifier
            .weight(1f)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.medium,
            ).padding(10.dp)
            .fillMaxWidth(),
        showErrors,
        onAnswer,
    )
}
