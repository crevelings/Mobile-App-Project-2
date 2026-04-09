package edu.moravian.survey.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SurveyResult::class, SurveyAnswer::class], version = 1)
@TypeConverters(Converters::class)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}
