package com.example.windy.favoritescreen.view

import com.example.windy.database.Location

interface OnSelect {
    fun onSelect(location: Location)
    fun onRemove(location: Location)
}