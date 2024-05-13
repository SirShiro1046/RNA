package org.examen.rnaex.configure;


import lombok.extern.log4j.Log4j2;
import org.examen.rnaex.entities.Rna;
import org.examen.rnaex.entities.TrainingData;
import org.examen.rnaex.readerFile.ReaderFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@Log4j2
public class ApiController {
    private Rna rna;

    @PostMapping("/train")
    public String iniciarEntrenamiento(@RequestBody TrainingData trainingData) {
        System.out.println(trainingData.getData());
        String resultado;
        ArrayList<int[]> listaVectores = matrizParaEnviar(trainingData.getData());

        rna = new Rna(listaVectores, trainingData.getAlfa(), trainingData.getBeta(), trainingData.getEpocas());
       resultado= rna.ejecucionEpocas();

        log.info("Proceso de entrenamiento iniciado");
        return resultado;
    }

    @PostMapping("/test")
    public String iniciarTest(@RequestBody TrainingData trainingData) {

        System.out.println(trainingData.getData());
        ArrayList<int[]> listaVectores = matrizParaEnviar(trainingData.getData());
        rna.setDatos(listaVectores);
        return rna.ejecucionEpocasTest();
    }


    public static ArrayList<int[]> matrizParaEnviar(String filePath) {
        ArrayList<int[]> listaVectores = new ArrayList<>();
        int cont = 0;

        ReaderFile file = new ReaderFile(filePath);
        String[][] matriz = Mapping.convertMatrix(file.readFile());
        String[] vector = new String[63];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                vector[cont] = matriz[i][j];
                cont++;

                if (cont == 63) {
                    int[] intVector = new int[63];
                    for (int k = 0; k < vector.length; k++) {
                        intVector[k] = Integer.parseInt(vector[k]);
                    }
                    listaVectores.add(intVector);
                    vector = new String[63];
                    cont = 0;
                }
            }
        }
        return listaVectores;
    }

}





