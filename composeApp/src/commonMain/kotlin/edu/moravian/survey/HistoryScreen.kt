package edu.moravian.survey

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.moravian.survey.data.DummyHasId
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.*

/**
 * History screen destination. Shows a list of surveys the user has taken, with the most recent at
 * the top.
 */
@Serializable
data object HistoryScreen

/**
 * Shows a list of surveys the user has taken, with the most recent at the top. Tapping on one
 * opens the [ViewSurveyScreen] for that survey.
 */
@Composable
fun HistoryScreen(
    onOpenSurvey: (Long) -> Unit,
) {
    // TODO: complete (may need to add parameter(s))
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val entries = emptyList<DummyHasId>() // TODO: load these (and change the class)

        Text(stringResource(Res.string.history), style = MaterialTheme.typography.headlineSmall)

        // TODO: show message if no surveys taken yet

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries, key = { it.id }) { result ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onOpenSurvey(result.id) }) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // TODO: complete
                    }
                }
            }
        }
    }
}
