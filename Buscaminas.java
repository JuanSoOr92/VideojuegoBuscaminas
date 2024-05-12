import java.util.Random;
import java.util.Scanner;

public class Buscaminas {
    static Scanner sc = new Scanner(System.in);
    final static char SIMBOLO_MINA = '*';
    static int ALTO_TABLERO;
    static int ANCHO_TABLERO;
    static int NUM_MINAS;

    public static void main(String[] args) {
        mostrarMenuBienvenida();
    }

    public static void imprimirTablero(char[][] tablero){
        // Imprime los bordes superiores
        System.out.print("   +");
        for (int col = 1; col <= tablero[0].length; col++) {
            System.out.print("--");
        }
        System.out.println("+");

        // Imprime el tablero con bordes y etiquetas de fila y columna
        for (int fila = 0; fila < tablero.length; fila++) {
            System.out.print((fila + 1) + " | ");
            for (int col = 0; col < tablero[0].length; col++) {
                System.out.print(tablero[fila][col] + " ");
            }
            System.out.println("|");
        }

        // Imprime el borde inferior
        System.out.print("   +");
        for (int col = 1; col <= tablero[0].length; col++) {
            System.out.print("--");
        }
        System.out.println("+");

        // Imprime las etiquetas de columna
        System.out.print("    ");
        for (int col = 1; col <= tablero[0].length; col++) {
            System.out.print(col + " ");
        }
        System.out.println();
    }

    public static void inicializarTablero(char[][] tablero){
        for(int i = 0; i < tablero.length; i++){
            for(int j = 0; j < tablero[0].length; j++){
                tablero[i][j] = '-';
            }
        }
    }

    public static void inicializarMascara(char[][] mascara){
        for(int i = 0; i < mascara.length; i++){
            for(int j = 0; j < mascara[0].length; j++){
                mascara[i][j] = '#';
            }
        }
    }

    public static void colocarMinas(char[][] tablero, int numeroMinas){
        Random rng = new Random();

        do{
            int fila = rng.nextInt( tablero.length);
            int columna = rng.nextInt( tablero.length);
            if(tablero[fila][columna] == '-') {
                tablero[fila][columna] = SIMBOLO_MINA;
                numeroMinas--;
            }
        }while(numeroMinas > 0);
    }

    public static void colocarNumeracion(char[][] tablero){
        for(int i = 0; i < tablero.length; i++){
            for(int j = 0; j < tablero[0].length; j++){
                if(tablero[i][j] != SIMBOLO_MINA) tablero[i][j] = enteroACaracter(contarMinasCercanas(tablero,i,j));
            }
        }
    }

    public static int contarMinasCercanas(char[][] tablero, int fila, int columna){
        int cont = 0;
        for(int i = Math.max(0, fila - 1); i <= Math.min(tablero.length - 1, fila + 1); i++){
            for(int j = Math.max(0, columna - 1); j <= Math.min(tablero[0].length - 1, columna + 1); j ++){
                if(tablero[i][j] == SIMBOLO_MINA) cont++;
            }
        }
        return cont;
    }

    public static void imprimirEstadisticas(char[][] tablero, char[][] mascara, int minas) {
        int filas = tablero.length;
        int columnas = tablero[0].length;
        int celdasDespejadas = contarCeldasDespejadas(mascara);

        System.out.println("-$ ===== Estadísticas de la Partida =====");
        System.out.println("-$ Minas en el tablero: " + minas);
        System.out.println("-$ Celdas despejadas: " + celdasDespejadas);
        System.out.println("-$ Celdas restantes: " + (filas * columnas - celdasDespejadas));
        System.out.println("-$ ======================================");

    }

    public static int contarCeldasDespejadas(char[][] mascara) {
        int contador = 0;
        for (int i = 0; i < mascara.length; i++) {
            for (int j = 0; j < mascara[0].length; j++) {
                if (mascara[i][j] != '#') {
                    contador++;
                }
            }
        }
        return contador;
    }


