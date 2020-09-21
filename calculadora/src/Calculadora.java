
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Calculadora {
    double[] variaveis = new double[25];

    boolean verificaSeCacacterPertenceAoAlfabeto(char c, int indiceDaLinha) {
        if ((Character.isLetter(c) && Character.isLowerCase(c)) || Character.isDigit(c) || c == '=' || c == '*'
                || c == '+' || c == ';') {
            return true;
        } else {
            System.out.printf("Erro na linha %d: caracter invalido %c\n", indiceDaLinha, c);
            return false;
        }
    }

    ArrayList<String> quebraLinha(String linha, int indiceDaLinha) {
        int i = 0;
        ArrayList<String> objetos = new ArrayList<String>();

        boolean foiDigito = false;
        boolean foiLetra = false;
        boolean foiOperador = false;
        boolean erroAoProcessar = false;
        while (i < linha.length()) {
            char c = linha.charAt(i);
            if (c != ' ' && verificaSeCacacterPertenceAoAlfabeto(c, indiceDaLinha)) {
                boolean ehDigito = Character.isDigit(c);
                boolean ehLetra = (Character.isLetter(c) && Character.isLowerCase(c));
                boolean ehOperador = c == '=' || c == '*' || c == '+';
                boolean ehPontEVirgula = c == ';';
                ;
                if (ehOperador) {
                    if (!objetos.isEmpty()) {
                        if (foiOperador) {
                            System.out.printf(
                                    "Erro ao processar linha %d: sintaxe invalida, esperado digito ou variavel após %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1));
                            erroAoProcessar = true;
                        }
                        if (foiDigito || foiLetra) {
                            objetos.add(String.valueOf(linha.charAt(i)));
                        }
                    } else {
                        objetos.add(String.valueOf(linha.charAt(i)));
                    }
                }
                if (ehLetra) {
                    if (!objetos.isEmpty()) {
                        if (foiLetra) {
                            System.out.printf("Erro ao processar linha %d: nome de variavel invalido %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1) + String.valueOf(c));
                            erroAoProcessar = true;
                        }
                        if (foiDigito) {
                            System.out.printf(
                                    "Erro ao processar linha %d: sintaxe invalida, esperado operador ou digito após %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1));
                            erroAoProcessar = true;
                        }
                        if (foiOperador) {
                            objetos.add(String.valueOf(linha.charAt(i)));
                        }
                    } else {
                        objetos.add(String.valueOf(linha.charAt(i)));
                    }
                }
                if (ehDigito) {
                    if (!objetos.isEmpty()) {
                        if (foiLetra) {
                            System.out.printf(
                                    "Erro ao processar linha %d: sintaxe invalida, esperado operador após %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1));
                            erroAoProcessar = true;
                        } else {
                            String number = "";
                            while (Character.isDigit(c) && (i < linha.length())) {
                                number = number.concat(String.valueOf(c));
                                i++;
                                if (i < linha.length()) {
                                    c = linha.charAt(i);
                                }
                            }
                            objetos.add(number);
                            i--;
                        }
                    } else {
                        objetos.add(String.valueOf(linha.charAt(i)));
                    }
                }
                if (ehPontEVirgula && i != linha.length() - 1) {
                    System.out.printf("Erro ao processar linha %d: ponto e virgula (;) no meio de expressão\n",
                            indiceDaLinha);
                    erroAoProcessar = true;
                }
                if (!ehPontEVirgula && i == linha.length() - 1) {
                    System.out.printf("Erro ao processar linha %d: esperado ponto e virgula (;) no final da linha\n",
                            indiceDaLinha);
                    erroAoProcessar = true;
                }
                foiDigito = ehDigito;
                foiLetra = ehLetra;
                foiOperador = ehOperador;
            }
            i++;
        }

        /*
         * for (i = 0; i < objetos.size(); i++) { System.out.printf("%s",
         * objetos.get(i)); } System.out.printf("\n");
         */
        if (!erroAoProcessar) {
            return objetos;
        } else {
            return null;
        }

    }

    void processaLinha(ArrayList<String> linha) {

    }

    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora();
        System.out.printf("Calculadora:\n\n");

        BufferedReader reader;
        int indiceDaLinha = 1;
        try {
            reader = new BufferedReader(new FileReader("entrada.txt"));
            String linha = reader.readLine();
            while (linha != null) {
                calculadora.quebraLinha(linha, indiceDaLinha);
                // read next line
                linha = reader.readLine();
                indiceDaLinha++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
