package edu.moravian.survey

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.moravian.survey.data.SurveyDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.Res

/**
 * The destination for the survey screen that can be filled out.
 */
@Serializable
data class SurveyScreen(val loadPrevious: Boolean = false)

/**
 * Displays the survey screen, which consists of a column with the survey view and a submit button.
 */
@Composable
fun SurveyScreen(
    database: SurveyDatabase,
    loadPrevious: Boolean = false,
    onCompleted: () -> Unit,
) {
    // TODO: complete (may need to add parameter(s))
    val scope = rememberCoroutineScope()
    val vm: SurveyVM = viewModel()

    val survey by vm.survey.collectAsState()
    val showErrors by vm.showErrors.collectAsState()

    LaunchedEffect(loadPrevious) {
        val dao = database.surveyDao()
        val latestSurveyId = dao.getLatestResult().firstOrNull()?.id

        if (latestSurveyId != null) {
            val loadedSurvey = AMISOS_R_SURVEY.load(latestSurveyId, dao)
            if (loadPrevious) {
                vm.loadFullSurvey(loadedSurvey)
            } else {
                vm.initialize(loadedSurvey)
            }
        } else {
            vm.initialize(null)
        }
    }

    Column(
        modifier =
            Modifier
                .safeContentPadding()
                .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // TODO: complete
        survey?.let { currentSurvey ->
            SurveyView(
                survey = currentSurvey,
                showErrors = showErrors,
                onAnswer = { updatedSurvey ->
                    vm.updateSurvey(updatedSurvey)
                },
            )
        }
        Button(
            onClick = {
                if (vm.attemptSubmit()) {
                    scope.launch {
                        vm.currentQuestions.save(
                            dao = database.surveyDao(),
                            timestamp = currentTimeMillis(),
                        )
                        onCompleted()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text("submit")
        }
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
