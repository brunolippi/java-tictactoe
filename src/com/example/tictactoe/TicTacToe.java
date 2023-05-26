package com.example.tictactoe;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class TicTacToe {
    static ArrayList<Integer> playerPositions = new ArrayList<>();
    static ArrayList<Integer> cpuPositions = new ArrayList<>();
    static String[][] gameBoard = {
            {" ", " | ", " ", " | ", " "},
            {"-", " + ", "-", " + ", "-"},
            {" ", " | ", " ", " | ", " "},
            {"-", " + ", "-", " + ", "-"},
            {" ", " | ", " ", " | ", " "},
    };

    public static void main(String[] args) {
        printGameBoard(false);
        while (true) {
            try {
                Scanner scan = new Scanner(System.in); // El scanner va a tomar el input de la consola.
                System.out.println("\033[1;37m" + "Ingrese la posición (1-9), 0 para ayuda." + "\u001B[0m");

                int playerPos = scan.nextInt();
                if (playerPos == 0) {
                    printGameBoard(true);
                    System.out.println("\033[1;37m" + "Ingrese la posición (1-9), 0 para ayuda." + "\u001B[0m");
                    playerPos = scan.nextInt();
                }
                while (playerPositions.contains(playerPos) || cpuPositions.contains(playerPos)) {
                    printGameBoard(false);
                    System.out.println("\u001B[31m" + "La posición ya está tomada! Intenta otra." + "\u001B[0m");
                    playerPos = scan.nextInt();
                }
                while (playerPos > 9 || playerPos < 0) {
                    printGameBoard(false);
                    System.out.println("\u001B[31m" + "El número debe ser mayor a 0 y menor a 9." + "\u001B[0m");
                    System.out.println("\033[1;37m" + "Ingrese la posición (1-9), 0 para ayuda." + "\u001B[0m");
                    playerPos = scan.nextInt();
                }
                placePiece(playerPos, "player");

                Winner winner = new Winner(playerPositions, cpuPositions);
                if (winner.checkWinner()) {
                    if (!returnWinnerAndReset(winner)) {
                        break;
                    }
                    resetGame();
                }

                Random random = new Random();
                int cpuPos = random.nextInt(9) + 1;
                while (playerPositions.contains(cpuPos) || cpuPositions.contains(cpuPos)) {
                    cpuPos = random.nextInt(9) + 1;
                }
                placePiece(cpuPos, "cpu");

                winner = new Winner(playerPositions, cpuPositions);
                if (winner.checkWinner()) {
                    if (returnWinnerAndReset(winner)) {
                        break;
                    }
                    resetGame();
                }

                printGameBoard(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error. Ingresa un número.");
            }

        }
    }

    @SafeVarargs
    public static void printGameBoard(boolean help, List<Integer>... winningPositions) {

        List<Integer> win = winningPositions.length > 0 ? winningPositions[0] : List.of(0, 0, 0);
        int position = 0;

        clearConsole();
        for (String[] row : gameBoard) {
            for (String c : row) {
                if (c.equals(" ") || c.equals("X") || c.equals("O")) {
                    position++;
                    if (help) {
                        if (c.equals(" ")) {
                            System.out.print("\u001B[32m" + position + "\u001B[0m");
                        } else {
                            System.out.print("\u001B[31m" + c + "\u001B[0m");
                        }
                    } else {
                        if (win.contains(position)) System.out.print("\u001B[32m" + c + "\u001B[0m");
                        else if (c.equals("X")) {
                            System.out.print("\u001B[33m" + c + "\u001B[0m");
                        } else {
                            System.out.print("\u001B[35m" + c + "\u001B[0m");
                        }
                    }
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        }
        System.out.println();
        if (win.contains(0)) System.out.println("(\u001B[33m X \u001B[0m) Player - " + "(\u001B[35m O \u001B[0m) CPU");
    }

    public static void placePiece(int pos, String user) {
        String symbol = "O";
        if (user.equals("player")) {
            symbol = "X";
            playerPositions.add(pos);
        } else {
            cpuPositions.add(pos);
        }

        switch (pos) {
            case 1: {
                gameBoard[0][0] = symbol;
                break;
            }
            case 2: {
                gameBoard[0][2] = symbol;
                break;
            }
            case 3: {
                gameBoard[0][4] = symbol;
                break;
            }
            case 4: {
                gameBoard[2][0] = symbol;
                break;
            }
            case 5: {
                gameBoard[2][2] = symbol;
                break;
            }
            case 6: {
                gameBoard[2][4] = symbol;
                break;
            }
            case 7: {
                gameBoard[4][0] = symbol;
                break;
            }
            case 8: {
                gameBoard[4][2] = symbol;
                break;
            }
            case 9: {
                gameBoard[4][4] = symbol;
                break;
            }
            default:
                break;
        }
    }

    public static boolean returnWinnerAndReset(Winner winner) {
        Scanner scan = new Scanner(System.in);
        List<Integer> winningPositions = winner.getWinningPositions();
        printGameBoard(false, winningPositions);
        System.out.println(winner.getWinner());
        System.out.println("Querés jugar devuelta? " + "\033[1;37;3m" + "(type true or false)" + "\033[0m");
        return scan.nextBoolean();
    }

    public static void resetGame() {
        gameBoard = new String[][]{
                {" ", " | ", " ", " | ", " "},
                {"-", " + ", "-", " + ", "-"},
                {" ", " | ", " ", " | ", " "},
                {"-", " + ", "-", " + ", "-"},
                {" ", " | ", " ", " | ", " "},
        };
        playerPositions = new ArrayList<>();
        cpuPositions = new ArrayList<>();
    }

    public static void clearConsole() {
        for (int i = 0; i < 20; ++i) {
            System.out.println();
        }
    }
}

class Winner {
    private final ArrayList<Integer> playerPositions;
    private final ArrayList<Integer> cpuPositions;
    private String winner = "";

    Winner(ArrayList<Integer> playerPositions, ArrayList<Integer> cpuPositions) {
        this.playerPositions = playerPositions;
        this.cpuPositions = cpuPositions;
    }

    Integer[][] winningPositions = {
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9},
            {1, 4, 7}, {2, 5, 8}, {3, 6, 9},
            {1, 5, 9}, {7, 5, 3},
    };

    public boolean checkWinner() {
        if (winner.length() == 0) {
            getWinner();
        }
        return winner.length() > 0;
    }

    public String getWinner() {
        for (Integer[] a : winningPositions) {
            List<Integer> l = Arrays.asList(a);
            if (playerPositions.containsAll(l)) {
                winner = "Player";
                return "\u001B[32m" + "Felicitaciones, ganaste!" + "\u001B[0m";
            } else if (cpuPositions.containsAll(l)) {
                winner = "CPU";
                return "Ganó la PC! Mal ahí :(";
            } else if (playerPositions.size() + cpuPositions.size() >= 9) {
                winner = "Nobody";
                return "Empate!";
            }
        }
        return "";
    }

    public List<Integer> getWinningPositions() {
        for (Integer[] pos : winningPositions) {
            List<Integer> positions = Arrays.asList(pos);
            if (playerPositions.containsAll(positions) || cpuPositions.containsAll(positions)) {
                return positions;
            }
        }
        return List.of(0, 0, 0);
    }
}
