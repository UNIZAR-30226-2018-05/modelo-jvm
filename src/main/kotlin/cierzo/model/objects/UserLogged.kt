package cierzo.model.objects

import io.swagger.client.ApiException
import io.swagger.client.model.AccountItem

object UserLogged {
    var user: User? = null

    /**
     * Return true if there are an user logged
     */
    fun isLogged(): Boolean {
        return if (user == null) {
            false
        } else {
            true
        }
    }

    /**
     * Used when an user log in (using the facade)
     */
    internal fun onLogin(accountItem: AccountItem) {
        user = User(accountItem)
    }

    /**
     * Used when an user log out (using the facade)
     */
    internal fun onLogout() {
        user = null
    }

    /**
     * Return the favorite playlist (id 0) of the logged user.
     */
    public fun getFavoritePlaylist(): Playlist {
        return if (isLogged()) {
            user!!.getPlaylists()[0]
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Return the authors of the songs on favorite playlist of the logged user.
     */
    public fun getAuthorsFromFavorite(): List<Author> {
        return if (isLogged()) {
            getFavoritePlaylist().getAuthors()
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Return the albums of the songs on favorite playlist of the logged user.
     */
    public fun getAlbumsFromFavorite(): List<Album> {
        return if (isLogged()) {
            getFavoritePlaylist().getAlbums()
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Add a song to the favorite songs playlist.
     */
    public fun addToFavorite(song: Song): Boolean {
        if (isLogged()) {
            try {
                getFavoritePlaylist().addSong(song)
                return true
            } catch (e: ApiException) {
                throw e
            }
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Remove a song from the favorite songs playlist.
     */
    public fun removeFromFavorite(index: Int): Boolean {
        if (isLogged()) {
            try {
                getFavoritePlaylist().removeSong(index)
                return true
            } catch (e: ApiException) {
                throw e
            }
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Create a new playlist with the logged user as the owner
     */
    public fun newPlaylist(name: String, desc: String) {
        if (isLogged()) {
            user!!.newPlaylist(name, desc)
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Remove a playlist from server and stored playlists list using the index of the stored playlists list.
     * Use with caution: Procure that the list haven't been reorder.
     */
    public fun removePlaylistAt(index: Int) {
        if (isLogged()) {
            user!!.removePlaylistAt(index)
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Remove a playlist from the server and reload the stored playlist list.
     * Use with caution: All user playlist must be downloaded again.
     */
    public fun removePlaylist(playlistId: String) {
        if (isLogged()) {
            user!!.removePlaylist(playlistId)
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Add a new friend (on server) and download and save it profile (as User) on the stored friends list.
     */
    public fun newFriend(friendId: String) {
        if (isLogged()) {
            user!!.newFriend(friendId)
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Remove a friend from server and stored friends list using the index of the stored friends list.
     * Use with caution: Procure that the list haven't been reorder.
     */
    public fun removeFriendAt(index: Int) {
        if (isLogged()) {
            user!!.removeFriendAt(index)
        } else {
            throw Exception("User not logged")
        }
    }

    /**
     * Remove a friend from the server and reload the stored friends list.
     * Use with caution: All user data (including playlist, but they aren't updated) must be downloaded again.
     * User must be logged to use this.
     */
    public fun removeFriend(friendId: String) {
        if (isLogged()) {
            user!!.removeFriend(friendId)
        } else {
            throw Exception("User not logged")
        }
    }
}