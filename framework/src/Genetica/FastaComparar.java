package Genetica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe FastaComparar é responsável por comparar dois arquivos FASTA
 * e gerar um relatório de similaridade entre as sequências.
 * @author rezende
 */

public class FastaComparar {


    /**
     * Compara dois arquivos FASTA e gera um arquivo de texto com o resultado da comparação.
     *
     * @param file1 O nome do primeiro arquivo FASTA.
     * @param file2 O nome do segundo arquivo FASTA.
     */
	
    public static void CompararArquivos(String file1, String file2) {
    	 // Criação dos objetos File para os arquivos FASTA
        File Fasta1 = new File(file1);
        File Fasta2 = new File(file2);

        try {
        	// Leitura das sequências dos arquivos FASTA
            String seq1 = readFastaSequence(file1);
            String seq2 = readFastaSequence(file2);
            // Verificação se as sequências foram lidas corretamente
            if (seq1 == null || seq2 == null) {
                System.out.println("Erro ao ler os arquivos FASTA.");
                return;
            }

            int similarity = calculateSimilarity(seq1, seq2);
            double percentSimilarity = (double) similarity / Math.max(seq1.length(), seq2.length()) * 100;

            String result = String.format("Comparação entre %s e %s:%n", file1, file2);
            result += String.format("Similaridade: %.2f%%%n", percentSimilarity);
            result += "Número de caracteres idênticos: " + similarity + "\n";
            result += "\nSequências compatíveis:\n";

            result += getCompatibleSequences(seq1, seq2);

            String baseFilename = "comparacao_" + new File(file1).getName() + "_e_" + new File(file2).getName();
            String resultFilename = baseFilename + ".txt";
            generateComparisonTextFile(result, resultFilename);
            System.out.println("Arquivo de comparação gerado: " + resultFilename);

        } catch (IOException e) {
            System.out.println("Erro ao ler os arquivos ou gerar o arquivo de texto: " + e.getMessage());
        }
    }

    /**
     * Lê a sequência de um arquivo FASTA.
     *
     * @param filename O nome do arquivo FASTA a ser lido.
     * @return A sequência contida no arquivo FASTA como uma String.
     * @throws IOException Se ocorrer um erro na leitura do arquivo.
     */
    public static String readFastaSequence(String filename) throws IOException {
        // StringBuilder para armazenar a sequência completa
        StringBuilder sequence = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Leitura de cada linha do arquivo
            while ((line = reader.readLine()) != null) {
                // Ignora as linhas que começam com ">" (cabeçalhos)
                if (!line.startsWith(">")) {
                    // Adiciona a linha (sequência) ao StringBuilder
                    sequence.append(line.trim());
                }
            }
        }
        // Retorna a sequência como String
        return sequence.toString();
    }

    /**
     * Calcula a similaridade entre duas sequências.
     *
     * @param seq1 A primeira sequência.
     * @param seq2 A segunda sequência.
     * @return O número de caracteres idênticos nas mesmas posições em ambas as sequências.
     */
    public static int calculateSimilarity(String seq1, String seq2) {
        // Determina o comprimento mínimo entre as duas sequências
        int minLength = Math.min(seq1.length(), seq2.length());
        int similarity = 0;

        // Compara as sequências caractere por caractere
        for (int i = 0; i < minLength; i++) {
            if (seq1.charAt(i) == seq2.charAt(i)) {
                // Incrementa o contador de similaridade se os caracteres forem iguais
                similarity++;
            }
        }

        // Retorna o número de caracteres idênticos
        return similarity;
    }

    /**
     * Gera uma representação das sequências compatíveis, marcando as diferenças com hífens.
     *
     * @param seq1 A primeira sequência.
     * @param seq2 A segunda sequência.
     * @return Uma String contendo as duas sequências alinhadas, com hífens nas posições diferentes.
     */
    public static String getCompatibleSequences(String seq1, String seq2) {
        // StringBuilders para armazenar as sequências compatíveis
        StringBuilder compatibleSeq1 = new StringBuilder();
        StringBuilder compatibleSeq2 = new StringBuilder();

        // Determina o comprimento mínimo entre as duas sequências
        int minLength = Math.min(seq1.length(), seq2.length());
        for (int i = 0; i < minLength; i++) {
            if (seq1.charAt(i) == seq2.charAt(i)) {
                // Adiciona o caractere à sequência compatível se for idêntico
                compatibleSeq1.append(seq1.charAt(i));
                compatibleSeq2.append(seq2.charAt(i));
            } else {
                // Adiciona hífen nas posições onde as sequências diferem
                compatibleSeq1.append('-');
                compatibleSeq2.append('-');
            }
        }

        // Retorna as sequências compatíveis formatadas
        return "Seq1: " + compatibleSeq1.toString() + "\nSeq2: " + compatibleSeq2.toString();
    }

    /**
     * Gera um arquivo de texto com o resultado da comparação.
     *
     * @param result O conteúdo do resultado da comparação.
     * @param outputFilename O nome do arquivo de saída.
     * @throws IOException Se ocorrer um erro na escrita do arquivo.
     */
    public static void generateComparisonTextFile(String result, String outputFilename) throws IOException {
        // Utiliza FileWriter para escrever o resultado no arquivo de saída
        try (FileWriter writer = new FileWriter(outputFilename)) {
            writer.write(result);
        }
    }
}