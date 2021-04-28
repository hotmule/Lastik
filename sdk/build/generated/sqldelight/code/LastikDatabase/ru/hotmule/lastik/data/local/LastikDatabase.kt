package ru.hotmule.lastik.`data`.local

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import ru.hotmule.lastik.`data`.local.sdk.newInstance
import ru.hotmule.lastik.`data`.local.sdk.schema

public interface LastikDatabase : Transacter {
  public val albumQueries: AlbumQueries

  public val artistQueries: ArtistQueries

  public val profileQueries: ProfileQueries

  public val scrobbleQueries: ScrobbleQueries

  public val topQueries: TopQueries

  public val trackQueries: TrackQueries

  public companion object {
    public val Schema: SqlDriver.Schema
      get() = LastikDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): LastikDatabase =
        LastikDatabase::class.newInstance(driver)
  }
}
