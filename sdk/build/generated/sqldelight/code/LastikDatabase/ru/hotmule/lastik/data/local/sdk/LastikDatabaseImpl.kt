package ru.hotmule.lastik.`data`.local.sdk

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.`internal`.copyOnWriteList
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass
import ru.hotmule.lastik.`data`.local.AlbumQueries
import ru.hotmule.lastik.`data`.local.AlbumTop
import ru.hotmule.lastik.`data`.local.ArtistQueries
import ru.hotmule.lastik.`data`.local.ArtistTop
import ru.hotmule.lastik.`data`.local.LastikDatabase
import ru.hotmule.lastik.`data`.local.LovedTracks
import ru.hotmule.lastik.`data`.local.Profile
import ru.hotmule.lastik.`data`.local.ProfileQueries
import ru.hotmule.lastik.`data`.local.ScrobbleData
import ru.hotmule.lastik.`data`.local.ScrobbleQueries
import ru.hotmule.lastik.`data`.local.Top
import ru.hotmule.lastik.`data`.local.TopQueries
import ru.hotmule.lastik.`data`.local.TrackQueries
import ru.hotmule.lastik.`data`.local.TrackTop
import ru.hotmule.lastik.domain.TopPeriod
import ru.hotmule.lastik.domain.TopType

internal val KClass<LastikDatabase>.schema: SqlDriver.Schema
  get() = LastikDatabaseImpl.Schema

internal fun KClass<LastikDatabase>.newInstance(driver: SqlDriver, topAdapter: Top.Adapter):
    LastikDatabase = LastikDatabaseImpl(driver, topAdapter)

