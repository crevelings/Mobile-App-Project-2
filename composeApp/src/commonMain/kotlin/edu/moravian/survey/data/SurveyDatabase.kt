package edu.moravian.survey.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(entities = [SurveyResult::class, SurveyAnswer::class], version = 1)
@TypeConverters(Converters::class)
@ConstructedBy(SurveyDatabaseConstructor::class)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object SurveyDatabaseConstructor : RoomDatabaseConstructor<SurveyDatabase> {
    override fun initialize(): SurveyDatabase
}
fun getRoomDatabase(
    builder: RoomDatabase.Builder<SurveyDatabase>,
): SurveyDatabase = builder.build()
