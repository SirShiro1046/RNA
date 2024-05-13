package org.examen.rnaex.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Rna {

    private ArrayList<int[]> datos;
    private int[][] c;
    private float alfa;
    private int b;
    private float[][] w;
    private int epocas;
    private ArrayList<Integer> fDatos;
    private String datosTest;
    private String datosTrain;
    private String pesosFinales;

    private int[][] d; // Cambio en el tipo de datos
    private int[][] y; // Cambio en el tipo de datos

    public Rna(ArrayList<int[]> datos, float alfa, int b, int epocas) {
        this.datos = datos;
        this.c = generateMatrix();
        this.alfa = alfa;
        this.b = b;
        this.w = pesosRandoms();
        this.epocas = epocas;
        this.fDatos = new ArrayList<>();
    }

    public int[] fnet(int[] fDatos) {
        int[] resultado = new int[7]; // Vector para almacenar los resultados

        // Iterar sobre cada fila de la matriz de pesos w
        for (int i = 0; i < 7; i++) {
            float sum = 0;
            // Iterar sobre cada posición en la fila de la matriz w
            for (int j = 0; j < 64; j++) {
                // Si es la posición 64, multiplicar por b
                if (j == 63) {
                    sum += w[i][j] * b;
                } else {
                    sum += w[i][j] * fDatos[j];
                }
            }
            // Pasar el resultado por la función de activación
            if (sum > 0) {
                resultado[i] = 1;
            } else {
                resultado[i] = -1;
            }
        }
        return resultado;
    }

    public String ejecucionEpocas() {
        int aciertos = 0;
        for (int i = 0; i < epocas; i++) {
            datosTrain = "";
            System.out.println("\nEPOCA " + (i + 1));
          //  datosTrain+= "\nEPOCA " + (i + 1);
            for (int j = 0; j < datos.size(); j++) {
                System.out.println("\nPATRON " + (j + 1));
               // datosTrain+= "\nPATRON " + (j + 1)+"\n";
                fDatos.clear(); // Limpiamos fDatos antes de agregar nuevos datos
                for (int k = 0; k < datos.get(j).length; k++) {
                    fDatos.add(datos.get(j)[k]); // Agregamos los datos de la fila actual a fDatos
                }
                y = new int[][]{fnet(fDatos.stream().mapToInt(Integer::intValue).toArray())}; // Convertir ArrayList a int[]
                System.out.println("Valor aprendido: " + Arrays.deepToString(y)+"\n");
               // datosTrain+= "Valor aprendido: " + Arrays.deepToString(y);
                d = new int[][]{c[j]}; // Obtenemos el valor esperado
                System.out.println("Valor que debio aprender: " + Arrays.deepToString(d));
              //  datosTrain+= "Valor que debio aprender: " + Arrays.deepToString(d)+"\n";
                if (Arrays.deepEquals(d, y)) {
                    aciertos++;
                }
                updateW(fDatos, d, y); // Actualizamos los pesos
            }
            System.out.println("Precisión: " + calcularPrecision(aciertos) + "%");
            datosTrain+= "Precisión: " + calcularPrecision(aciertos) + "%";
            aciertos = 0;
        }
        return datosTrain+"\n"+pesosFinales;
    }


    private void updateW(ArrayList<Integer> fDatos, int[][] d, int[][] y) {
        pesosFinales="";
        System.out.println("Pesos Actuales");
        for (int i = 0; i < w.length; i++) {
            System.out.println("w[" + i + "] = " + Arrays.toString(w[i])); // Imprimimos los pesos actuales
        }
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[i].length; j++) {
                if (j < w[i].length - 1) {
                    w[i][j] += alfa * fDatos.get(j) * (d[0][i] - y[0][i]); // Actualizamos los pesos
                } else {
                    w[i][j] += alfa * b * (d[0][i] - y[0][i]); // Actualizamos los pesos
                }
            }
        }
        System.out.println("Pesos Actualizados");
        for (int i = 0; i < w.length; i++) {
            System.out.println("w[" + i + "] = " + Arrays.toString(w[i])); // Imprimimos los pesos actualizados
            pesosFinales+="w[" + i + "] = " + Arrays.toString(w[i])+"\n";
        }
    }

    private double calcularPrecision(int valor) {
        return (valor / (double) datos.size()) * 100; // Calculamos la precisión como un porcentaje
    }

    public static float[][] pesosRandoms() {
        float[][] pesos = new float[7][64];
        Random rand = new Random();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 64; j++) {
                pesos[i][j] = rand.nextFloat(); // Generar valores aleatorios entre 0 y 1
            }
        }
        return pesos;
    }

    public int[][] generateMatrix() {
        int[][] matriz = new int[21][7];

        // Llenar la matriz con 0s en todas partes
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 7; j++) {
                matriz[i][j] = -1;
            }
        }

        // Colocar un 1 que se mueve hacia la derecha en cada fila
        for (int i = 0; i < 21; i++) {
            matriz[i][i % 7] = 1;
        }

        return matriz;
    }

    public void setDatos(ArrayList<int[]> datos) {
        this.datos = datos;
    }

    public String ejecucionEpocasTest() {
        int aciertos = 0;
        for (int j = 0; j < datos.size(); j++) {
            System.out.println("\nPATRON " + (j + 1));
            fDatos.clear(); // Limpiamos fDatos antes de agregar nuevos datos
            for (int k = 0; k < datos.get(j).length; k++) {
                fDatos.add(datos.get(j)[k]); // Agregamos los datos de la fila actual a fDatos
            }
            y = new int[][]{fnet(fDatos.stream().mapToInt(Integer::intValue).toArray())}; // Convertir ArrayList a int[]
            System.out.println("Valor aprendido: " + Arrays.deepToString(y));
            d = new int[][]{c[j]}; // Obtenemos el valor esperado
            System.out.println("Valor que debio aprender: " + Arrays.deepToString(d));
            if (Arrays.deepEquals(d, y)) {
                System.out.println("El patrón " + (j + 1) + " fue aprendido correctamente.");
                aciertos++;
            } else {
                System.out.println("El patrón " + (j + 1) + " no fue aprendido correctamente.");
            }
        }
        double precision = (aciertos / (double) datos.size()) * 100;

        System.out.println("Precisión: " + precision + "%");
        datosTest="Prediccion: " + precision + "%";
        return datosTest;
    }

}
