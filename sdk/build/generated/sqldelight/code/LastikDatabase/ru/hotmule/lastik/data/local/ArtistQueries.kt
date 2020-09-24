package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Transacter

interface ArtistQueries : Transacter {
  fun insert(artist: Artist)
}