private class LastikDatabaseImpl(
  driver: SqlDriver,
  internal val topAdapter: Top.Adapter
) : TransacterImpl(driver), LastikDatabase {
  public override val albumQueries: AlbumQueriesImpl = AlbumQueriesImpl(this, driver)

  public override val artistQueries: ArtistQueriesImpl = ArtistQueriesImpl(this, driver)

  public override val profileQueries: ProfileQueriesImpl = ProfileQueriesImpl(this, driver)

  public override val scrobbleQueries: ScrobbleQueriesImpl = ScrobbleQueriesImpl(this, driver)

  public override val topQueries: TopQueriesImpl = TopQueriesImpl(this, driver)

  public override val trackQueries: TrackQueriesImpl = TrackQueriesImpl(this, driver)

  public object Schema : SqlDriver.Schema {
    public override val version: Int
      get() = 1

    public override fun create(driver: SqlDriver): Unit {
      driver.execute(null, """
          |CREATE TABLE album (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    artistId INTEGER NOT NULL,
          |    name TEXT NOT NULL,
          |    lowArtwork TEXT,
          |    highArtwork TEXT,
          |    UNIQUE (artistId, name),
          |    FOREIGN KEY (artistId) REFERENCES artist(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE artist (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    name TEXT NOT NULL,
          |    UNIQUE (name)
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE profile (
          |    userName TEXT PRIMARY KEY NOT NULL,
          |    parentUserName TEXT,
          |    realName TEXT,
          |    lowResImage TEXT,
          |    highResImage TEXT,
          |    playCount INTEGER,
          |    registerDate INTEGER,
          |    FOREIGN KEY (parentUserName) REFERENCES profile(userName) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE scrobble (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    trackId INTEGER NOT NULL,
          |    listenedAt INTEGER NOT NULL,
          |    nowPlaying INTEGER DEFAULT 0 NOT NULL,
          |    UNIQUE (trackId, listenedAt),
          |    FOREIGN KEY (trackId) REFERENCES track(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE top (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    type INTEGER NOT NULL,
          |    period INTEGER NOT NULL,
          |    rank INTEGER NOT NULL,
          |    itemId INTEGER,
          |    playCount INTEGER,
          |    UNIQUE (type, period, rank)
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE track (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    artistId INTEGER NOT NULL,
          |    albumId INTEGER,
          |    name TEXT NOT NULL,
          |    loved INTEGER DEFAULT 0 NOT NULL,
          |    lovedAt INTEGER,
          |    UNIQUE (artistId, name),
          |    FOREIGN KEY (artistId) REFERENCES artist(id) ON DELETE CASCADE,
          |    FOREIGN KEY (albumId) REFERENCES album(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
    }

    public override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ): Unit {
    }
  }
}

private class AlbumQueriesImpl(
  private val database: LastikDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), AlbumQueries {
  internal val getId: MutableList<Query<*>> = copyOnWriteList()

  public override fun getId(artistId: Long, name: String): Query<Long> = GetIdQuery(artistId,
      name) { cursor ->
    cursor.getLong(0)!!
  }

  public override fun insert(
    artistId: Long,
    name: String,
    lowArtwork: String?,
    highArtwork: String?
  ): Unit {
    driver.execute(1619406,
        """INSERT OR IGNORE INTO album(artistId, name, lowArtwork, highArtwork) VALUES (?, ?, ?, ?)""",
        4) {
      bindLong(1, artistId)
      bindString(2, name)
      bindString(3, lowArtwork)
      bindString(4, highArtwork)
    }
    notifyQueries(1619406, {database.albumQueries.getId + database.scrobbleQueries.scrobbleData +
        database.topQueries.artistTop + database.topQueries.albumTop +
        database.topQueries.trackTop + database.trackQueries.lovedTracks})
  }

  private inner class GetIdQuery<out T : Any>(
    @JvmField
    public val artistId: Long,
    @JvmField
    public val name: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getId, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(-1248988836,
        """SELECT id FROM album WHERE artistId = ? AND name = ?""", 2) {
      bindLong(1, artistId)
      bindString(2, name)
    }

    public override fun toString(): String = "album.sq:getId"
  }
}

private class ArtistQueriesImpl(
  private val database: LastikDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), ArtistQueries {
  internal val getId: MutableList<Query<*>> = copyOnWriteList()

  public override fun getId(name: String): Query<Long> = GetIdQuery(name) { cursor ->
    cursor.getLong(0)!!
  }

  public override fun insert(name: String): Unit {
    driver.execute(-1722171082, """INSERT OR IGNORE INTO artist(name) VALUES (?)""", 1) {
      bindString(1, name)
    }
    notifyQueries(-1722171082, {database.artistQueries.getId +
        database.scrobbleQueries.scrobbleData + database.topQueries.artistTop +
        database.topQueries.albumTop + database.topQueries.trackTop +
        database.trackQueries.lovedTracks})
  }

  public override fun deleteLovedTracks(): Unit {
    driver.execute(-131000046, """
    |DELETE FROM artist WHERE EXISTS (
    |    SELECT t.id FROM track t
    |    WHERE artist.id = t.artistId AND t.lovedAt NOT NULL
    |)
    """.trimMargin(), 0)
    notifyQueries(-131000046, {database.albumQueries.getId + database.artistQueries.getId +
        database.scrobbleQueries.scrobbleData + database.topQueries.artistTop +
        database.topQueries.albumTop + database.topQueries.trackTop +
        database.trackQueries.getLovedTracksPageCount + database.trackQueries.getId +
        database.trackQueries.lovedTracks})
  }

  private inner class GetIdQuery<out T : Any>(
    @JvmField
    public val name: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getId, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(-473310988,
        """SELECT id FROM artist WHERE name = ?""", 1) {
      bindString(1, name)
    }

    public override fun toString(): String = "artist.sq:getId"
  }
}

private class ProfileQueriesImpl(
  private val database: LastikDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), ProfileQueries {
  internal val getProfile: MutableList<Query<*>> = copyOnWriteList()

  internal val getFriends: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> getProfile(mapper: (
    userName: String,
    parentUserName: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T> = Query(633869762, getProfile, driver, "profile.sq", "getProfile",
      "SELECT * FROM profile LIMIT 1") { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1),
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getLong(5),
      cursor.getLong(6)
    )
  }

  public override fun getProfile(): Query<Profile> = getProfile { userName, parentUserName,
      realName, lowResImage, highResImage, playCount, registerDate ->
    Profile(
      userName,
      parentUserName,
      realName,
      lowResImage,
      highResImage,
      playCount,
      registerDate
    )
  }

  public override fun <T : Any> getFriends(parentUserName: String?, mapper: (
    userName: String,
    parentUserName: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?
  ) -> T): Query<T> = GetFriendsQuery(parentUserName) { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1),
      cursor.getString(2),
      cursor.getString(3),
      cursor.getString(4),
      cursor.getLong(5),
      cursor.getLong(6)
    )
  }

  public override fun getFriends(parentUserName: String?): Query<Profile> =
      getFriends(parentUserName) { userName, parentUserName_, realName, lowResImage, highResImage,
      playCount, registerDate ->
    Profile(
      userName,
      parentUserName_,
      realName,
      lowResImage,
      highResImage,
      playCount,
      registerDate
    )
  }

  public override fun deleteFriends(parentUserName: String?): Unit {
    driver.execute(null,
        """DELETE FROM profile WHERE parentUserName ${ if (parentUserName == null) "IS" else "=" } ?""",
        1) {
      bindString(1, parentUserName)
    }
    notifyQueries(-761508165, {database.profileQueries.getProfile +
        database.profileQueries.getFriends})
  }

  public override fun deleteAll(): Unit {
    driver.execute(1716453831, """DELETE FROM profile""", 0)
    notifyQueries(1716453831, {database.profileQueries.getProfile +
        database.profileQueries.getFriends})
  }

  public override fun upsert(
    parentUsername: String?,
    realName: String?,
    lowResImage: String?,
    highResImage: String?,
    playCount: Long?,
    registerDate: Long?,
    userName: String,
    parentUserName: String?
  ): Unit {
    driver.execute(-1086870225, """
    |UPDATE profile
    |    SET parentUserName = ?,
    |        realName = ?,
    |        lowResImage = ?,
    |        highResImage = ?,
    |        playCount = ?,
    |        registerDate = ?
    |    WHERE userName = ?
    """.trimMargin(), 7) {
      bindString(1, parentUsername)
      bindString(2, realName)
      bindString(3, lowResImage)
      bindString(4, highResImage)
      bindLong(5, playCount)
      bindLong(6, registerDate)
      bindString(7, userName)
    }
    driver.execute(-1086870224, """
    |INSERT OR IGNORE
    |    INTO profile (userName, parentUserName, realName, lowResImage, highResImage, playCount, registerDate)
    |    VALUES (?, ?, ?, ?, ?, ?, ?)
    """.trimMargin(), 7) {
      bindString(1, userName)
      bindString(2, parentUserName)
      bindString(3, realName)
      bindString(4, lowResImage)
      bindString(5, highResImage)
      bindLong(6, playCount)
      bindLong(7, registerDate)
    }
    notifyQueries(1259202814, {database.profileQueries.getProfile +
        database.profileQueries.getFriends})
  }

  private inner class GetFriendsQuery<out T : Any>(
    @JvmField
    public val parentUserName: String?,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getFriends, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(null,
        """SELECT * FROM profile WHERE parentUserName ${ if (parentUserName == null) "IS" else "=" } ?""",
        1) {
      bindString(1, parentUserName)
    }

    public override fun toString(): String = "profile.sq:getFriends"
  }
}

private class ScrobbleQueriesImpl(
  private val database: LastikDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), ScrobbleQueries {
  internal val getScrobblesCount: MutableList<Query<*>> = copyOnWriteList()

  internal val scrobbleData: MutableList<Query<*>> = copyOnWriteList()

  public override fun getScrobblesCount(): Query<Long> = Query(139991260, getScrobblesCount, driver,
      "scrobble.sq", "getScrobblesCount", "SELECT COUNT(id) FROM scrobble") { cursor ->
    cursor.getLong(0)!!
  }

  public override fun <T : Any> scrobbleData(mapper: (
    listenedAt: Long,
    nowPlaying: Boolean,
    loved: Boolean?,
    track: String?,
    artist: String?,
    album: String?,
    lowArtwork: String?
  ) -> T): Query<T> = Query(1320613324, scrobbleData, driver, "scrobble.sq", "scrobbleData", """
  |SELECT
  |    s.listenedAt,
  |    s.nowPlaying,
  |    t.loved,
  |    t.name AS track,
  |    ar.name AS artist,
  |    al.name AS album,
  |    al.lowArtwork
  |FROM scrobble s
  |LEFT JOIN track t ON s.trackId = t.id
  |LEFT JOIN album al ON t.albumId = al.id
  |LEFT JOIN artist ar ON al.artistId = ar.id
  |ORDER BY nowPlaying DESC, s.listenedAt DESC
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!! == 1L,
      cursor.getLong(2)?.let { it == 1L },
      cursor.getString(3),
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6)
    )
  }

  public override fun scrobbleData(): Query<ScrobbleData> = scrobbleData { listenedAt, nowPlaying,
      loved, track, artist, album, lowArtwork ->
    ScrobbleData(
      listenedAt,
      nowPlaying,
      loved,
      track,
      artist,
      album,
      lowArtwork
    )
  }

  public override fun insert(
    trackId: Long,
    listenedAt: Long,
    nowPlaying: Boolean
  ): Unit {
    driver.execute(-1853325067,
        """INSERT INTO scrobble(trackId, listenedAt, nowPlaying) VALUES (?, ?, ?)""", 3) {
      bindLong(1, trackId)
      bindLong(2, listenedAt)
      bindLong(3, if (nowPlaying) 1L else 0L)
    }
    notifyQueries(-1853325067, {database.scrobbleQueries.getScrobblesCount +
        database.scrobbleQueries.scrobbleData})
  }

  public override fun deleteAll(): Unit {
    driver.execute(-576659398, """DELETE FROM scrobble""", 0)
    notifyQueries(-576659398, {database.scrobbleQueries.getScrobblesCount +
        database.scrobbleQueries.scrobbleData})
  }
}

