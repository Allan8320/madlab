package com.example.exp7

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ---------- ENTITY (table) ----------
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val addedAt: Long = System.currentTimeMillis()
)

// ---------- DAO (queries) ----------
@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY title COLLATE NOCASE ASC")
    fun getBooksByTitle(): Flow<List<Book>>          // "Title A-Z"

    @Query("SELECT * FROM books ORDER BY addedAt DESC")
    fun getBooksNewestFirst(): Flow<List<Book>>      // "Newest First"

    @Insert
    suspend fun insert(book: Book)

    @Delete
    suspend fun delete(book: Book)
}

// ---------- DATABASE ----------
@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "reading_list_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
