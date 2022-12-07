package connectfour

enum class Player() {
    RED,
    YELLOW,
    BLANK
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
            if (board[i][j] == Player.BLANK) {
                print(" ║")
            } else if (board[i][j] == Player.RED) {
                print("o║")
            } else if (board[i][j] == Player.YELLOW) {
                print("*║")
            }
        }
        println()
    }

    for (i in 0..board[0].size * 2) {
        if (i == board[0].size * 2) print("╝")
        else if (i == 0) print("╚")
        else if (i % 2 == 0) print("╩")
        else print("═")
    }
    println()
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

fun makeMove(board: Array<Array<Player>>, move: Int, player: Player) {
    loop@ for (i in board.lastIndex downTo 0) {
        for (j in 0..board[i].lastIndex) {
            if (j == move - 1 && board[i][j] == Player.BLANK) {
                board[i][j] = player
                break@loop
            }
        }
    }

}

fun checkColumn(board: Array<Array<Player>>, move: Int): Boolean {
    if (board[0][move - 1] != Player.BLANK) {
        println("Column $move is full")
        return false
    }
    return true
}

fun play() {
    val rowsAndColumns: Array<Int>
    val rows: Int
    val columns: Int
    val player = Array<String>(2) { "" }
    println("Connect Four")
    println("First player's name:")
    player[0] = readln()
    println("Second player's name:")
    player[1] = readln()

    rowsAndColumns = setBoardDimensions()
    rows = rowsAndColumns[0]
    columns = rowsAndColumns[1]

    println("${player[0]} vs ${player[1]}")
    println("$rows X $columns board")
    var playersTurn = 0

    val board = Board(rows, columns)
    printBoard(board.board)

    while (true) {
        println("${player[playersTurn]}'s turn:")
        val move = readln()

        if (move.lowercase() == "end") {
            println("Game over!")
            break
        }

        if (move.matches("""\d+""".toRegex())) {
            if (move.toInt() > board.board[0].size || move.toInt() < 1) {
                println("The column number is out of range (1 - ${board.board[0].size}) ")
                continue
            }
        } else {
            println("Incorrect column number")
            continue
        }

        if (!checkColumn(board.board, move.toInt())) {
            continue
        }

        when (playersTurn) {
            0 -> {
                makeMove(board.board, move.toInt(), Player.RED)
                playersTurn += 1
            }

            1 -> {
                makeMove(board.board, move.toInt(), Player.YELLOW)
                playersTurn -= 1
            }
        }

        printBoard(board.board)
    }
}

fun main() {
    play()
}