private class TopQueriesImpl(
  private val database: LastikDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), TopQueries {
  internal val getTopCount: MutableList<Query<*>> = copyOnWriteList()

  internal val artistTop: MutableList<Query<*>> = copyOnWriteList()

  internal val albumTop: MutableList<Query<*>> = copyOnWriteList()

  internal val trackTop: MutableList<Query<*>> = copyOnWriteList()

  public override fun getTopCount(type: TopType, period: TopPeriod): Query<Long> =
      GetTopCountQuery(type, period) { cursor ->
    cursor.getLong(0)!!
  }

  public override fun <T : Any> artistTop(period: TopPeriod, mapper: (
    name: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T> = ArtistTopQuery(period) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getLong(1)!!.toInt(),
      cursor.getLong(2),
      cursor.getString(3)
    )
  }

  public override fun artistTop(period: TopPeriod): Query<ArtistTop> = artistTop(period) { name,
      rank, playCount, lowArtwork ->
    ArtistTop(
      name,
      rank,
      playCount,
      lowArtwork
    )
  }

  public override fun <T : Any> albumTop(period: TopPeriod, mapper: (
    artist: String?,
    album: String?,
    lowArtwork: String?,
    rank: Int,
    playCount: Long?
  ) -> T): Query<T> = AlbumTopQuery(period) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getString(1),
      cursor.getString(2),
      cursor.getLong(3)!!.toInt(),
      cursor.getLong(4)
    )
  }

  public override fun albumTop(period: TopPeriod): Query<AlbumTop> = albumTop(period) { artist,
      album, lowArtwork, rank, playCount ->
    AlbumTop(
      artist,
      album,
      lowArtwork,
      rank,
      playCount
    )
  }

  public override fun <T : Any> trackTop(period: TopPeriod, mapper: (
    artist: String?,
    track: String?,
    rank: Int,
    playCount: Long?,
    lowArtwork: String?
  ) -> T): Query<T> = TrackTopQuery(period) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getString(1),
      cursor.getLong(2)!!.toInt(),
      cursor.getLong(3),
      cursor.getString(4)
    )
  }

  public override fun trackTop(period: TopPeriod): Query<TrackTop> = trackTop(period) { artist,
      track, rank, playCount, lowArtwork ->
    TrackTop(
      artist,
      track,
      rank,
      playCount,
      lowArtwork
    )
  }

  public override fun insert(
    type: TopType,
    period: TopPeriod,
    rank: Int,
    itemId: Long?,
    playCount: Long?
  ): Unit {
    driver.execute(1869053748,
        """INSERT INTO top(type, period, rank, itemId, playCount) VALUES (?, ?, ?, ?, ?)""", 5) {
      bindLong(1, database.topAdapter.typeAdapter.encode(type))
      bindLong(2, database.topAdapter.periodAdapter.encode(period))
      bindLong(3, rank.toLong())
      bindLong(4, itemId)
      bindLong(5, playCount)
    }
    notifyQueries(1869053748, {database.topQueries.getTopCount + database.topQueries.artistTop +
        database.topQueries.albumTop + database.topQueries.trackTop})
  }

  public override fun deleteTop(type: TopType, period: TopPeriod): Unit {
    driver.execute(1050021199, """DELETE FROM top WHERE type = ? AND period = ?""", 2) {
      bindLong(1, database.topAdapter.typeAdapter.encode(type))
      bindLong(2, database.topAdapter.periodAdapter.encode(period))
    }
    notifyQueries(1050021199, {database.topQueries.getTopCount + database.topQueries.artistTop +
        database.topQueries.albumTop + database.topQueries.trackTop})
  }

  public override fun upsert(
    itemId: Long?,
    playCount: Long?,
    type: TopType,
    period: TopPeriod,
    rank: Int
  ): Unit {
    driver.execute(2078219995, """
    |UPDATE top
    |    SET itemId = ?, playCount = ?
    |    WHERE type = ? AND period = ? AND rank = ?
    """.trimMargin(), 5) {
      bindLong(1, itemId)
      bindLong(2, playCount)
      bindLong(3, database.topAdapter.typeAdapter.encode(type))
      bindLong(4, database.topAdapter.periodAdapter.encode(period))
      bindLong(5, rank.toLong())
    }
    driver.execute(2078219996, """
    |INSERT OR IGNORE INTO top(type, period, rank, itemId, playCount)
    |    VALUES (?, ?, ?, ?, ?)
    """.trimMargin(), 5) {
      bindLong(1, database.topAdapter.typeAdapter.encode(type))
      bindLong(2, database.topAdapter.periodAdapter.encode(period))
      bindLong(3, rank.toLong())
      bindLong(4, itemId)
      bindLong(5, playCount)
    }
    notifyQueries(-2080516694, {database.topQueries.getTopCount + database.topQueries.artistTop +
        database.topQueries.albumTop + database.topQueries.trackTop})
  }

  private inner class GetTopCountQuery<out T : Any>(
    @JvmField
    public val type: TopType,
    @JvmField
    public val period: TopPeriod,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getTopCount, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(1971077781,
        """SELECT COUNT(id) FROM top WHERE type = ? AND period = ?""", 2) {
      bindLong(1, database.topAdapter.typeAdapter.encode(type))
      bindLong(2, database.topAdapter.periodAdapter.encode(period))
    }

    public override fun toString(): String = "top.sq:getTopCount"
  }

  private inner class ArtistTopQuery<out T : Any>(
    @JvmField
    public val period: TopPeriod,
    mapper: (SqlCursor) -> T
  ) : Query<T>(artistTop, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(1983249971, """
    |SELECT
    |    ar.name,
    |    t.rank,
    |    t.playCount,
    |    al.lowArtwork
    |FROM top t
    |LEFT JOIN artist ar ON itemId = ar.id
    |LEFT JOIN album al ON al.id = (
    |    SELECT a.id FROM album a
    |    LEFT JOIN top t_al ON a.id = t_al.itemId
    |    WHERE a.artistId = ar.id ORDER BY t_al.playCount DESC LIMIT 1
    |)
    |WHERE t.type = 0 AND t.period = ? ORDER BY t.rank
    """.trimMargin(), 1) {
      bindLong(1, database.topAdapter.periodAdapter.encode(period))
    }

    public override fun toString(): String = "top.sq:artistTop"
  }

  private inner class AlbumTopQuery<out T : Any>(
    @JvmField
    public val period: TopPeriod,
    mapper: (SqlCursor) -> T
  ) : Query<T>(albumTop, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(1854605857, """
    |SELECT
    |    ar.name AS artist,
    |    al.name AS album,
    |    al.lowArtwork,
    |    t.rank,
    |    t.playCount
    |FROM top t
    |LEFT JOIN album al ON itemId = al.id
    |LEFT JOIN artist ar ON al.artistId = ar.id
    |WHERE t.type = 1 AND t.period = ? ORDER BY t.rank
    """.trimMargin(), 1) {
      bindLong(1, database.topAdapter.periodAdapter.encode(period))
    }

    public override fun toString(): String = "top.sq:albumTop"
  }

  private inner class TrackTopQuery<out T : Any>(
    @JvmField
    public val period: TopPeriod,
    mapper: (SqlCursor) -> T
  ) : Query<T>(trackTop, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(1593006533, """
    |SELECT
    |    ar.name AS artist,
    |    tr.name AS track,
    |    t.rank,
    |    t.playCount,
    |    al.lowArtwork
    |FROM top t
    |LEFT JOIN track tr ON itemId = tr.id
    |LEFT JOIN album al ON tr.albumId = al.id
    |LEFT JOIN artist ar ON tr.artistId = ar.id
    |WHERE t.type = 2 AND t.period = ? ORDER BY t.rank
    """.trimMargin(), 1) {
      bindLong(1, database.topAdapter.periodAdapter.encode(period))
    }

    public override fun toString(): String = "top.sq:trackTop"
  }
}

