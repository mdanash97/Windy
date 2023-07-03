package com.example.windy.database

sealed class DatabaseResult {
    class Success(val data: List<Location>): DatabaseResult()
    class Error(val message: String?): DatabaseResult()
    object Loading: DatabaseResult()
}