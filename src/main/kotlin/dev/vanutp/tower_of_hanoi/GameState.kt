package dev.vanutp.tower_of_hanoi

enum class GameStatus {
    Running,
    Finished,
}

data class TowerState(
    val discs: MutableList<Int> = mutableListOf()
)

data class GameState(
    val discCount: Int,
    var status: GameStatus = GameStatus.Running,
    var movesDone: Int = 0,
    var cursorPosition: Int = 0,
    var isDiskElevated: Boolean = false,
    val towers: List<TowerState> = listOf(
        TowerState(discs = (discCount downTo 1).toMutableList()),
        TowerState(),
        TowerState(),
    )
)
