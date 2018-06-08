
import java.util.ArrayList;

/**
 * @author bruno de lima santos (tatu_de_boa)
 * Classe utilizada para realizar regressões linear simples e linear múltipla
 */
public class Regressor {
    
    // Atributos necessários
    private static ArrayList<Double> dadosX;
    private static ArrayList<Double> dadosY;
    private static ArrayList<ArrayList<Double>> dadosXi;
    private static ArrayList<Double> residuos = new ArrayList<>();
    private static ArrayList<Double> estimadores = new ArrayList<>();
    private static ArrayList<Double> dpEstimadores = new ArrayList<>();
    private static double somaQuadRes;
    private static double somaQuadReg;
    private static double coefDet;
    private static double variancia;
    private static boolean exibir = true;
    
    // Métodos de acesso (get)
    public ArrayList<Double> getResiduos() { return residuos; }
    
    public ArrayList<Double> getEstimadores() { return estimadores; }
    
    public ArrayList<Double> getDesvio() { return dpEstimadores; }
    
    public double getSQD() { return somaQuadRes; }
    
    public double getSQR() { return somaQuadReg; }
    
    public double getR2() { return coefDet; }
    
    public double getVar() { return variancia; }
    
    // Método de acesso e modificação de atributo
    public void setLog(boolean log) { exibir = log; }
    
    public void reglin(ArrayList<Double> x, ArrayList<Double> y) {
        // Atualiza a referência da classe com os valores novos para regressão
        dadosX = x;
        dadosY = y;
        // Executa o cálculo dos dois parâmetros
        double carl = spd(dadosX, dadosY) / sqd(dadosX);
        double clrl = media(dadosY) - carl * media(dadosX);
        // Atualiza o ArrayList com os estimadores;
        estimadores.clear();
        estimadores.add(clrl);
        estimadores.add(carl);
        // Calcula alguns parâmetros estatísticos gerais
        // Soma de quadrados dos resíduos e soma de quadrados da regressão
        double mediaY = media(dadosY);
        residuos.clear();
        somaQuadRes = 0;
        somaQuadReg = 0;
        for(int i = 0; i < dadosY.size(); i++) {
            double yest = clrl + carl * dadosX.get(i);
            residuos.add(dadosY.get(i) - yest);
            somaQuadRes += Math.pow(residuos.get(i), 2);
            somaQuadReg += Math.pow(yest - mediaY, 2);
        }
        // Cálculo do coeficiente de determinação
        coefDet = somaQuadReg / (somaQuadReg + somaQuadRes);
        // Cálculo da variância
        variancia = somaQuadRes / (dadosY.size() - 2);
        // Calcula o erro padrão dos estimadores
        dpEstimadores.clear();
        dpEstimadores.add(Math.sqrt(variancia * (1 / dadosY.size() + Math.pow(media(dadosX), 2) / sqd(dadosX))));
        dpEstimadores.add(Math.sqrt(variancia / sqd(dadosX)));
        // A linha seguinte só exibe na tela os resultados obtidos
        if(exibir) exibirLog();
    }
    
    public void reglm(ArrayList<ArrayList<Double>> xi, ArrayList<Double> y) {
        int numObs = xi.get(0).size();
        ArrayList<ArrayList<Double>> matrizX = new ArrayList<>();
        ArrayList<ArrayList<Double>> matrizY = new ArrayList<>();
        dadosY = y;
        // Monta a matriz utilizada para regressão não linear
        for(int i = 0; i < numObs; i++) {
            matrizX.add(new ArrayList<>());
            matrizY.add(new ArrayList<>());
            matrizX.get(i).add(1.0);
            matrizY.get(i).add(y.get(i));
            for(int j = 0; j < xi.size(); j++) {
                matrizX.get(i).add(xi.get(j).get(i));
            }
        }
        // Cálculo dos estimadores
        ArrayList<ArrayList<Double>> xtransposta = Calculo.mtztranspor(matrizX);
        ArrayList<ArrayList<Double>> xinversa = Calculo.mtzinverter(Calculo.mtzmulti(xtransposta, matrizX));
        ArrayList<ArrayList<Double>> resultado = Calculo.mtzmulti(Calculo.mtzmulti(xinversa, xtransposta), matrizY);
        estimadores.clear();
        for(int i = 0; i < resultado.size(); i++) {
            estimadores.add(resultado.get(i).get(0));
        }
        // Cálculo da soma dos quadrados da regressão e dos resíduos
        double media = media(y);
        somaQuadRes = 0;
        somaQuadReg = 0;
        residuos.clear();
        for(int i = 0; i < y.size(); i++) {
            double yest = 0;
            for(int j = 0; j < matrizX.get(i).size(); j++) {
                yest += matrizX.get(i).get(j) * estimadores.get(j);
            }
            residuos.add(y.get(i) - yest);
            somaQuadRes += Math.pow(residuos.get(i), 2);
            somaQuadReg += Math.pow(yest - media, 2);
        }
        // Cálculo da variância
        variancia = somaQuadRes / (y.size() - matrizX.get(0).size());
        // Cálculo do erro padrão dos estimadores
        dpEstimadores.clear();
        for(int i = 0; i < estimadores.size(); i++) {
            dpEstimadores.add(Math.sqrt(variancia * xinversa.get(i).get(i)));
        }
        // Cálculo do coeficiente de determinação
        coefDet = somaQuadReg / (somaQuadReg + somaQuadRes);
        // Exibição dos resultados
        if(exibir) exibirLog();
    }
    
    public void exibirLog() {
        System.out.println("----------------------------------");
        System.out.println("A regressão foi efetuada com sucesso, os dados estão abaixo:");
        System.out.println("Valor médio das observações y: " + media(dadosY));
        for(int i = 0; i < estimadores.size(); i++) {
            System.out.println("Parâmetro a" + i + ": " + estimadores.get(i) + " +/- " + dpEstimadores.get(i));
        }
        System.out.println("Variância da regressão: " + variancia);
        System.out.println("Soma dos quadrados dos resíduos: " + somaQuadRes);
        System.out.println("Soma dos quadrados da regressão: " + somaQuadReg);
        System.out.println("Coeficiente de determinação: " + coefDet);
        System.out.println("----------------------------------");
    }
    
    private double spd(ArrayList<Double> x, ArrayList<Double> y) {
        int tamanho = x.size();
        double somatorioX = 0, somatorioY = 0, somatorioXY = 0;
        for(int i = 0; i < tamanho; i++) {
            somatorioX += x.get(i);
            somatorioY += y.get(i);
            somatorioXY += x.get(i) * y.get(i);
        }
        return somatorioXY - somatorioX * somatorioY / tamanho;
    }
    
    private double sqd(ArrayList<Double> valores) {
        double somatorioX = 0, somatorioXX = 0;
        for(double iterador : valores) {
            somatorioX += iterador;
            somatorioXX += Math.pow(iterador, 2);
        }
        return somatorioXX - somatorioX * somatorioX / valores.size();
    }
    
    private double media(ArrayList<Double> valores) {
        double somatorio = 0;
        for(double iterador : valores) {
            somatorio += iterador;
        }
        return somatorio / valores.size();
    }
    
}
