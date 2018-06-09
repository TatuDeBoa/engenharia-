package engenharia;

import java.util.ArrayList;

/**
 * @author bruno de lima santos (tatu_de_boa)
 * Classe estática que é utilizada para procedimentos numéricos comuns
 */
public class Calculo {
    
    public static ArrayList<ArrayList<Double>> mtztranspor(ArrayList<ArrayList<Double>> matriz) {
        ArrayList<ArrayList<Double>> mtz = new ArrayList<>();
        int numeroLinhas = matriz.size();
        int numeroColunas = matriz.get(0).size();
        for(int i = 0; i < numeroColunas; i++) {
            mtz.add(new ArrayList<>());
            for(int j = 0; j < numeroLinhas; j++) {
                mtz.get(i).add(matriz.get(j).get(i));
            }
        }
        return mtz;
    }
    
    public static ArrayList<ArrayList<Double>> mtzinverter(ArrayList<ArrayList<Double>> matriz) {
        ArrayList<ArrayList<Double>> mtz = new ArrayList<>();
        ArrayList<ArrayList<Double>> identidade = new ArrayList<>();
        int numeroLinhas = matriz.size();
        int numeroColunas = matriz.get(0).size();
        // Verifica se é uma matriz quadrada
        if(numeroLinhas == numeroColunas) {  
            for(int i = 0; i < numeroLinhas; i++) {
                identidade.add(new ArrayList<>());
                mtz.add(new ArrayList<>());
                for(int j = 0; j < numeroLinhas; j++) {
                    if(i == j) {
                        identidade.get(i).add(1.0);
                    } else {
                        identidade.get(i).add(0.0);
                    }
                    mtz.get(i).add(matriz.get(i).get(j));
                }
            }
            // Inicia a inversão pela eliminação de Gauss-Jordan
            for(int i = 0; i < numeroLinhas; i++) {
                double pivot = mtz.get(i).get(i);
                for(int j = 0; j < numeroLinhas; j++) {
                    mtz.get(i).set(j, mtz.get(i).get(j) / pivot);
                    identidade.get(i).set(j, identidade.get(i).get(j) / pivot);
                }
                for(int j = 0; j < numeroLinhas; j++) {
                    if(j != i) {
                        double escalar = mtz.get(j).get(i);
                        for(int k = 0; k < numeroLinhas; k++) {
                            mtz.get(j).set(k, mtz.get(j).get(k) - escalar * mtz.get(i).get(k));
                            identidade.get(j).set(k, identidade.get(j).get(k) - escalar * identidade.get(i).get(k));
                        }
                    }
                }
            }
        }
        return identidade;
    }
    
    public static ArrayList<ArrayList<Double>> mtzmulti(ArrayList<ArrayList<Double>> matriz1, ArrayList<ArrayList<Double>> matriz2) {
        ArrayList<ArrayList<Double>> mtz = new ArrayList<>();
        if(matriz1.get(0).size() == matriz2.size()) {
            int numeroLinhas = matriz1.size();
            int numeroColunas = matriz2.get(0).size();
            int numeroParcelas = matriz1.get(0).size();
            for(int i = 0; i < numeroLinhas; i++) {
                mtz.add(new ArrayList<>());
                for(int j = 0; j < numeroColunas; j++) {
                    double soma = 0;
                    for(int k = 0; k < numeroParcelas; k++) {
                        soma += matriz1.get(i).get(k) * matriz2.get(k).get(j);
                    }
                    mtz.get(i).add(soma);
                }
            }
        }
        return mtz;
    }
    
}
