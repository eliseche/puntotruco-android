package com.quitarts.puntotruco.model

enum class CounterStyle {
    MATCHES,
    BEANS,
    TOKENS
}

enum class TableStyle {
    GREEN,
    BURGUNDY,
    LEATHER,
    WOOD
}

enum class ScreenMode {
    ALWAYS_ON,
    NORMAL
}

data class GameState(
    val ours: Int = 0,
    val theirs: Int = 0,
    val target: Int = 30,
    val customTarget: Int = 20,
    val counter: CounterStyle = CounterStyle.MATCHES,
    val table: TableStyle = TableStyle.GREEN,
    val screenMode: ScreenMode = ScreenMode.ALWAYS_ON,
    val hapticsEnabled: Boolean = true,
    val victoryAcknowledged: Boolean = false
)
