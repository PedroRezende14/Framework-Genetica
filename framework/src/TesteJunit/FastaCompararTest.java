package TesteJunit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import Genetica.FastaComparar;

public class FastaCompararTest {

    private String testFile1 = "test1.fasta";
    private String testFile2 = "test2.fasta";
    private String comparisonFile = "comparacao_test1.fasta_e_test2.fasta.txt";

    @Before
    public void setUp() throws IOException {
        // Criar arquivos FASTA de teste
        createTestFile(testFile1, ">Seq1\nATGCGTACGTA");
        createTestFile(testFile2, ">Seq2\nATGCATACGTA");
    }

    @After
    public void tearDown() {
        // Limpar arquivos após os testes
        new File(testFile1).delete();
        new File(testFile2).delete();
        new File(comparisonFile).delete();
    }

    @Test
    public void testCompararArquivos() {
        FastaComparar.CompararArquivos(testFile1, testFile2);
        File resultFile = new File(comparisonFile);
        assertTrue("O arquivo de comparação deve ser criado", resultFile.exists());
    }

    @Test
    public void testReadFastaSequence() throws IOException {
        String sequence = FastaComparar.readFastaSequence(testFile1);
        assertEquals("A sequência lida deve ser correta", "ATGCGTACGTA", sequence);
    }

    @Test
    public void testCalculateSimilarity() {
        String seq1 = "ATGCGTACGTA";
        String seq2 = "ATGCATACGTA";
        int similarity = FastaComparar.calculateSimilarity(seq1, seq2);
        assertEquals("A similaridade calculada deve ser correta", 10, similarity);
    }

    @Test
    public void testGetCompatibleSequences() {
        String seq1 = "ATGCGTACGTA";
        String seq2 = "ATGCATACGTA";
        String result = FastaComparar.getCompatibleSequences(seq1, seq2);
        String expected = "Seq1: ATGC-TACGTA\nSeq2: ATGC-TACGTA";
        assertEquals("As sequências compatíveis devem ser corretas", expected, result);
    }

    @Test
    public void testGenerateComparisonTextFile() throws IOException {
        String result = "Teste de comparação";
        FastaComparar.generateComparisonTextFile(result, comparisonFile);
        String fileContent = new String(Files.readAllBytes(Paths.get(comparisonFile)));
        assertEquals("O conteúdo do arquivo de comparação deve ser correto", result, fileContent);
    }

    private void createTestFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }
}