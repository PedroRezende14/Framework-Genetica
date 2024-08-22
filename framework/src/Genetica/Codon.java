package Genetica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * A classe CodonAnalyzer é responsável por analisar a frequência de códons em sequências de DNA
 * contidas em arquivos FASTA.
 * @author rezende
 */
public class Codon{
	 /**
     * Analisa a frequência de códons em um arquivo FASTA e gera um relatório.
     *
     * @param fastaFile O nome do arquivo FASTA a ser analisado.
     */
    public static void CodonAnalise(String fastaFile) {
        // Implementação do método
    	File Fasta = new File(fastaFile);
    	 if (Fasta.exists()) {
    	 
	        try {
	            String sequence = readFastaSequence(fastaFile);
	            if (sequence == null) {
	                System.out.println("Erro ao ler o arquivo FASTA.");
	                return;
	            }
	
	            Map<String, Integer> codonFrequency = calculateCodonFrequency(sequence);
	            String analysisResult = generateCodonFrequencyAnalysis(codonFrequency, fastaFile);
	            
	            String outputFileName = fastaFile.replace(".fasta", "_codon_analysis.txt");
	            writeAnalysisToFile(analysisResult, outputFileName);
	
	            System.out.println("Análise de códons concluída. Resultado salvo em: " + outputFileName);
	
	        } catch (IOException e) {
	            System.out.println("Erro ao analisar o arquivo: " + e.getMessage());
	        }
    	 }else {
    		 System.out.println("Arquivos fasta não encontrado, Verfique se o nome do arquivo, Lembre de adicionar o .Fasta");
    	 }
    }

    
    /**
     * Lê a sequência de DNA de um arquivo FASTA.
     *
     * @param filename O nome do arquivo FASTA a ser lido.
     * @return A sequência de DNA como uma String.
     * @throws IOException Se ocorrer um erro na leitura do arquivo.
     */
    public static String readFastaSequence(String filename) throws IOException {
        // Implementação do método
        StringBuilder sequence = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    if (!line.startsWith(">")) {
                        throw new IOException("Arquivo FASTA inválido: " + filename);
                    }
                    isFirstLine = false;
                } else {
                    sequence.append(line.trim());
                }
            }
        }
        return sequence.toString().toUpperCase();
    }
    
    /**
     * Calcula a frequência de cada códon na sequência de DNA.
     *
     * @param sequence A sequência de DNA.
     * @return Um Map contendo os códons como chaves e suas frequências como valores.
     */

    public static Map<String, Integer> calculateCodonFrequency(String sequence) {
    	   // Implementação do método
        Map<String, Integer> codonFrequency = new HashMap<>();
        for (int i = 0; i < sequence.length() - 2; i += 3) {
            String codon = sequence.substring(i, i + 3);
            codonFrequency.put(codon, codonFrequency.getOrDefault(codon, 0) + 1);
        }
        return codonFrequency;
    }

    
    /**
     * Gera um relatório de análise da frequência de códons.
     *
     * @param codonFrequency O Map contendo as frequências dos códons.
     * @param filename O nome do arquivo FASTA analisado.
     * @return Uma String contendo o relatório de análise.
     */
    
    public static String generateCodonFrequencyAnalysis(Map<String, Integer> codonFrequency, String filename) {
    	  // Implementação do método
        StringBuilder result = new StringBuilder();
        result.append("Análise de frequência de códons para ").append(filename).append(":\n");
        result.append(String.format("%-6s %-10s %-10s%n", "Códon", "Frequência", "Aminoácido"));
        result.append("---------------------------------------\n");

        codonFrequency.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                String codon = entry.getKey();
                int frequency = entry.getValue();
                String aminoacid = getAminoacid(codon);
                result.append(String.format("%-6s %-10d %-10s%n", codon, frequency, aminoacid));
            });

        return result.toString();
    }
    
    /**
     * Retorna o aminoácido correspondente a um códon.
     *
     * @param codon O códon (sequência de três nucleotídeos).
     * @return O nome do aminoácido correspondente ao códon.
     */

    public static String getAminoacid(String codon) {
    	 // Implementação do método
        // Tabela simplificada de códons para aminoácidos
    	switch (codon) {
        // Fenilalanina (Phe, F)
        case "TTT": case "TTC": return "Phe";
        // Leucina (Leu, L)
        case "TTA": case "TTG": case "CTT": case "CTC": case "CTA": case "CTG": return "Leu";
        // Isoleucina (Ile, I)
        case "ATT": case "ATC": case "ATA": return "Ile";
        // Metionina (Met, M)
        case "ATG": return "Met";
        // Valina (Val, V)
        case "GTT": case "GTC": case "GTA": case "GTG": return "Val";
        // Serina (Ser, S)
        case "TCT": case "TCC": case "TCA": case "TCG": case "AGT": case "AGC": return "Ser";
        // Prolina (Pro, P)
        case "CCT": case "CCC": case "CCA": case "CCG": return "Pro";
        // Treonina (Thr, T)
        case "ACT": case "ACC": case "ACA": case "ACG": return "Thr";
        // Alanina (Ala, A)
        case "GCT": case "GCC": case "GCA": case "GCG": return "Ala";
        // Tirosina (Tyr, Y)
        case "TAT": case "TAC": return "Tyr";
        // Histidina (His, H)
        case "CAT": case "CAC": return "His";
        // Glutamina (Gln, Q)
        case "CAA": case "CAG": return "Gln";
        // Asparagina (Asn, N)
        case "AAT": case "AAC": return "Asn";
        // Lisina (Lys, K)
        case "AAA": case "AAG": return "Lys";
        // Ácido aspártico (Asp, D)
        case "GAT": case "GAC": return "Asp";
        // Ácido glutâmico (Glu, E)
        case "GAA": case "GAG": return "Glu";
        // Cisteína (Cys, C)
        case "TGT": case "TGC": return "Cys";
        // Triptofano (Trp, W)
        case "TGG": return "Trp";
        // Arginina (Arg, R)
        case "CGT": case "CGC": case "CGA": case "CGG": case "AGA": case "AGG": return "Arg";
        // Glicina (Gly, G)
        case "GGT": case "GGC": case "GGA": case "GGG": return "Gly";
        // Códons de parada
        case "TAA": case "TAG": case "TGA": return "Stop";
        default: return "???";
    }
    }
    /**
     * Escreve o conteúdo da análise em um arquivo.
     *
     * @param content O conteúdo a ser escrito no arquivo.
     * @param fileName O nome do arquivo de saída.
     * @throws IOException Se ocorrer um erro na escrita do arquivo.
     */
    public static void writeAnalysisToFile(String content, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }

}