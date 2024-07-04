package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static String[][] board = new String[3][3];
    static boolean isXturn = true;
    static int firstTurn = 0;

    public static void main(String[] args) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = " ";
            }
        }

        printBoard();

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите координаты в виде двух чисел от 0 до 2, разделённых пробелом (например, 0 1), или 'stop' для выхода:");

        while (true) {
            if (isXturn) {
                System.out.println("Ваш ход (X): ");
                String input = sc.nextLine();
                if (input.equals("stop")) {
                    break;
                }

                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    System.out.println("Неверный формат ввода. Попробуйте еще раз.");
                    continue;
                }

                int row, col;
                try {
                    row = Integer.parseInt(parts[0]);
                    col = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат ввода. Попробуйте еще раз.");
                    continue;
                }

                if (row < 0 || row > 2 || col < 0 || col > 2 || !board[row][col].equals(" ")) {
                    System.out.println("Неверный ход. Попробуйте еще раз.");
                    continue;
                }

                board[row][col] = "X";
                isXturn = false;
            } else {
                makeComputerMove();
                isXturn = true;
            }

            printBoard();

            String winner = checkWinner();
            if (!winner.equals("")) {
                System.out.println("Победитель: " + winner);
                break;
            }
        }

        sc.close();
    }

    public static void printBoard() {
        for (int i = 0; i < board.length; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
    }

    public static String checkWinner() {

        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals(" ")) {
                return board[i][0];
            }
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].equals(" ")) {
                return board[0][i];
            }
        }

        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals(" ")) {
            return board[0][0];
        }
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals(" ")) {
            return board[0][2];
        }

        boolean isDraw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals(" ")) {
                    isDraw = false;
                    break;
                }
            }
        }
        if (isDraw) {
            return "Ничья";
        }

        return "";
    }

    public static void makeComputerMove() {
        int rand = (int) (Math.random() * 4 + 1);
        if (firstTurn == 0){
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j].equals("X")) {
                        firstTurn++;
                        if (board[1][1].equals("X")) {
                            if (rand == 1) {
                                board[0][0] = "O";
                            } else if (rand == 2) {
                                board[0][2] = "O";
                            } else if (rand == 3) {
                                board[2][0] = "O";
                            } else if (rand == 4) {
                                board[2][2] = "O";
                            }
                        } else {
                            board[1][1] = "O";
                        }
                    }
                }
            }
        } else {
            int[] bestMove = findBestMove();
            board[bestMove[0]][bestMove[1]] = "O";
        }
    }

    public static int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals(" ")) {
                    board[i][j] = "O";
                    int score = minimax(board, 0, false);
                    board[i][j] = " ";
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }

        return move;
    }

    public static int minimax(String[][] board, int depth, boolean isMaximizing) {
        String result = checkWinner();
        if (!result.equals("")) {
            if (result.equals("O")) {
                return 10 - depth;
            } else if (result.equals("X")) {
                return depth - 10;
            } else {
                return 0;
            }
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals(" ")) {
                        board[i][j] = "O";
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = " ";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals(" ")) {
                        board[i][j] = "X";
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = " ";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}