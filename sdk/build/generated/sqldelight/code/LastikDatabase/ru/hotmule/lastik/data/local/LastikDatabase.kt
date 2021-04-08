package ru.hotmule.lastik.data.local

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import ru.hotmule.lastik.data.local.sdk.newInstance
import ru.hotmule.lastik.data.local.sdk.schema

interface LastikDatabase : Transacter {
  val albumQueries: AlbumQueries

  val artistQueries: ArtistQueries

  val profileQueries: ProfileQueries

  val scrobbleQueries: ScrobbleQueries

  val topQueries: TopQueries

  val trackQueries: TrackQueries

  companion object {
    val Schema: SqlDriver.Schema
      get() = LastikDatabase::class.schema

    operator fun invoke(driver: SqlDriver, topAdapter: Top.Adapter): LastikDatabase =
        LastikDatabase::class.newInstance(driver, topAdapter)}
}