    public static char enteroACaracter(int entero){
        char caracter;
        switch (entero) {
            case 0 :return caracter = '0';
            case 1 :return caracter = '1';
            case 2 :return caracter = '2';
            case 3 :return caracter = '3';
            case 4 :return caracter = '4';
            case 5 :return caracter = '5';
            case 6 :return caracter = '6';
            case 7 :return caracter = '7';
            case 8 :return caracter = '8';
            case 9 :return caracter = '9';
            default :return caracter = '!';
        }
    }

    public static void desenmascararCeldas(char[][] tablero, char[][] mascara, int fila, int columna){
        if(tablero[fila][columna] == '*') mascara[fila][columna] = '*';
        else if(tablero[fila][columna] >= '1' && tablero[fila][columna] <= '9') mascara[fila][columna] =tablero[fila][columna];
        else desenmascararCeldasVacias(tablero, mascara, fila, columna);
    }

    public static void desenmascararCeldasVacias(char[][] tablero, char[][] mascara, int fila, int columna) {
        if (tablero[fila][columna] == '0' && mascara[fila][columna] == '#') {
            // Desenmascaramos la celda actual
            mascara[fila][columna] = '-';

            // Actualizamos los números en los bordes de la zona vacía
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nuevaFila = fila + i;
                    int nuevaColumna = columna + j;

                    if (nuevaFila >= 0 && nuevaFila < tablero.length && nuevaColumna >= 0 && nuevaColumna < tablero[0].length) {
                        if (tablero[nuevaFila][nuevaColumna] != '0') {
                            mascara[nuevaFila][nuevaColumna] = enteroACaracter(contarMinasCercanas(tablero, nuevaFila, nuevaColumna));
                        } else {
                            // Llamamos recursivamente a la función para desenmascarar las celdas vacías adyacentes
                            desenmascararCeldasVacias(tablero, mascara, nuevaFila, nuevaColumna);
                        }
                    }
                }
            }
        }
    }

    public static void establecerDificultad(){
        boolean entradaIncorrecta = false;
        String dificultad = "";
        do{
            System.out.println("-$ ¿Con qué dificultad quieres jugar?");
            System.out.println("-$ 1. Nivel Fácil");
            System.out.println("-$ 2. Nivel Medio");
            System.out.println("-$ 3. Nivel Difícil");
            System.out.print("-$ 4. Nivel Experto \n--->");
            int opcion = sc.nextInt();

            switch(opcion){
                case 1: ANCHO_TABLERO = 5; ALTO_TABLERO = 5; NUM_MINAS = 4; dificultad = "Fácil"; break;
                case 2: ANCHO_TABLERO = 7; ALTO_TABLERO = 7; NUM_MINAS = 15; dificultad = "Medio";break;
                case 3: ANCHO_TABLERO = 10; ALTO_TABLERO = 10; NUM_MINAS = 30; dificultad = "Difícil";break;
                case 4: ANCHO_TABLERO = 15; ALTO_TABLERO = 15; NUM_MINAS =60; dificultad = "Experto";break;
                default:
                    System.out.println("-$ ¡ATENCIÓN! Opción no válida. Inténtelo de nuevo.");
                    entradaIncorrecta = true;
            }
        } while(entradaIncorrecta);

        System.out.println("-$ La dificultad seleccionada es: " + dificultad);
    }

    public static void mostrarMenuBienvenida() {
        System.out.println("-$ ¡Bienvenido al Buscaminas!");
        int opcion;

        do {
            System.out.println("-$ ===== Menú Principal =====");
            System.out.println("-$ 1. Jugar");
            System.out.println("-$ 2. Reglas del Buscaminas");
            System.out.println("-$ 3. Salir");
            System.out.print("-$ Seleccione una opción: \n---> ");

            opcion = obtenerEntradaUsuario();

            switch (opcion) {
                case 1:
                    establecerDificultad();
                    juego();
                    break;
                case 2:
                    imprimirReglasBuscaminas();
                    break;
                case 3:
                    System.out.println("-$ ¡Gracias por jugar! Hasta luego.");
                    break;
                default:
                    System.out.println("-$ ¡ATENCIÓN! Opción no válida. Inténtelo de nuevo.");
                    break;
            }
        } while (opcion != 3);
    }

    public static void imprimirReglasBuscaminas() {
        System.out.println("-$ ===== Reglas del Buscaminas =====");
        System.out.println("-$ El objetivo del juego es descubrir todas las casillas sin minas.");
        System.out.println("-$ Cada casilla puede contener una mina o un número que indica cuántas minas hay alrededor.");
        System.out.println("-$ Puedes desenmascarar una casilla escribiendo sus coordenadas (fila y columna).");
        System.out.println("-$ Si desenmascaras una casilla con una mina, pierdes el juego.");
        System.out.println("-$ Si desenmascaras todas las casillas sin minas, ganas el juego.");
        System.out.println("-$ ¡Buena suerte!\n");
    }

    public static int obtenerEntradaUsuario() {
        while (!sc.hasNextInt()) {
            System.out.println("-$ ¡ATENCIÓN! Entrada no válida. Introduce un número.");
            sc.next(); // Consumir la entrada no válida
        }
        return sc.nextInt();
    }

    public static void juego() {

        char[][] tablero = new char[ALTO_TABLERO][ANCHO_TABLERO];
        char[][] mascara = new char[ALTO_TABLERO][ANCHO_TABLERO];
        inicializarTablero(tablero);
        inicializarMascara(mascara);
        colocarMinas(tablero, NUM_MINAS);
        colocarNumeracion(tablero);

        boolean juegoTerminado;

        imprimirEstadisticas(tablero, mascara, NUM_MINAS);
        imprimirTablero(mascara);

        do {


            // Solicitud de entrada para fila y columna con control de errores
            int fila, columna;
            do {
                System.out.print("-$ Introduce la fila: \n---> ");
                fila = obtenerEntradaUsuario();
                System.out.print("-$ Introduce la columna: \n---> ");
                columna = obtenerEntradaUsuario();

                if (esCoordenadaValida(fila, columna, ALTO_TABLERO, ANCHO_TABLERO)) {
                    System.out.println("Coordenadas inválidas. Inténtalo de nuevo.");
                }
            } while (esCoordenadaValida(fila, columna, ALTO_TABLERO, ANCHO_TABLERO));

            // Desenmascarar celdas y actualizar el juego
            desenmascararCeldas(tablero, mascara, fila - 1, columna - 1);
            imprimirEstadisticas(tablero, mascara, NUM_MINAS);
            imprimirTablero(mascara);

            /* esta línea sirve para ver donde se encuentran los numeros y las minas del tablero (se puede utilizar para "hacer 
            trampas" y ganar la partida de una manera más rápida ya que te indica dónde se encuentran las minas del tablero y así ver que el juego funciona sin necesidad
            de jugar una partida entera)*/
            
            //imprimirTablero(tablero); 
            

            juegoTerminado = verificarFinDeJuego(tablero, mascara, fila - 1, columna - 1);

        } while (!juegoTerminado);

    }

    public static boolean esCoordenadaValida(int fila, int columna, int alto, int ancho) {
        return fila < 1 || fila > alto || columna < 1 || columna > ancho;
    }

    public static boolean verificarFinDeJuego(char[][] tablero, char[][] mascara, int fila, int columna) {
        boolean hitMina = false;
        boolean todasDespejadas = true;

        if(tablero[fila][columna] == SIMBOLO_MINA) {
            System.out.println("-$ ¡Has golpeado una mina! ¡Fin del juego!");
            hitMina = true;
        }
        else todasDespejadas = (ANCHO_TABLERO * ALTO_TABLERO - contarCeldasDespejadas(mascara)) == NUM_MINAS;

        // Mostrar mensaje de victoria si no hay mina golpeada y todas las casillas están despejadas
        if (!hitMina && todasDespejadas) {
            System.out.println("-$ ¡Felicidades! ¡Has ganado!");
        }

        // Devolver verdadero si hay una mina golpeada o todas las casillas están despejadas
        return hitMina || todasDespejadas;
    }
}