package cierzo.model.objects

import cierzo.model.APIConnector
import cierzo.model.ItemArrayConverter
import io.swagger.client.ApiException
import io.swagger.client.model.PlaylistItem
import org.threeten.bp.LocalDate

/**
 * Clase que representa una playlist de canciones
 * @param id: ID de la playlist
 * @param songs: Canciones que contiene la playlist
 * @param name: Nombre de la playlist
 * @param ownerId: ID del dueño de la playlist
 * @param description: Pequeña descripcion de la playlist
 * @param date: Fecha de creacion de la playlist
 * @param imageURL: Ruta de la imagen de la playlist
 *
 */
class Playlist(
    var id: String,
    var songs: MutableList<Song>,
    var name: String,
    private var ownerId: String,
    var description: String,
    var date: LocalDate,
    var imageURL: String,
    var songAmount: Int
) {

    constructor(playlistItem: PlaylistItem): this(
            id = playlistItem.id,
            songs = ItemArrayConverter.songFromSongs(playlistItem.songs),
            name = playlistItem.name,
            ownerId = playlistItem.ownerID,
            description = playlistItem.description,
            date = playlistItem.creationDate,
            imageURL = "",
            songAmount = 0) {
        imageURL = songs[0].imageURL // La imagen de la playlist es la imagen de la primera canción
        songAmount = songs.size
    }

    /**
     * Returns the user (as User) that owns the playlist. Replace getter for ownerId.
     * Can be called from the application.
     */
    public fun getOwner(): User {
        return if (ownerId.equals(UserLogged.user?.id)) {
            UserLogged.user!!
        } else {
            APIConnector.getUser(ownerId)
        }
    }

    /**
     * Add a new song to the playlist.
     * Only available for users who owns this playlist. Should be called only from an User object.
     */
    internal fun addSong(song: Song) {
        try {
            APIConnector.addSong(id, song.id)
            songs.add(song)
        } catch (e: ApiException) {
            throw e
        }

    }

    /**
     * Remove a song from the playlist.
     * Only available for users who owns this playlist. Should be called only from an User object.
     */
    internal fun removeSong(index: Int) {
        try {
            APIConnector.removeSong(id, songs[index].id)
            songs.removeAt(index)
        } catch (e: ApiException) {
            throw e
        }
    }

    /**
     * Swap the position of two songs in the playlist.
     * Only available for users who owns this playlist. Should be called only from an User object.
     */
    internal fun reorder(index1: Int, index2: Int) {
        songs[index1] = songs[index2].also { songs[index2] = songs[index1] }
    }

    /**
     * Remove this playlist from the server.
     * Only available for users who owns this playlist. Should be called only from an User object.
     */
    internal fun removeThis() {
        APIConnector.removePlaylist(id)
    }

    /**
     * Returns a list (not mutable) of all authors (as Author) on this playlist. Without repeating authors.
     * Can be called from the application.
     */
    public fun getAuthors(): List<Author> {
        var authors: MutableList<Author> = mutableListOf()
        for (song in songs) {
            authors.add(APIConnector.getAuthor(song.authorID))
        }
        return authors.distinct()
    }

    /**
     * Return a list (not mutable) of all albums (as Album) on this playlist. Without repeating albums.
     * Can be called from the application.
     */
    public fun getAlbums(): List<Album> {
        var albums: MutableList<Album> = mutableListOf()
        for (song in songs) {
            albums.add(APIConnector.getAlbum(song.albumID))
        }
        return albums.distinct()
    }
}