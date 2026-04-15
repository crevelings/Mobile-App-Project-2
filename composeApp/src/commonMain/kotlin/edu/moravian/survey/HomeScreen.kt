package edu.moravian.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onTakeSurvey: () -> Unit,
    onLoadPrevious: () -> Unit,
    onOpenHistory: () -> Unit,
) {
    // TODO: complete (may need to add parameter(s))
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatusText()
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onTakeSurvey) { Text(stringResource(Res.string.take_survey)) }
        Button(onClick = onLoadPrevious) { Text(stringResource(Res.string.load_previous)) }
        TextButton(onClick = onOpenHistory) { Text(stringResource(Res.string.view_history)) }
    }
}

@Composable
private fun StatusText() {
    val now = currentTimeMillis()
    // TODO: complete (may need to add parameter(s))
    // NOTES:
    // 1. Report if no surveys taken yet
    // 2. Show reminder messages using reminderMessage()
    // 3. Times can be displayed with formatEpochMillis()
}
