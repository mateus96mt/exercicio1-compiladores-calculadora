
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
            System.out.printf("\nErro na linha %d: caracter invalido %c\n", indiceDaLinha, c);
            return false;
        }
    }

    ArrayList<String> quebraLinha(String linha, int indiceDaLinha) {
        int i = 0;
        ArrayList<String> objetos = new ArrayList<String>();

        boolean foiDigito = false;
        boolean foiLetra = false;
        boolean foiOperador = false;
        boolean erroAoQuebrarLinha = false;
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
                                    "\nErro ao processar linha %d: sintaxe invalida, esperado digito ou variavel após %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1));
                            erroAoQuebrarLinha = true;
                            i = linha.length();
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
                            System.out.printf("\nErro ao processar linha %d: nome de variavel invalido %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1) + String.valueOf(c));
                            erroAoQuebrarLinha = true;
                            i = linha.length();
                        }
                        if (foiDigito) {
                            System.out.printf(
                                    "\nErro ao processar linha %d: sintaxe invalida, esperado operador ou digito após %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1));
                            erroAoQuebrarLinha = true;
                            i = linha.length();
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
                                    "\nErro ao processar linha %d: sintaxe invalida, esperado operador após %s\n",
                                    indiceDaLinha, objetos.get(objetos.size() - 1));
                            erroAoQuebrarLinha = true;
                            i = linha.length();
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
                    System.out.printf("\nErro ao processar linha %d: ponto e virgula (;) no meio de expressão\n",
                            indiceDaLinha);
                    erroAoQuebrarLinha = true;
                    i = linha.length();
                }
                if (!ehPontEVirgula && i == linha.length() - 1) {
                    System.out.printf("\nErro ao processar linha %d: esperado ponto e virgula (;) no final da linha\n",
                            indiceDaLinha);
                    erroAoQuebrarLinha = true;
                    i = linha.length();
                }
                foiDigito = ehDigito;
                foiLetra = ehLetra;
                foiOperador = ehOperador;
            }
            i++;
        }

        if (!erroAoQuebrarLinha) {
            return objetos;
        } else {
            return null;
        }

    }

    void processaLinha(ArrayList<String> linhaQuebrada, int indiceDaLinha) {
        if (linhaQuebrada != null && !linhaQuebrada.isEmpty()) {
            String linha = "";
            int j;
            for (j = 0; j < linhaQuebrada.size(); j++) {
                linha = linha + "[" + linhaQuebrada.get(j) + "]";
            }
            linha = linha + "\n";
            System.out.printf("\nProcessando linha %d: %s", indiceDaLinha, linha);
            ArrayList<Integer> posicoesOperadorMultiplicacao = new ArrayList<Integer>();
            ArrayList<Integer> posicoesOperadorAdicao = new ArrayList<Integer>();
            ArrayList<Integer> posicoesOperadorAtribuicao = new ArrayList<Integer>();

            int i;
            for (i = 0; i < linhaQuebrada.size(); i++) {
                String s = linhaQuebrada.get(i);
                if (s.equals("*")) {
                    posicoesOperadorMultiplicacao.add(i);
                }
                if (s.equals("+")) {
                    posicoesOperadorAdicao.add(i);
                }
                if (s.equals("=")) {
                    posicoesOperadorAtribuicao.add(i);
                }
            }

            boolean ehAtribuicao = false;
            boolean erroAoProcessarLinha = false;
            if (posicoesOperadorAtribuicao.size() > 1
                    || (posicoesOperadorAtribuicao.size() == 1 && posicoesOperadorAtribuicao.get(0) != 1)) {
                System.out.printf(
                        "\nErro ao processar linha %d: operador de atribuicao = em posicao invalida na expressao\n",
                        indiceDaLinha);
                erroAoProcessarLinha = true;
            }
            if (posicoesOperadorAtribuicao.size() == 1 && posicoesOperadorAtribuicao.get(0) == 1) {
                ehAtribuicao = true;
            }
            if (ehAtribuicao && !(Character.isLetter(linhaQuebrada.get(0).charAt(0))
                    && Character.isLowerCase(linhaQuebrada.get(0).charAt(0)))) {
                System.out.printf(
                        "\nErro ao processar linha %d: operador de atribuicao = so pode ser usado para atribuir variaveis\n",
                        indiceDaLinha);
                erroAoProcessarLinha = true;
            }

            if (!erroAoProcessarLinha) {
                while (posicoesOperadorMultiplicacao.size() > 0) {
                    int posicao = posicoesOperadorMultiplicacao.get(0);
                    if (posicao + 1 >= linhaQuebrada.size() || posicao - 1 < 0) {
                        System.out.printf("\nErro ao processar linha %d: expressao invalida\n", indiceDaLinha);
                        erroAoProcessarLinha = true;
                        posicoesOperadorMultiplicacao = new ArrayList<Integer>();
                    } else {
                        String v1 = linhaQuebrada.get(posicao - 1);
                        String v2 = linhaQuebrada.get(posicao + 1);
                        char c1 = v1.charAt(0);
                        char c2 = v2.charAt(0);
                        int valor1, valor2;
                        if ((Character.isLetter(c1) && Character.isLowerCase(c1))) {
                            valor1 = variaveis[(int) c1 - 97];
                        } else {
                            valor1 = Integer.parseInt(v1);
                        }
                        if ((Character.isLetter(c2) && Character.isLowerCase(c2))) {
                            valor2 = variaveis[(int) c2 - 97];
                        } else {
                            valor2 = Integer.parseInt(v2);
                        }
                        int result = valor1 * valor2;
                        linhaQuebrada.set(posicao, String.valueOf(result));
                        linhaQuebrada.remove(posicao + 1);
                        linhaQuebrada.remove(posicao - 1);

                        linha = "";
                        for (j = 0; j < linhaQuebrada.size(); j++) {
                            linha = linha + "[" + linhaQuebrada.get(j) + "]";
                        }
                        linha = linha + "\n";
                        System.out.printf("\nProcessando linha %d: %s", indiceDaLinha, linha);

                        posicoesOperadorMultiplicacao = new ArrayList<Integer>();
                        for (i = 0; i < linhaQuebrada.size(); i++) {
                            String s = linhaQuebrada.get(i);
                            if (s.equals("*")) {
                                posicoesOperadorMultiplicacao.add(i);
                            }
                        }
                    }
                }
                posicoesOperadorAdicao = new ArrayList<Integer>();
                for (i = 0; i < linhaQuebrada.size(); i++) {
                    String s = linhaQuebrada.get(i);
                    if (s.equals("+")) {
                        posicoesOperadorAdicao.add(i);
                    }
                }
                while (posicoesOperadorAdicao.size() > 0) {
                    int posicao = posicoesOperadorAdicao.get(0);
                    if (posicao + 1 >= linhaQuebrada.size() || posicao - 1 < 0) {
                        System.out.printf("\nErro ao processar linha %d: expressao invalida\n", indiceDaLinha);
                        erroAoProcessarLinha = true;
                        posicoesOperadorAdicao = new ArrayList<Integer>();
                    } else {
                        String v1 = linhaQuebrada.get(posicao - 1);
                        String v2 = linhaQuebrada.get(posicao + 1);
                        char c1 = v1.charAt(0);
                        char c2 = v2.charAt(0);
                        int valor1, valor2;
                        if ((Character.isLetter(c1) && Character.isLowerCase(c1))) {
                            valor1 = variaveis[(int) c1 - 97];
                        } else {
                            valor1 = Integer.parseInt(v1);
                        }
                        if ((Character.isLetter(c2) && Character.isLowerCase(c2))) {
                            valor2 = variaveis[(int) c2 - 97];
                        } else {
                            valor2 = Integer.parseInt(v2);
                        }
                        int result = valor1 + valor2;
                        linhaQuebrada.set(posicao, String.valueOf(result));
                        linhaQuebrada.remove(posicao + 1);
                        linhaQuebrada.remove(posicao - 1);

                        linha = "";
                        for (j = 0; j < linhaQuebrada.size(); j++) {
                            linha = linha + "[" + linhaQuebrada.get(j) + "]";
                        }
                        linha = linha + "\n";
                        System.out.printf("\nProcessando linha %d: %s", indiceDaLinha, linha);

                        posicoesOperadorAdicao = new ArrayList<Integer>();
                        for (i = 0; i < linhaQuebrada.size(); i++) {
                            String s = linhaQuebrada.get(i);
                            if (s.equals("+")) {
                                posicoesOperadorAdicao.add(i);
                            }
                        }
                    }
                }

                if (!erroAoProcessarLinha) {
                    if (ehAtribuicao) {
                        variaveis[(int) linhaQuebrada.get(0).charAt(0) - 97] = Integer.parseInt(linhaQuebrada.get(2));
                    } else {
                        char c = linhaQuebrada.get(0).charAt(0);
                        if ((Character.isLetter(c) && Character.isLowerCase(c))) {
                            System.out.printf("\nSaida do processamento da linha %d: %d\n", indiceDaLinha,
                                    variaveis[(int) linhaQuebrada.get(0).charAt(0) - 97]);
                        } else {
                            System.out.printf("\nSaida do processamento da linha %d: %s\n", indiceDaLinha,
                                    linhaQuebrada.get(0));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Calculadora calculadora = new Calculadora();
        System.out.printf("\n\n-----------------CALCULADORA-----------------:\n");
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
            System.out.printf("\n\n--------------------------------------------:\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
