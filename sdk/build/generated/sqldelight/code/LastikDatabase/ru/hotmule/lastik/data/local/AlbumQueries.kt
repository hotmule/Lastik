package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Transacter

interface AlbumQueries : Transacter {
  fun insert(album: Album)
}
