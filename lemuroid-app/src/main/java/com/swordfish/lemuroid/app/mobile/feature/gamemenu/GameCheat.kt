package com.swordfish.lemuroid.app.mobile.feature.gamemenu

import java.io.Serializable

data class GameCheat(
    val name: String,
    val code: String,
    val enabled: Boolean = false,
) : Serializable
