package com.callblocker

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class BlockTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        BlockState.toggle(this)
        updateTile()
    }

    private fun updateTile() {
        val on = BlockState.isEnabled(this)
        qsTile?.let { tile ->
            tile.state = if (on) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.label = if (on) "Block OFF" else "Block ON"
            tile.updateTile()
        }
    }
}
