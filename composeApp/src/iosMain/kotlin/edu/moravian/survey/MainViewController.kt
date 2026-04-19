package edu.moravian.survey

import androidx.compose.ui.window.ComposeUIViewController
import edu.moravian.survey.data.getDatabaseBuilder
import edu.moravian.survey.data.getRoomDatabase

fun MainViewController() = ComposeUIViewController {
    App(getRoomDatabase(getDatabaseBuilder()))
}