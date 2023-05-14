package com.example.composetodolistapp.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideTodoDatabaseDao(database: TodoDatabase): DatabaseDao {
        return database.todoDao()
    }
    @Provides
    fun provideTodoRepository(database: TodoDatabase): TodoRepository {
        return TodoRepository(database.todoDao())
    }
    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): TodoDatabase {
        return Room.databaseBuilder(
            appContext,
            TodoDatabase::class.java,
            "database"
        ).build()
    }
}


//class DatabaseModule {
//    @Provides
//    fun provideChannelDao(appDatabase: AppDatabase): ChannelDao {
//        return appDatabase.channelDao()
//    }
//}

//@Provides
//@Singleton
//fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
//    return Room.databaseBuilder(
//        appContext,
//        AppDatabase::class.java,
//        "RssReader"
//    ).build()
//}