package org.examen.rnaex.readerFile;

import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;

public class ReaderFile {
    private String fileContent;

    public ReaderFile(String fileContent) {
        this.fileContent = fileContent;
    }

    public String[][] readFile() {
        try (BufferedReader br = new BufferedReader(new StringReader(fileContent))) {
            // Determina el tamaño de la matriz
            int numRows = 0;
            int numCols = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                numRows++;
                if (numCols == 0) {
                    numCols = linea.length();
                }
            }

            // Crea la matriz
            String[][] matriz = new String[numRows][numCols];

            // Vuelve a leer el archivo para llenar la matriz
            try (BufferedReader br2 = new BufferedReader(new StringReader(fileContent))) {
                int fila = 0;
                while ((linea = br2.readLine()) != null) {
                    char[] filaChars = linea.toCharArray();
                    for (int i = 0; i < filaChars.length; i++) {
                        matriz[fila][i] = String.valueOf(filaChars[i]);
                    }
                    fila++;
                }
            }

            return matriz;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
