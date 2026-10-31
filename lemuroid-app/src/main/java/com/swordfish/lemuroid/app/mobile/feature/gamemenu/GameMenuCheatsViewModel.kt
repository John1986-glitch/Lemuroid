package com.swordfish.lemuroid.app.mobile.feature.gamemenu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameMenuCheatsViewModel(
    context: Context,
    private val gameId: String,
) : ViewModel() {

    private val storage =
        GameCheatStorage(context.applicationContext)

    private val _cheats =
        MutableStateFlow(storage.load(gameId))

    val cheats: StateFlow<List<GameCheat>> =
        _cheats.asStateFlow()

    fun addCheat(name: String, code: String) {
        val cleanName = name.trim()
        val cleanCode = code.trim()

        if (cleanName.isBlank() || cleanCode.isBlank()) {
            return
        }

        updateCheats(
            _cheats.value + GameCheat(
                name = cleanName,
                code = cleanCode,
            ),
        )
    }

    fun setCheatEnabled(
        index: Int,
        enabled: Boolean,
    ) {
        val current = _cheats.value
        if (index !in current.indices) return

        updateCheats(
            current.toMutableList().also {
                it[index] = it[index].copy(enabled = enabled)
            },
        )
    }

    fun deleteCheat(index: Int) {
        val current = _cheats.value
        if (index !in current.indices) return

        updateCheats(
            current.toMutableList().also {
                it.removeAt(index)
            },
        )
    }

    private fun updateCheats(updated: List<GameCheat>) {
        _cheats.value = updated
        storage.save(gameId, updated)
    }

    class Factory(
        private val context: Context,
        private val gameId: String,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(
            modelClass: Class<T>,
        ): T {
            if (modelClass.isAssignableFrom(GameMenuCheatsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GameMenuCheatsViewModel(
                    context.applicationContext,
                    gameId,
                ) as T
            }

            throw IllegalArgumentException(
                "Unknown ViewModel class: ${modelClass.name}",
            )
        }
    }
}
