package logic;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {
//..........................................................................................................................................//
    // RESUMO -> Pego o string de numeros e transformo em um array de numeros inteiros
	static int[] PegarNumeros(int tamanho, String numeros) { 
        int var[] = new int[tamanho];
        for (int j = 0; j < tamanho; j++) {
            var[j] = Character.getNumericValue(numeros.charAt(j)); 
        }
        return var; // Retorna um array de inteiros
    }
	
//..........................................................................................................................................//
        //RESUMO -> Retorna se a expressao e legitma ou nao
	    static boolean isLegit(String exp) {
	       
	        if(exp.length() == 1 && exp.charAt(0) >= 'A' && exp.charAt(0) <= 'Z')
	            return true;
	       
	        int s = 0;
	        int f = exp.length() - 1;
	       
	        if(exp.charAt(s) == '(' && exp.charAt(f) == ')' && exp.charAt(s + 1) == '~' && s + 2 < f)
	            return isLegit(exp.substring(s + 2, f));
	       
	        else if(exp.charAt(s) == '(' && exp.charAt(f) == ')') {
	            int operatorIndex = findOperator(exp);
	            if(operatorIndex != -1 &&  s + 1 < operatorIndex - 1 && operatorIndex + 2 < f)
	                return isLegit(exp.substring(s + 1, operatorIndex - 1)) &&
	                        isLegit(exp.substring(operatorIndex + 2, f));
	        }  
	       
	        return false;
	       
        }
        
//..........................................................................................................................................//
        // RESUMO -> Retorna se a expressao e satisfativel ou nao
        static boolean satisfies(Map<Character, Boolean> valoresverdade, String exp) {
	       
	        if(exp.length() == 1)
	            return valoresverdade.get(exp.charAt(0));
	       
	        int s = 0;
	        int f = exp.length() - 1;
	        int operatorIndex = findOperator(exp);
	        switch(exp.charAt(operatorIndex)) {
	       
	        case '~': return !satisfies(valoresverdade, exp.substring(operatorIndex + 1, f));
	        case '&': return
	                    satisfies(valoresverdade, exp.substring(s + 1, operatorIndex - 1))
	                    && satisfies(valoresverdade, exp.substring(operatorIndex + 2, f));
	        case 'v': return
	                satisfies(valoresverdade, exp.substring(s + 1, operatorIndex - 1))
	                || satisfies(valoresverdade, exp.substring(operatorIndex + 2, f));
	        case '>': return
	                !satisfies(valoresverdade, exp.substring(s + 1, operatorIndex - 1))
	                || satisfies(valoresverdade, exp.substring(operatorIndex + 2, f));
	        default:
	                return true;
	        }
	    }
//.........................................................................................................................................//
        //RESUMO -> Encontra o operador central da expressao
	    static int findOperator(String exp) {
	       
	        int numParenthesis = 0;
	        for(int i = 0; i < exp.length(); i++) {
	           
	            if(exp.charAt(i) == '(')
	                numParenthesis++;
	            else if(exp.charAt(i) == ')')
	                numParenthesis--;
	           
	            if((exp.charAt(i) == '>' || exp.charAt(i) == '&' || exp.charAt(i) == 'v' ||
	                    exp.charAt(i) == '~') && numParenthesis == 1)
	                return i;
	        }
	       
	        return -1;     
        }
     
//........................Troca o nome do arquivo .in para SAIDA.out ..........................................//
		public static String getPath(String path) {
			String path1 = path.replaceAll(".in", "SAIDA.out");
			return path1;
		}

//...........................................................................................................................................//
        //RESUMO -> Metodo main
	    public static void main(String[] args) {
		    String path = "C:\\Users\\Ramom José\\Desktop\\Arquivos\\Entrada3.in"; //colocar o diretório de entrada e saida
		    String path1 = getPath(path);
	    
   	    try {

   		    PrintWriter writer = new PrintWriter(path1, "UTF-8");
            File file = new File(path);
            Scanner tec = new Scanner(file);
    
            int n = tec.nextInt(); //quantidade de expressoes
            tec.nextLine();
            
             for(int i = 0; i < n; i++) {
                String exp = tec.nextLine();
                Map<Character, Boolean> valoresverdade = new HashMap<>(); //HashMap - relaciona A com 1, exemplo
                 
            //.....................Separa a expressao da valoracao.................................//

                String letras = exp; // inicialmente com numeros. Depois a expressao fica sem numeros
                String numeros = ""; // string de numeros
                int tam = letras.length();
                
                for(int k = 0; k < tam; k++) {
                    if(letras.charAt(k) == '0' || letras.charAt(k) == '1') {
                        numeros = letras.substring(k); // tira as letras mas continua String
                        letras = letras.substring(0, k-1); // tira os numeros
                        k = tam; //stop
                    }
                }

            //.......................................................................................//       
                numeros = numeros.replaceAll("\\s","");  // junta todos os numeros - sem espaco
                tam = numeros.length();
                
                int arrayNumerosValores[] = PegarNumeros(tam, numeros);
                
                boolean valores[] = new boolean[tam]; //array da valoração
                
                for (int k = 0; k < tam; k++) { // coloco os valores verdades no array(valores) de acordo com os numeros(1-True, 0-False)
                    if (arrayNumerosValores[k] == 0)
                        valores[k] = false;
                    else if (arrayNumerosValores[k] == 1) {
                        valores[k] = true;
                    }
                }
                
                int k1 = 0; //index do array de valores que sera usado posteriormente
                String novaLetras = letras; //string de expressao sem numeros
                tam = letras.length();

                //...............................Colocando a valoracao em cada letra....................................//
                for(int k = 0; k < tam; k++) {
                       
                    if((int)letras.charAt(k) >= 'A' && (int)letras.charAt(k) <= 'Z') { //So pega as letras de A a Z para valora-las
                       
                        valoresverdade.put(letras.charAt(k), valores[k1]); //coloca o valor verdade em cada letra no mapa hash(valoresverdade)
                        k1++;
                        letras = letras.replace(letras.charAt(k), '-');
                    }
                }
                //.....................................................................................................//

                letras = novaLetras; //volta ao normal a variavel letras
               
                //etapa 4
                if(letras.charAt(0) != '{' ) {// se não for um conjunto de expressoes
                        if(isLegit(letras)) {
                            if(satisfies(valoresverdade,letras)) {
                                writer.println("Problema #" + (i+1));
                                writer.println("A valoracao-verdade satisfaz a proposicao.");
                                if(i != n-1)
                                writer.println("");
                            }else {
                                writer.println("Problema #" + (i+1));
                                writer.println("A valoracao-verdade nao satisfaz a proposicao.");
                                if(i != n-1)
                                writer.println("");
                            }
                        }else {
                            writer.println("Problema #" + (i+1));
                            writer.println("A palavra nao e legitima.");
                            if(i != n-1)
                            writer.println("");
                        }
                }
                
                else{ //se for um conjunto de expressoes

                    String[] vetorSentencas = letras.split(","); // armazena cada sentença em um index
                    vetorSentencas[0] = vetorSentencas[0].substring(1); // tira a { chave do inicio
                    
                    // tira a ultima chave } e ja armazena na sentenca
                    vetorSentencas[vetorSentencas.length - 1] = vetorSentencas[vetorSentencas.length - 1].substring(0, vetorSentencas[vetorSentencas.length - 1].length()- 1);
                    
                    // garanto que o resto fique no seu index certo já que o [inicial] e o [ultimo] estao certos pelo de cima           
                    for(int k = 1; k < vetorSentencas.length;k++) {
                        vetorSentencas[k] = vetorSentencas[k].substring(1);
                    }
                    
                    int status = 0;
                    int alltrue = 0;
                    int aux = 0;
                    
                    //.....Testa todas as sentencas do vetorSentencas e encontra a satisfativel....// 
                    while(aux < vetorSentencas.length && status == 0) {
                    	if(!isLegit(vetorSentencas[aux])) { 
                    		 writer.println("Problema #" + (i+1));
                             writer.println("Ha uma palavra nao legitima no conjunto.");
                             if(i != n-1)
                             writer.println("");
                             aux = -1;
                    		 status = 1;
                    	}
                    	aux++;
                    }
                    aux = 0;
                    //............................................................................//

                    //...Testa todas as sentencas do vetorSentencas e encontra a nao satisfativel....//
                    while(aux < vetorSentencas.length && status != 1) {
                    	if(!satisfies(valoresverdade, vetorSentencas[aux])) {
                    		 writer.println("Problema #" + (i+1));
                             writer.println("A valoracao-verdade nao satisfaz o conjunto.");
                             if(i != n-1)
                             writer.println("");
                             status = 1;
                             aux = -1;
                    	}
                    	aux++;
                    	if(aux >= vetorSentencas.length)
                    		alltrue = 1;
                    }
                    //...........................................................................//

                    //..................Se todas as sentencas forem satisfativeis................//
                    if(alltrue == 1) {
                        writer.println("Problema #" + (i+1));
                        writer.println("A valoracao-verdade satisfaz o conjunto.");
                        if(i != n-1)
                        writer.println("");
                    }
                    //..........................................................................//

                }
            }
           writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } 
}