package jp.ml.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.ml.movies.data.local.entity.Favorite

@Database(
    entities = [Favorite::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract val favoriteDao: FavoriteDao

    companion object {
        const val DATABASE_NAME = "movies_db"
    }
}