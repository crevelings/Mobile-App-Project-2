package edu.moravian.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.moravian.survey.data.SurveyDatabase
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.*

/**
 * Navigation destination for the screen that shows a survey that has already been taken. The survey
 * id must be provided.
 */
@Serializable
data class ViewSurveyScreenDest(
    val surveyId: Long,
)

/**
 * Displays a survey that has already been taken. The survey is not editable, but it shows the
 * answers that were given.
 */
@Composable
fun ViewSurveyScreen(
    database: SurveyDatabase,
    surveyId: Long,
) {
    // TODO: complete (may need to add parameter(s))
    var loading by remember { mutableStateOf(true) }
    var survey by remember { mutableStateOf(AMISOS_R_SURVEY) }
    LaunchedEffect(surveyId) {
        // TODO: load the data
        loading = false
    }

    if (loading) {
        Row {
            CircularProgressIndicator()
            Text(stringResource(Res.string.loading))
        }
        return
    }

    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // TODO: complete
        SurveyView(survey, false, null)
    }
}