private class TrackQueriesImpl(
  private val database: LastikDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), TrackQueries {
  internal val getLovedTracksPageCount: MutableList<Query<*>> = copyOnWriteList()

  internal val getId: MutableList<Query<*>> = copyOnWriteList()

  internal val lovedTracks: MutableList<Query<*>> = copyOnWriteList()

  public override fun getLovedTracksPageCount(): Query<Long> = Query(-1872453589,
      getLovedTracksPageCount, driver, "track.sq", "getLovedTracksPageCount",
      "SELECT COUNT(track.id) FROM track WHERE lovedAt NOT NULL") { cursor ->
    cursor.getLong(0)!!
  }

  public override fun getId(artistId: Long, name: String): Query<Long> = GetIdQuery(artistId,
      name) { cursor ->
    cursor.getLong(0)!!
  }

  public override fun <T : Any> lovedTracks(mapper: (
    artist: String?,
    track: String,
    loved: Boolean,
    lovedAt: Long?,
    lowArtwork: String?
  ) -> T): Query<T> = Query(-1242084855, lovedTracks, driver, "track.sq", "lovedTracks", """
  |SELECT
  |    ar.name AS artist,
  |    t.name AS track,
  |    t.loved,
  |    t.lovedAt,
  |    al.lowArtwork
  |FROM track t
  |LEFT JOIN album al ON t.albumId = al.id
  |LEFT JOIN artist ar ON t.artistId = ar.id
  |WHERE t.lovedAt NOT NULL ORDER BY lovedAt DESC
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getString(0),
      cursor.getString(1)!!,
      cursor.getLong(2)!! == 1L,
      cursor.getLong(3),
      cursor.getString(4)
    )
  }

  public override fun lovedTracks(): Query<LovedTracks> = lovedTracks { artist, track, loved,
      lovedAt, lowArtwork ->
    LovedTracks(
      artist,
      track,
      loved,
      lovedAt,
      lowArtwork
    )
  }

  public override fun insert(
    artistId: Long,
    albumId: Long?,
    name: String,
    loved: Boolean,
    lovedAt: Long?
  ): Unit {
    driver.execute(1405765034,
        """INSERT OR IGNORE INTO track(artistId, albumId, name, loved, lovedAt) VALUES (?, ?, ?, ?, ?)""",
        5) {
      bindLong(1, artistId)
      bindLong(2, albumId)
      bindString(3, name)
      bindLong(4, if (loved) 1L else 0L)
      bindLong(5, lovedAt)
    }
    notifyQueries(1405765034, {database.scrobbleQueries.scrobbleData +
        database.topQueries.trackTop + database.trackQueries.getLovedTracksPageCount +
        database.trackQueries.getId + database.trackQueries.lovedTracks})
  }

  public override fun dropLovedTrackDates(): Unit {
    driver.execute(50389644, """
    |UPDATE track SET lovedAt = NULL WHERE id IN (
    |    SELECT id FROM track WHERE lovedAt NOT NULL
    |)
    """.trimMargin(), 0)
    notifyQueries(50389644, {database.scrobbleQueries.scrobbleData + database.topQueries.trackTop +
        database.trackQueries.getLovedTracksPageCount + database.trackQueries.getId +
        database.trackQueries.lovedTracks})
  }

  public override fun updateTrackLove(
    loved: Boolean,
    track: String,
    artist: String
  ): Unit {
    driver.execute(-1313635133, """
    |UPDATE track SET loved = ? WHERE name = ? AND artistId = (
    |    SELECT artistId FROM artist WHERE name = ?
    |)
    """.trimMargin(), 3) {
      bindLong(1, if (loved) 1L else 0L)
      bindString(2, track)
      bindString(3, artist)
    }
    notifyQueries(-1313635133, {database.scrobbleQueries.scrobbleData +
        database.topQueries.trackTop + database.trackQueries.getLovedTracksPageCount +
        database.trackQueries.getId + database.trackQueries.lovedTracks})
  }

  public override fun upsertRecentTrack(
    albumId: Long?,
    loved: Boolean,
    artistId: Long,
    name: String,
    lovedAt: Long?
  ): Unit {
    driver.execute(-440884447, """
    |UPDATE track
    |    SET albumId = ?,
    |        loved = ?
    |    WHERE artistId = ? AND name = ?
    """.trimMargin(), 4) {
      bindLong(1, albumId)
      bindLong(2, if (loved) 1L else 0L)
      bindLong(3, artistId)
      bindString(4, name)
    }
    driver.execute(-440884446, """
    |INSERT OR IGNORE
    |    INTO track(artistId, albumId, name, loved, lovedAt)
    |    VALUES (?, ?, ?, ?, ?)
    """.trimMargin(), 5) {
      bindLong(1, artistId)
      bindLong(2, albumId)
      bindString(3, name)
      bindLong(4, if (loved) 1L else 0L)
      bindLong(5, lovedAt)
    }
    notifyQueries(-1126714512, {database.scrobbleQueries.scrobbleData +
        database.topQueries.trackTop + database.trackQueries.getLovedTracksPageCount +
        database.trackQueries.getId + database.trackQueries.lovedTracks})
  }

  public override fun upsertLovedTrack(
    loved: Boolean,
    lovedAt: Long?,
    artistId: Long,
    name: String,
    albumId: Long?
  ): Unit {
    driver.execute(688319210, """
    |UPDATE track
    |    SET loved = ?,
    |        lovedAt = ?
    |    WHERE artistId = ? AND name = ?
    """.trimMargin(), 4) {
      bindLong(1, if (loved) 1L else 0L)
      bindLong(2, lovedAt)
      bindLong(3, artistId)
      bindString(4, name)
    }
    driver.execute(688319211, """
    |INSERT OR IGNORE
    |    INTO track(artistId, albumId, name, loved, lovedAt)
    |    VALUES (?, ?, ?, ?, ?)
    """.trimMargin(), 5) {
      bindLong(1, artistId)
      bindLong(2, albumId)
      bindString(3, name)
      bindLong(4, if (loved) 1L else 0L)
      bindLong(5, lovedAt)
    }
    notifyQueries(-1451796103, {database.scrobbleQueries.scrobbleData +
        database.topQueries.trackTop + database.trackQueries.getLovedTracksPageCount +
        database.trackQueries.getId + database.trackQueries.lovedTracks})
  }

  private inner class GetIdQuery<out T : Any>(
    @JvmField
    public val artistId: Long,
    @JvmField
    public val name: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getId, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(-1480788480,
        """SELECT id FROM track WHERE artistId = ? AND name = ?""", 2) {
      bindLong(1, artistId)
      bindString(2, name)
    }

    public override fun toString(): String = "track.sq:getId"
  }
}
