import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MiClase extends JPanel {
    // CONSTANTES
    private static final int ANCHO = 500;
    private static final int ALTURA = 500;
    private static final int CELDASTAMANO = ALTURA / 4;
    private static final int NUMWIN = 2048;
    // COLORES
    private static final Color BLANCO = new Color(255, 255, 255);
    private static final Color NEGRO = new Color(0, 0, 0);
    private static final Color ROJO = new Color(255, 0, 0);
    private static final Color VERDE = new Color(0, 255, 0);
    private static final Color[] COLORES = {
            new Color(204, 192, 179), new Color(225, 228, 200),
            new Color(237, 224, 200), new Color(242, 177, 121),
            new Color(246, 149, 99),  new Color(246, 124, 95),
            new Color(246, 94, 59),   new Color(237, 207, 114),
            new Color(237, 204, 97),  new Color(237, 200, 88),
            new Color(237, 197, 63),  new Color(237, 194, 46)
    };

    // Tablero de juego
    private int[][] tablero = new int[4][4];
    private boolean estadoPerdido = false;
    private boolean estadoGanado = false;
    private int score = 0;
    private int bestScore = 0;
    private JLabel scoreLabel;
    private JLabel bestScoreLabel;

    public native boolean compararSiEs0(int a);
    public native int regresarMayor(int a, int b);
    public native boolean comprobarSiPierde(int a);
    public native boolean comprobarSiGana(int a, int b);
    public native int sumarNumeros(int a, int b);
    public native int numAleatorio248();
    public native int incrementarScore(int a, int b);
    static {
        System.loadLibrary("MiBiblioteca");
    }

    public MiClase(JLabel scoreLabel, JLabel bestScoreLabel) {
        this.scoreLabel = scoreLabel;
        this.bestScoreLabel = bestScoreLabel;

        setPreferredSize(new Dimension(ANCHO, ALTURA));
        setBackground(BLANCO);
        setFocusable(true);
        agregarEventosTeclado();
        reiniciarJuego();
    }

    private void agregarEventosTeclado() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (estadoGanado || estadoPerdido) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        reiniciarJuego();
                    }
                    return;
                }
                boolean moved = false;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> moved = moverIzquierda();
                    case KeyEvent.VK_RIGHT -> moved = moverDerecha();
                    case KeyEvent.VK_UP -> moved = moverArriba();
                    case KeyEvent.VK_DOWN -> moved = moverAbajo();
                }
                if (moved) {
                    generarNumero();
                    estadoGanado = comprobarGanar();
                    estadoPerdido = comprobarPerder();
                }
                repaint();
            }
        });
    }

    private void reiniciarJuego() {
        tablero = new int[4][4];
        estadoPerdido = false;
        estadoGanado = false;
        score=0;
        generarNumero();
        generarNumero();
        repaint();
    }

    private void actualizarPuntajes() {

        bestScore = regresarMayor(bestScore, score);  // MASMx86
        scoreLabel.setText(" Score: " + score);
        bestScoreLabel.setText("Best Score: " + bestScore);
    }


    private void generarNumero() {
        ArrayList<int[]> vacios = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tablero[i][j] == 0) {
                    vacios.add(new int[]{i, j});
                }
            }
        }

        if (!vacios.isEmpty()) {
            int[] pos = vacios.get(new Random().nextInt(vacios.size()));
            tablero[pos[0]][pos[1]] = numAleatorio248(); // MASMx86

        }
    }

    private boolean comprobarGanar() {
        for (int[] fila : tablero) {
            for (int num : fila) {
                if (comprobarSiGana(num,NUMWIN)) {// MASMx86
                    return true;
                }
            }
        }
        return false;
    }

    private boolean comprobarPerder() {
        for (int[] fila : tablero) {
            for (int num : fila) {
                if (comprobarSiPierde(num)) {// MASMx86
                    return false;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == tablero[i][j + 1] || tablero[j][i] == tablero[j + 1][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean moverIzquierda() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] fila = tablero[i];
            int[] original = fila.clone();
            moverNumeros(fila);
            combinarNumeros(fila);//MASMx86
            moverNumeros(fila);
            if (!Arrays.equals(original, fila)) moved = true;
        }
        return moved;
    }

    private boolean moverDerecha() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] fila = tablero[i];
            reverse(fila);
            int[] original = fila.clone();
            moverNumeros(fila);
            combinarNumeros(fila);//MASMx86
            moverNumeros(fila);
            reverse(fila);
            if (!Arrays.equals(original, fila)) moved = true;
        }
        return moved;
    }

    private boolean moverArriba() {
        boolean moved = false;
        for (int j = 0; j < 4; j++) {
            int[] columna = new int[4];
            for (int i = 0; i < 4; i++) columna[i] = tablero[i][j];
            int[] original = columna.clone();
            moverNumeros(columna);
            combinarNumeros(columna); //MASMx86
            moverNumeros(columna);
            for (int i = 0; i < 4; i++) tablero[i][j] = columna[i];
            if (!Arrays.equals(original, columna)) moved = true;
        }
        return moved;
    }

    private boolean moverAbajo() {
        boolean moved = false;
        for (int j = 0; j < 4; j++) {
            int[] columna = new int[4];
            for (int i = 0; i < 4; i++) columna[i] = tablero[i][j];
            reverse(columna);
            int[] original = columna.clone();
            moverNumeros(columna);
            combinarNumeros(columna);//MASMx86
            moverNumeros(columna);
            reverse(columna);
            for (int i = 0; i < 4; i++) tablero[i][j] = columna[i];
            if (!Arrays.equals(original, columna)) moved = true;
        }
        return moved;
    }

    private void moverNumeros(int[] nums) {
        // Crear una lista para almacenar los números diferentes de cero
        ArrayList<Integer> noEsZero = new ArrayList<>();

        // Agregar todos los números diferentes de cero a la lista
        for (int num : nums) {
            if (compararSiEs0(num)) { // MASMx86
                noEsZero.add(num);
            }
        }

        // Actualizar el arreglo original: primero los números diferentes de cero
        int i = 0;
        for (; i < noEsZero.size(); i++) {
            nums[i] = noEsZero.get(i);
        }

        // Rellenar el resto del arreglo con ceros
        for (; i < nums.length; i++) {
            nums[i] = 0;
        }
    }


    private void combinarNumeros(int[] nums) {
        for (int i = 0; i < 3; i++) {
            if (nums[i] == nums[i + 1] && nums[i] != 0) {
                nums[i] = sumarNumeros(nums[i], nums[i]); // MASMx86
                score = incrementarScore(score, nums[i]); // MASMx86
                nums[i + 1] = 0;
            }
        }
    }

    private void reverse(int[] array) {
        int izquierda = 0, derecha = array.length - 1;
        while (izquierda < derecha) {
            int temp = array[izquierda];
            array[izquierda] = array[derecha];
            array[derecha] = temp;
            izquierda++;
            derecha--;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int value = tablero[i][j];
                Color color = COLORES[Math.min(value, COLORES.length - 1)];
                g2.setColor(color);
                g2.fillRect(j * CELDASTAMANO, i * CELDASTAMANO, CELDASTAMANO, CELDASTAMANO);
                if (value != 0) {
                    g2.setColor(NEGRO);
                    g2.drawString(String.valueOf(value), j * CELDASTAMANO + CELDASTAMANO / 2, i * CELDASTAMANO + CELDASTAMANO / 2);
                }
            }
        }
        actualizarPuntajes();

        if (estadoGanado) {
            Font font = new Font("Arial",Font.BOLD,30);
            g2.setFont(font);
            g2.setColor(Color.GREEN);
            g2.drawString("¡Has Ganado!", ANCHO / 2 - 100, ALTURA/2);
            g2.drawString("Presiona R para jugar nuevamente", 2, ALTURA/2 +30);
        } else if (estadoPerdido) {
            Font font = new Font("Arial",Font.BOLD,30);
            g2.setFont(font);
            g2.setColor(Color.RED);
            g2.drawString("¡Perdiste!", ANCHO / 2 - 75, ALTURA/2);
            g2.drawString("Presiona R para jugar nuevamente", 2, ALTURA/2+30);
        }
    }

    public static void main(String[] args) {
        JLabel scoreLabel = new JLabel("Score: 0");
        JLabel bestScoreLabel = new JLabel("Best Score: 0");

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        bestScoreLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new GridLayout(1, 2));
        scoresPanel.add(scoreLabel);
        scoresPanel.add(bestScoreLabel);

        MiClase juego = new MiClase(scoreLabel, bestScoreLabel);

        JFrame frame = new JFrame("MiJuego2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scoresPanel, BorderLayout.NORTH);
        frame.add(juego, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
