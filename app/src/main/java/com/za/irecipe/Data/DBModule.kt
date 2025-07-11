package com.za.irecipe.Data

import android.content.Context
import com.squareup.moshi.Moshi
import com.za.irecipe.Data.dao.PreparationDao
import com.za.irecipe.Data.dao.RecipeDao
import com.za.irecipe.Data.db.AppDB
import com.za.irecipe.Data.remote.services.GeminiApiService
import com.za.irecipe.Data.repository.GenAiRepositoryImpl
import com.za.irecipe.Data.repository.PreparationRepositoryImpl
import com.za.irecipe.Data.repository.RecipeRepositoryImpl
import com.za.irecipe.Domain.repository.GenAiRepository
import com.za.irecipe.Domain.repository.PreparationRepository
import com.za.irecipe.Domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDB {
        return AppDB.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideRecipeDao(db: AppDB) = db.recipeDao()

    @Provides
    @Singleton
    fun providePreparationDao(db: AppDB) = db.preparationDao()

    @Provides
    @Singleton
    fun provideRecipeRepository(recipeDao: RecipeDao): RecipeRepository {
        return RecipeRepositoryImpl(recipeDao)
    }

    @Provides
    @Singleton
    fun providePreparationRepository(preparationDao: PreparationDao): PreparationRepository {
        return PreparationRepositoryImpl(preparationDao)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://vision.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    @Named("gemini")
    fun provideGeminiRetrofit(moshi: Moshi): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(@Named("gemini") retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGenAiRepository(geminiApiService: GeminiApiService): GenAiRepository {
        return GenAiRepositoryImpl(geminiApiService)
    }
}