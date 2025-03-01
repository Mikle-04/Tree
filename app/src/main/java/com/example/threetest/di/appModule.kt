package com.example.three.di

import androidx.room.Room
import com.example.three.data.AppDatabase
import com.example.three.data.NodeRepository
import com.example.three.presentation.TreeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "tree-db").build() }
    single { get<AppDatabase>().nodeDao() }
    single { NodeRepository(get()) }
    viewModel { TreeViewModel(get()) }
}