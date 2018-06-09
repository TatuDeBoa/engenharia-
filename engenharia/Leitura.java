package engenharia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author bruno de lima santos (tatu_de_boa)
 * Classe para leitura de arquivos externos, armazenando os valores em ArrayList<ArrayList<String>>
 */
public class Leitura {
    
    private String localizacao;
    private String divisor = ",";
    
    public void setLocalizacao(String local) {
        this.localizacao = local;
    }
    
    public void setDivisor(String divisor) {
        this.divisor = divisor;
    }
    
    public ArrayList ler() {
        ArrayList<ArrayList<String>> resultado = new ArrayList<>();
        BufferedReader buffer;
        try {
            String linha;
            buffer = new BufferedReader(new FileReader(this.localizacao));
            while((linha = buffer.readLine()) != null) {
                resultado.add(new ArrayList<>());
                String[] leitura = linha.split(divisor);
                int indc = resultado.size() - 1;
                resultado.get(indc).addAll(Arrays.asList(leitura));
            }
        } finally {
            return resultado;
        }
    }
    
}
