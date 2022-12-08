package connectfour

const val BoardMaximumSize = 9

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

fun checkPair(board: Array<Array<Player>>): Boolean {
    for (i in 0..board.lastIndex) {
        val pairs = board[i].toList().zipWithNext { a, b -> a == b && a != Player.BLANK }
            .zipWithNext { a, b -> a == b && a }
            .zipWithNext { a, b -> a == b && a }

        if (pairs.contains(true)) {
            return true
        }
    }

    return false
}

fun checkGameResult(board: Array<Array<Player>>): Boolean {
    val verticalBoard = Array(board[0].size) { Array<Player>(board.size) { Player.BLANK } }

    for (i in 0..board[0].lastIndex) {
        for (j in 0..board.lastIndex) {
            verticalBoard[i][j] = board[j][i]
        }
    }

    val diagonalBoard =
        Array(BoardMaximumSize * BoardMaximumSize) { Array<Player>(BoardMaximumSize) { Player.BLANK } }
    var step = 0
    for (i in 0..board.lastIndex) {
        for (j in 0..board[i].lastIndex) {
            if (j <= board[i].lastIndex - 3 && i <= board.lastIndex - 3) {
                diagonalBoard[step][j] = board[i][j]
                diagonalBoard[step][j + 1] = board[i + 1][j + 1]
                diagonalBoard[step][j + 2] = board[i + 2][j + 2]
                diagonalBoard[step][j + 3] = board[i + 3][j + 3]
                step++
            }
        }
    }

    val diagonalReversedBoard =
        Array(BoardMaximumSize * BoardMaximumSize) { Array<Player>(BoardMaximumSize) { Player.BLANK } }
    step = 0
    for (i in 0..board.lastIndex) {
        for (j in 0..board[i].lastIndex) {
            if (j >= 3 && i <= board.lastIndex - 3) {
                diagonalReversedBoard[step][j] = board[i][j]
                diagonalReversedBoard[step][j - 1] = board[i + 1][j - 1]
                diagonalReversedBoard[step][j - 2] = board[i + 2][j - 2]
                diagonalReversedBoard[step][j - 3] = board[i + 3][j - 3]
                step++
            }
        }
    }

    return when {
        checkPair(board) -> true
        checkPair(verticalBoard) -> true
        checkPair(diagonalBoard) -> true
        checkPair(diagonalReversedBoard) -> true
        else -> false
    }
}

fun boardIsFull(board: Array<Array<Player>>): Boolean {
    for (i in board) {
        for (j in i) {
            if (j == Player.BLANK) {
                return false
            }
        }
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
                if (checkGameResult(board.board)) {
                    printBoard(board.board)
                    println("Player ${player[playersTurn]} won")
                    println("Game Over!")
                    break
                }
                playersTurn += 1
            }

            1 -> {
                makeMove(board.board, move.toInt(), Player.YELLOW)
                if (checkGameResult(board.board)) {
                    printBoard(board.board)
                    println("Player ${player[playersTurn]} won")
                    println("Game Over!")
                    break
                }
                playersTurn -= 1
            }
        }

        printBoard(board.board)

        if (boardIsFull(board.board)) {
            println("It is a draw")
            println("Game Over!")
            break
        }
    }
}

fun main() {
    play()
}
