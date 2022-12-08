package connectfour

const val BoardMaximumSize = 9

enum class Player() {
    RED,
    YELLOW,
    BLANK
}

class ConnectFour {
    var rowsAndColumns: String
    var rows: Int
    var columns: Int
    val player = Array<String>(2) { "" }

    init {
        println("Connect Four")
        println("First player's name:")
        player[0] = readln()
        println("Second player's name:")
        player[1] = readln()

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
    }
    val board = Array(rows) { Array<Player>(columns) { Player.BLANK } }

    fun clearBoard() {
        for (i in board.indices) {
            for (j in board.indices) {
                board[i][j] = Player.BLANK
            }
        }
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

    fun play() {
        val score = Array<Int>(2) { 0 }
        var playersTurn = 0

        val games = numberOfGames()

        repeat(games) {
            if (games == 1) {
                println("${player[0]} vs ${player[1]}")
                println("${rows} X ${columns} board")
                println("Single game")
            } else if (it == 0) {
                println("${player[0]} vs ${player[1]}")
                println("${rows} X ${columns} board")
                println("Total $games games")
                println("Game #${it + 1}")
            } else {
                println("Game #${it + 1}")
            }

            printBoard(board)
            while (true) {
                println("${player[playersTurn]}'s turn:")
                val move = readln()

                if (move.lowercase() == "end") {
                    println("Game over!")
                    return
                }

                if (move.matches("""\d+""".toRegex())) {
                    if (move.toInt() > board[0].size || move.toInt() < 1) {
                        println("The column number is out of range (1 - ${board[0].size}) ")
                        continue
                    }
                } else {
                    println("Incorrect column number")
                    continue
                }

                if (!checkColumn(board, move.toInt())) {
                    continue
                }

                when (playersTurn) {
                    0 -> {
                        makeMove(board, move.toInt(), Player.RED)
                        if (checkGameResult(board)) {
                            printBoard(board)
                            println("Player ${player[playersTurn]} won")
                            score[playersTurn] += 2
                            //playersTurn = 0
                            playersTurn += 1
                            clearBoard()
                            break
                        }
                        playersTurn += 1
                    }

                    1 -> {
                        makeMove(board, move.toInt(), Player.YELLOW)
                        if (checkGameResult(board)) {
                            printBoard(board)
                            println("Player ${player[playersTurn]} won")
                            score[playersTurn] += 2
                            //playersTurn = 0
                            playersTurn -= 1
                            clearBoard()
                            break
                        }
                        playersTurn -= 1
                    }
                }

                printBoard(board)

                if (boardIsFull(board)) {
                    println("It is a draw")
                    score[0] += 1
                    score[1] += 1
                    clearBoard()
                    break
                }
            }
            println("Score\n${player[0]}: ${score[0]} ${player[1]}: ${score[1]}")
        }
        println("Game Over!")
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

    fun checkColumn(board: Array<Array<Player>>, move: Int): Boolean {
        if (board[0][move - 1] != Player.BLANK) {
            println("Column $move is full")
            return false
        }
        return true
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

    fun numberOfGames(): Int {
        while (true) {
            println(
                "Do you want to play single or multiple games?\n" +
                        "For a single game, input 1 or press Enter\n" +
                        "Input a number of games:"
            )
            val games = readln()

            if (games.matches("""\d+""".toRegex()) && games.toInt() > 0) {
                return games.toInt()
            } else if (games.isEmpty()) {
                return 1
            } else {
                println("Invalid input")
                continue
            }
        }
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
}


fun main() {
    val game = ConnectFour()
    game.play()
}
