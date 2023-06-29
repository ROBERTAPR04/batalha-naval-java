import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {
    private static final int TAMANHO_MAPA = 10;
    private static final int TOTAL_BARCOS = 10;
    private static final int[] TAMANHO_BARCOS = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    private char[][] mapa;
    private boolean[][] mapaOponente;
    private boolean jogadorVsComputador;
    private boolean modoAutomatico;
    private Random random;

    public BatalhaNaval(boolean jogadorVsComputador) {
        this.jogadorVsComputador = jogadorVsComputador;
        this.modoAutomatico = false;
        this.random = new Random();

        mapa = new char[TAMANHO_MAPA][TAMANHO_MAPA];
        mapaOponente = new boolean[TAMANHO_MAPA][TAMANHO_MAPA];
        inicializarMapa();
    }

    private void inicializarMapa() {
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                mapa[i][j] = '-';
                mapaOponente[i][j] = false;
            }
        }
    }

    private void exibirMapa() {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                if (mapaOponente[i][j]) {
                    System.out.print(mapa[i][j] + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void posicionarBarcos() {
        Scanner scanner = new Scanner(System.in);
        int barcosRestantes = TOTAL_BARCOS;

        System.out.println("Posicionamento dos barcos:");

        while (barcosRestantes > 0) {
            exibirMapa();
            System.out.println("Posicione um barco de tamanho " + TAMANHO_BARCOS[barcosRestantes - 1] + ":");
            System.out.print("Digite a linha (A-J): ");
            char linhaChar = scanner.nextLine().toUpperCase().charAt(0);
            System.out.print("Digite a coluna (0-9): ");
            int coluna = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Digite a orientação (H para horizontal, V para vertical): ");
            char orientacao = scanner.nextLine().toUpperCase().charAt(0);

            int linha = linhaChar - 'A';

            if (linha >= 0 && linha < TAMANHO_MAPA && coluna >= 0 && coluna < TAMANHO_MAPA) {
                if (verificarPosicaoValida(linha, coluna, TAMANHO_BARCOS[barcosRestantes - 1], orientacao)) {
                    alocarBarco(linha, coluna, TAMANHO_BARCOS[barcosRestantes - 1], orientacao);
                    barcosRestantes--;
                } else {
                    System.out.println("Posição inválida. Escolha outra posição.");
                }
            } else {
                System.out.println("Coordenadas inválidas. Digite novamente.");
            }
        }
    }

    private boolean verificarPosicaoValida(int linha, int coluna, int tamanho, char orientacao) {
        if (orientacao == 'H') {
            if (coluna + tamanho > TAMANHO_MAPA) {
                return false;
            }
            for (int i = coluna; i < coluna + tamanho; i++) {
                if (mapa[linha][i] != '-') {
                    return false;
                }
            }
        } else if (orientacao == 'V') {
            if (linha + tamanho > TAMANHO_MAPA) {
                return false;
            }
            for (int i = linha; i < linha + tamanho; i++) {
                if (mapa[i][coluna] != '-') {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private void alocarBarco(int linha, int coluna, int tamanho, char orientacao) {
        if (orientacao == 'H') {
            for (int i = coluna; i < coluna + tamanho; i++) {
                mapa[linha][i] = 'B';
            }
        } else if (orientacao == 'V') {
            for (int i = linha; i < linha + tamanho; i++) {
                mapa[i][coluna] = 'B';
            }
        }
    }

    private void jogar() {
        Scanner scanner = new Scanner(System.in);
        boolean fimDeJogo = false;
        boolean jogador1 = true;

        while (!fimDeJogo) {
            if (jogador1) {
                System.out.println("Jogador 1 - É a sua vez!");
            } else {
                System.out.println("Jogador 2 - É a sua vez!");
            }

            if (jogador1 || !jogadorVsComputador) {
                exibirMapa();
                System.out.print("Digite a linha (A-J) para atirar: ");
                char linhaChar = scanner.nextLine().toUpperCase().charAt(0);
                System.out.print("Digite a coluna (0-9) para atirar: ");
                int coluna = scanner.nextInt();
                scanner.nextLine();
                System.out.println();

                int linha = linhaChar - 'A';

                if (linha >= 0 && linha < TAMANHO_MAPA && coluna >= 0 && coluna < TAMANHO_MAPA) {
                    if (mapaOponente[linha][coluna]) {
                        System.out.println("Você já atirou nessa posição antes. Repita a jogada.");
                        continue;
                    }
                    if (mapa[linha][coluna] == 'B') {
                        mapa[linha][coluna] = 'X';
                        mapaOponente[linha][coluna] = true;
                        System.out.println("Você acertou um barco!");
                    } else {
                        mapaOponente[linha][coluna] = true;
                        System.out.println("Você acertou na água.");
                    }
                } else {
                    System.out.println("Coordenadas inválidas. Digite novamente.");
                    continue;
                }
            } else {
                int linha = random.nextInt(TAMANHO_MAPA);
                int coluna = random.nextInt(TAMANHO_MAPA);

                if (mapaOponente[linha][coluna]) {
                    continue;
                }

                System.out.println("O computador atirou na posição " + (char) ('A' + linha) + coluna);

                if (mapa[linha][coluna] == 'B') {
                    mapa[linha][coluna] = 'X';
                    mapaOponente[linha][coluna] = true;
                    System.out.println("O computador acertou um barco!");
                } else {
                    mapaOponente[linha][coluna] = true;
                    System.out.println("O computador acertou na água.");
                }
            }

            jogador1 = !jogador1;

            fimDeJogo = verificarFimDeJogo();
        }

        exibirMapa();
        if (jogador1) {
            System.out.println("Jogador 2 venceu! Todos os barcos foram destruídos.");
        } else {
            System.out.println("Jogador 1 venceu! Todos os barcos foram destruídos.");
        }
    }

    private boolean verificarFimDeJogo() {
        for (int i = 0; i < TAMANHO_MAPA; i++) {
            for (int j = 0; j < TAMANHO_MAPA; j++) {
                if (mapa[i][j] == 'B') {
                    return false;
                }
            }
        }
        return true;
    }

    public void iniciarJogo() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo à Batalha Naval!");
        System.out.println("Escolha o modo de jogo:");
        System.out.println("1 - Jogador vs. Jogador");
        System.out.println("2 - Jogador vs. Computador");
        int modo = scanner.nextInt();
        scanner.nextLine();

        if (modo == 1) {
            jogadorVsComputador = false;
        } else if (modo == 2) {
            jogadorVsComputador = true;
        } else {
            System.out.println("Modo inválido. Encerrando o jogo.");
            return;
        }

        System.out.println("Escolha o método de posicionamento dos barcos:");
        System.out.println("1 - Escolha manual");
        System.out.println("2 - Escolha automática");
        int metodoPosicionamento = scanner.nextInt();
        scanner.nextLine();

        if (metodoPosicionamento == 1) {
            modoAutomatico = false;
        } else if (metodoPosicionamento == 2) {
            modoAutomatico = true;
        } else {
            System.out.println("Método inválido. Encerrando o jogo.");
            return;
        }

        if (!modoAutomatico) {
            posicionarBarcos();
        } else {
            posicionarBarcosAutomaticamente();
        }

        jogar();
    }

    private void posicionarBarcosAutomaticamente() {
        int barcosRestantes = TOTAL_BARCOS;

        while (barcosRestantes > 0) {
            int linha = random.nextInt(TAMANHO_MAPA);
            int coluna = random.nextInt(TAMANHO_MAPA);
            char orientacao = random.nextInt(2) == 0 ? 'H' : 'V';

            if (verificarPosicaoValida(linha, coluna, TAMANHO_BARCOS[barcosRestantes - 1], orientacao)) {
                alocarBarco(linha, coluna, TAMANHO_BARCOS[barcosRestantes - 1], orientacao);
                barcosRestantes--;
            }
        }
    }

    public static void main(String[] args) {
        BatalhaNaval jogo = new BatalhaNaval(true);
        jogo.iniciarJogo();
    }
}
