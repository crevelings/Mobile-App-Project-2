package edu.moravian.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.moravian.survey.data.SurveyDatabase
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.*

/**
 * The home screen destination, which shows the current status and allows the user to take a survey
 * or view their history.
 */
@Serializable
data object HomeScreen

/**
 * The home screen, which shows the current status and allows the user to take a survey or view
 * their history.
 */
@Composable
fun HomeScreen(
    database: SurveyDatabase,
    onTakeSurvey: () -> Unit,
    onLoadPrevious: () -> Unit,
    onOpenHistory: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatusText(database)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onTakeSurvey) { Text(stringResource(Res.string.take_survey)) }
        Button(onClick = onLoadPrevious) { Text(stringResource(Res.string.load_previous)) }
        TextButton(onClick = onOpenHistory) { Text(stringResource(Res.string.view_history)) }
    }
}

@Composable
private fun StatusText(database: SurveyDatabase) {
    val latest by
        database.surveyDao().getLatestResult().collectAsState(initial = null)
    val now = currentTimeMillis()

    val mostRecent = latest

    Column {
        if (mostRecent == null) {
            Text(
                stringResource(Res.string.no_survey_results_yet),
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            Text(
                stringResource(Res.string.last_score, mostRecent.score),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                stringResource(Res.string.last_completed, formatEpochMillis(mostRecent.timestamp)),
                style = MaterialTheme.typography.bodyMedium,
            )
            reminderMessage(now, mostRecent.timestamp)?.let { reminderRes ->
                Text(
                    stringResource(reminderRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
