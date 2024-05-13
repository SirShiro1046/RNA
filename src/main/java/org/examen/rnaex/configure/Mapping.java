package org.examen.rnaex.configure;

public class Mapping {

    public static String[][] convertMatrix(String[][] matriz){
        String[][] matrizModificada = new String[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                switch (matriz[i][j]) {
                    case "#":
                    case "@":
                        matrizModificada[i][j] = "1";
                        break;
                    case ".":
                    case "o":
                        matrizModificada[i][j] = "-1";
                        break;
                    // Puedes agregar más casos según sea necesario
                    default:
                        // Mantener el mismo carácter si no coincide con las reglas
                        matrizModificada[i][j] = matriz[i][j];
                        break;
                }
            }
        }
        return matrizModificada;
    }
}
