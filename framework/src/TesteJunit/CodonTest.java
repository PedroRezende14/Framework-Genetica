package TesteJunit;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import Genetica.Codon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CodonTest {

    @TempDir
    Path tempDir;

    @Test
    void testCodonAnalysis() throws IOException {
        // Criação de um arquivo FASTA temporário
        Path fastaFile = tempDir.resolve("test.fasta");
        Files.writeString(fastaFile, ">test_sequence\nATGCGATAGCTAGCTAACGCGTAGCTAGCTAGGCTAATG");

        // Chamada do método para análise do códons
        Codon.CodonAnalise(fastaFile.toString());

        Path outputFile = tempDir.resolve("test_codon_analysis.txt");
        assertTrue(Files.exists(outputFile));

        // Verificação do conteúdo do arquivo de saída
        String analysisResult = Files.readString(outputFile);
        assertTrue(analysisResult.contains("Análise de frequência de códons para test.fasta:"));
        assertTrue(analysisResult.contains("ATG"));  // Verifica se o códon "ATG" foi identificado
        assertTrue(analysisResult.contains("TAA"));  // Verifica se o códon "TAA" foi identificado
    }

    @Test
    void testCalculateCodonFrequency() {
        // Teste para verificar a contagem dos códons
        String sequence = "ATGCGATAGCTAGCTAACGCGTAGCTAGCTAGGCTAATG";
        Map<String, Integer> codonFrequency = Codon.calculateCodonFrequency(sequence);

        assertEquals(2, codonFrequency.get("ATG"));
        assertEquals(1, codonFrequency.get("CGA"));
        assertEquals(2, codonFrequency.get("TAG"));
    }

    @Test
    void testGetAminoacid() {
        // Teste para verificar a tradução de códons em aminoácidos
        assertEquals("Met", Codon.getAminoacid("ATG"));
        assertEquals("Leu", Codon.getAminoacid("CTG"));
        assertEquals("Stop", Codon.getAminoacid("TAA"));
        assertEquals("???", Codon.getAminoacid("XXX"));
    }

    @Test
    void testReadFastaSequence() throws IOException {
        // Teste para verificar a leitura do arquivo FASTA
        Path fastaFile = tempDir.resolve("test.fasta");
        Files.writeString(fastaFile, ">test_sequence\nATGCGATAGCTAGCTAACGCGTAGCTAGCTAGGCTAATG");

        String sequence = Codon.readFastaSequence(fastaFile.toString());
        assertEquals("ATGCGATAGCTAGCTAACGCGTAGCTAGCTAGGCTAATG", sequence);
    }

    @Test
    void testWriteAnalysisToFile() throws IOException {
        // Teste para verificar a escrita do relatório em um arquivo
        String content = "Análise de frequência de códons";
        Path outputFile = tempDir.resolve("output.txt");

        Codon.writeAnalysisToFile(content, outputFile.toString());

        assertTrue(Files.exists(outputFile));
        assertEquals(content, Files.readString(outputFile));
    }
}
