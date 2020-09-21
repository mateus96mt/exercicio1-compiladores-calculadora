
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Calculadora {
    int[] variaveis = new int[25];

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

    void processaLinha(ArrayList<String> linhaQuebrada, int indiceDaLinha) {
        int posicao = 0;
        boolean ehPrint = true;
        boolean ehDefinicaoDeVariavel = false;
        if (linhaQuebrada != null && !linhaQuebrada.isEmpty()) {
            String s = String.valueOf(linhaQuebrada.get(0).charAt(0));
            if (s.equals("+") || s.equals("*") || linhaQuebrada.get(0).equals("=")) {
                System.out.printf("Erro ao processar linha %d: operador %s logo no inicio da expressao\n",
                        indiceDaLinha, linhaQuebrada.get(0));
            }
            char s1 = s.charAt(0);
            char s2 = ' ';
            if (linhaQuebrada.size() > 1) {
                s2 = s.charAt(1);
            }
            if (linhaQuebrada.size() > 1 && (Character.isLetter(s1) && Character.isLowerCase(s1))) {
                if (s2 != '=') {
                    ehPrint = true;
                } else {
                    ehDefinicaoDeVariavel = true;
                }

            }
        }
        int variavel;
        int i = 0;
        int result = 0;
        for (i = 0; i < linhaQuebrada.size(); i++) {
            char c = linhaQuebrada.get(i).charAt(0);
            int aux1 = 0;
            int aux2 = 0;
            if (c == '*') {
                String a = linhaQuebrada.get(i - 1);
                String b = linhaQuebrada.get(i + 1);
                if (Character.isLetter(a.charAt(0)) && Character.isLowerCase(a.charAt(0))) {
                    aux1 = variaveis[(int) a.charAt(0) - 97];
                } else {
                    aux1 = Integer.parseInt(a);
                }
                if (Character.isLetter(b.charAt(0)) && Character.isLowerCase(b.charAt(0))) {
                    aux2 = variaveis[(int) b.charAt(0) - 97];
                } else {
                    aux2 = Integer.parseInt(a);
                }
                result = aux1 * aux2;
                aux1 = aux1 * aux2;
            }
            if (c == '+') {
                
            }
            if (c == '=') {

            }
        }
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
                ArrayList<String> linhaQuebrada = calculadora.quebraLinha(linha, indiceDaLinha);
                calculadora.processaLinha(linhaQuebrada, indiceDaLinha);
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
