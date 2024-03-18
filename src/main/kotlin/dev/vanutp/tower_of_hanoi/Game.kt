package dev.vanutp.tower_of_hanoi

import Key
import keyMap
import org.jline.keymap.BindingReader
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp
import toInt

class Game(discCount: Int) {
    private val state = GameState(discCount)
    private val term = TerminalBuilder.terminal()
    private val reader = BindingReader(term.reader())

    private fun getPossibleTowers(discSize: Int) =
        state.towers
            .withIndex()
            .filter { x ->
                (x.value.discs.lastOrNull() ?: (state.discCount + 1)) >= discSize
            }
            .map { x -> x.index }

    private fun getNonEmptyTowers() =
        state.towers
            .withIndex()
            .filter { x -> x.value.discs.size > 0 }
            .map { x -> x.index }

    private fun render() {
        term.puts(InfoCmp.Capability.clear_screen)
        val writer = term.writer()
        val fullTowerPadding = " ".repeat(state.discCount + 1)
        for (currHeight in (state.discCount + 1) downTo 1) {
            for ((towerIdx, tower) in state.towers.withIndex()) {
                val currentTowerElevated = state.isDiskElevated && state.cursorPosition == towerIdx
                val isElevatedDisc = tower.discs.size == currHeight - 1 && currentTowerElevated
                val isSkippedDisc = tower.discs.size == currHeight && currentTowerElevated

                if ((tower.discs.size >= currHeight || isElevatedDisc) && !isSkippedDisc) {
                    val discSize = tower.discs[currHeight - 1 - isElevatedDisc.toInt()]
                    val paddingSize = state.discCount + 1 - discSize
                    val isOddPadding = paddingSize % 2 == 1
                    val left = " ".repeat(paddingSize / 2) + if (isOddPadding) "▐" else ""
                    val right = (if (isOddPadding) "▌" else "") + " ".repeat(paddingSize / 2)
                    val center = "█".repeat(discSize - isOddPadding.toInt())
                    writer.print(left + center + right)
                } else {
                    writer.print(fullTowerPadding)
                }
                writer.print(" ")
            }
            writer.println()
        }

        val towerBase = "⎺".repeat(state.discCount + 1) + " "
        writer.println(towerBase.repeat(state.towers.size))
        val cursor = "^".repeat(state.discCount + 1)
        writer.print(("$fullTowerPadding ").repeat(state.cursorPosition) + cursor)
        writer.println()

        writer.flush()
    }

    private fun gameStep() {
        var start: Int? = null
        while (true) {
            render()
            when (val key = reader.readBinding(keyMap)) {
                Key.Space -> {
                    if (start == null) {
                        state.isDiskElevated = true
                        start = state.cursorPosition
                    } else if (start == state.cursorPosition) {
                        state.isDiskElevated = false
                        start = null
                    } else {
                        state.isDiskElevated = false
                        break
                    }
                }

                Key.Left, Key.Right -> {
                    val possibleTowers = if (state.isDiskElevated) {
                        getPossibleTowers(state.towers[state.cursorPosition].discs.last())
                    } else {
                        getNonEmptyTowers()
                    }
                    val delta = if (key == Key.Left) -1 else 1
                    val newTowerIdx =
                        (possibleTowers.size + possibleTowers.indexOf(state.cursorPosition) + delta) % possibleTowers.size
                    val oldCursor = state.cursorPosition
                    state.cursorPosition = possibleTowers[newTowerIdx]
                    if (state.isDiskElevated) {
                        state.towers[state.cursorPosition].discs.add(state.towers[oldCursor].discs.removeLast())
                    }
                }
            }
        }
    }

    fun mainLoop() {
        term.enterRawMode()
        while (state.status != GameStatus.Finished) {
            gameStep()
            state.movesDone += 1
            if (state.towers.last().discs.size == state.discCount) {
                render()
                term.writer().println("You did it!")
                term.writer().println("It took you ${state.movesDone} moves")
                term.writer().flush()
                break
            }
        }
    }

}
