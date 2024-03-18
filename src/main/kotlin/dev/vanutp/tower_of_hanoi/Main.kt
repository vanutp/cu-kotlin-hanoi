package dev.vanutp.tower_of_hanoi

fun getDiscCount(): Int {
    val minDiscCount = 1
    val maxDiscCount = 9
    while (true) {
        print("Enter the disc count ($minDiscCount-$maxDiscCount): ")
        val input = readln()
        val discCount = input.toIntOrNull()
        if (discCount == null) {
            println("Please enter a number")
            continue
        } else if (discCount < 1 || discCount > 9) {
            println("The disc count should be between 1 and 9")
            continue
        } else {
            return discCount
        }
    }
}

fun main() {
    println("Welcome to the Tower of Hanoi")
    println("Controls:")
    println("Space - take/place a disc")
    println("Left/Right arrow - move a disc")
    println()
    val discCount = getDiscCount()

    val game = Game(discCount)
    game.mainLoop()
}
