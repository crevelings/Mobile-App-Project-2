package edu.moravian.survey

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.moravian.survey.data.SurveyDatabase
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import surveytaker.composeapp.generated.resources.*

/**
 * The main app composable. Sets up the navigation graph and top app bar.
 */
@Composable
fun App(database: SurveyDatabase) {
    // TODO: complete (may need to add parameter(s))
    val navController = rememberNavController()
    MaterialTheme {
        Scaffold(
            topBar = {
                val curBackStackEntry by navController.currentBackStackEntryAsState()
                val curDestination = curBackStackEntry?.destination
                val onHomeScreen = curDestination?.hasRoute<HomeScreen>() == true
                TopAppBar(
                    back = if (onHomeScreen) null else ({ navController.navigateUp() }),
                )
            },
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = HomeScreen,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable<HomeScreen> {
                    HomeScreen(
                        database = database,
                        onTakeSurvey = { navController.navigate(SurveyScreen()) },
                        onLoadPrevious = { navController.navigate(SurveyScreen(loadPrevious = true)) },
                        onOpenHistory = { navController.navigate(HistoryScreen) },
                    )
                }
                composable<SurveyScreen> { navBackStackEntry ->
                    val dest = navBackStackEntry.toRoute<SurveyScreen>()
                    SurveyScreen(
                        database = database,
                        loadPrevious = dest.loadPrevious,
                        onCompleted = { navController.navigateUp() }
                    )
                }
                composable<HistoryScreen> {
                    HistoryScreen(
                        database = database,
                        onOpenSurvey = { surveyId ->
                            navController.navigate(ViewSurveyScreenDest(surveyId))
                        }
                    )
                }
                composable<ViewSurveyScreenDest> { navBackStackEntry ->
                    val surveyId = navBackStackEntry.toRoute<ViewSurveyScreenDest>().surveyId
                    ViewSurveyScreen(
                        database = database,
                        surveyId = surveyId
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(back: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(stringResource(Res.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (back != null) {
                Button(onClick = back) {
                    Icon(
                        painterResource(Res.drawable.arrow_back),
                        contentDescription = stringResource(Res.string.back),
                    )
                }
            }
        },
    )
}
