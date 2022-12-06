package connectfour

enum class Player(val color: String) {
    RED("o"),
    YELLOW("*"),
    BLANK(" ")
}
class Board(rows: Int, columns: Int) {
    val board = Array(rows) { Array<Player>(columns) { Player.BLANK } }
}
fun printBoard(board: Array<Array<Player>>) {
    for (i in board[0].indices) {
        print(" ${i + 1}")
    }
    println()
    for (i in board.indices) {
        print("║")
        for (j in 0..board[i].lastIndex) {

            if (board[i][j] == Player.BLANK) print(" ║")
            else if (board[i][j] == Player.RED) print("o║")
            else if (board[i][j] == Player.YELLOW) print("*║")

        }
        println()
    }

    for (i in 0..board.size * 2) {
        if (i == board.size * 2) print("╝")
        else if (i == 0) print("╚")
        else if (i % 2 == 0) print("╩")
        else if (i % 2 == 1) print("═")
    }
}

fun setBoardDimensions(): Array<Int> {
    var rowsAndColumns: String
    var rows: Int
    var columns: Int

    while (true) {
        println(
            "Set the board dimensions (Rows x Columns)\n" +
                    "Press Enter for default (6 x 7)"
        )
        rowsAndColumns = readln()
        rowsAndColumns = rowsAndColumns.filter { !it.isWhitespace() }
        rowsAndColumns = rowsAndColumns.lowercase()

        if (rowsAndColumns.isEmpty()) {
            rows = 6
            columns = 7
        } else if (!rowsAndColumns.matches("""\d+x\d+""".toRegex())) {
            println("Invalid input")
            continue
        } else {
            rows = rowsAndColumns.split("x").first().toInt()
            columns = rowsAndColumns.split("x").last().toInt()
        }

        if (rows !in 5..9) {
            println("Board rows should be from 5 to 9")
            continue
        } else if (columns !in 5..9) {
            println("Board columns should be from 5 to 9")
            continue
        } else {
            break
        }
    }

    return arrayOf(rows, columns)
}

fun main() {
    var rowsAndColumns: Array<Int>
    val rows: Int
    val columns: Int

    println("Connect Four")
    println("First player's name:")
    val firstPlayer = readln()
    println("Second player's name:")
    val secondPLayer = readln()

    rowsAndColumns = setBoardDimensions()
    rows = rowsAndColumns[0]
    columns = rowsAndColumns[1]

    println("$firstPlayer vs $secondPLayer")
    println("$rows X $columns board")


    val board = Board(rows, columns)
    printBoard(board.board)
}

/*repeat(columns) { print(" ${it + 1}") }
println()
for (i in 1..rows) {
    print("| ".repeat(columns))
    println("|")
}
println("=".repeat(columns * 2 + 1))*/