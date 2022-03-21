package jp.ml.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.ml.movies.data.local.entity.Favorite

@Database(
    entities = [Favorite::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract val favoriteDao: FavoriteDao

    companion object {
        const val DATABASE_NAME = "movies_db"
    }
}