import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MiClase extends JPanel {
    // CONSTANTES
    private static final int ANCHO = 400;
    private static final int ALTURA = 400;
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


    public native boolean comprobarSiPierde(int a);
    public native boolean comprobarSiGana(int a, int b);
    public native int sumarNumeros(int a, int b);
    public native void regresarTexto();
    public native int numAleatorio248();
    public native int numAleatorio04();
   // public native void moverNumeros(int[] nums);    // Cargar la biblioteca nativa
    static {
        System.loadLibrary("MiBiblioteca");
    }

    public MiClase() {
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
        generarNumero();
        generarNumero();
        repaint();
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
                if (comprobarSiGana(num,NUMWIN)) return true; // MASMx86
            }
        }
        return false;
    }

    private boolean comprobarPerder() {
        for (int[] fila : tablero) {
            for (int num : fila) {
                if (comprobarSiPierde(num)) return false; // MASMx86
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
            if (num != 0) {
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



        if (estadoGanado) {
            g2.setColor(VERDE);
            g2.drawString("¡Has Ganado!", ANCHO / 2 - 50, ALTURA / 2);
        } else if (estadoPerdido) {
            g2.setColor(ROJO);
            g2.drawString("¡Perdiste!", ANCHO / 2 - 50, ALTURA / 2);
        }
    }

    public static void main(String[] args) {
        MiClase conectar = new MiClase();
        conectar.regresarTexto();
        JFrame frame = new JFrame("MiJuego2048");
        MiClase juego = new MiClase();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(juego);
        frame.pack();
        frame.setVisible(true);
    }
}
