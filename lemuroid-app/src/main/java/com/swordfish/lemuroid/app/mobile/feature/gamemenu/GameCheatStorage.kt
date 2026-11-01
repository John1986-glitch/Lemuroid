package com.swordfish.lemuroid.app.mobile.feature.gamemenu

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class GameCheatStorage(context: Context) {
    private val preferences =
        context.getSharedPreferences(
            "game_cheats",
            Context.MODE_PRIVATE,
        )

    fun load(gameId: String): List<GameCheat> {
        val savedData =
            preferences.getString("cheats_$gameId", null)
                ?: return emptyList()

        return runCatching {
            val array = JSONArray(savedData)

            buildList {
                for (i in 0 until array.length()) {
                    val item = array.optJSONObject(i) ?: continue

                    val name = item.optString("name")
                    val code = item.optString("code")

                    if (name.isNotBlank() && code.isNotBlank()) {
                        add(
                            GameCheat(
                                name = name,
                                code = code,
                                enabled = item.optBoolean("enabled", false),
                            ),
                        )
                    }
                }
            }
        }.getOrDefault(emptyList())
    }

    fun save(
        gameId: String,
        cheats: List<GameCheat>,
    ) {
        val array = JSONArray()

        cheats.forEach { cheat ->
            array.put(
                JSONObject()
                    .put("name", cheat.name)
                    .put("code", cheat.code)
                    .put("enabled", cheat.enabled),
            )
        }

        preferences.edit()
            .putString("cheats_$gameId", array.toString())
            .apply()
    }
}
