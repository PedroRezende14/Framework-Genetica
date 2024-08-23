package TesteJunit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Genetica.GeneticaFacade;

public class GeneticaFacadeTest {

    private GeneticaFacade geneticaFacade;
    private String testScientificName1 = "Anura";
    private String testScientificName2 = "Homo sapiens";

    @Before
    public void setUp() {
        geneticaFacade = new GeneticaFacade();
    }

    @After
    public void tearDown() {
        // Limpa os arquivos gerados durante os testes
        new File(testScientificName1 + ".fasta").delete();
        new File(testScientificName2 + ".fasta").delete();
    }

    @Test
    public void testBaixarFasta() {
        geneticaFacade.BaixarFasta(testScientificName1);
        File file = new File(testScientificName1 + ".fasta");
        assertTrue("O arquivo FASTA deve ser criado", file.exists());
    }

    @Test
    public void testCompararArquivos() throws IOException {
        // Criar arquivos de teste
        createTestFile(testScientificName1 + ".fasta", ">Seq1\nATGC");
        createTestFile(testScientificName2 + ".fasta", ">Seq2\nATGC");

        geneticaFacade.CompararArquivos(testScientificName1 + ".fasta", testScientificName2 + ".fasta");
        // Como não podemos verificar diretamente o resultado da comparação,
        // assumimos que se não houver exceção, o teste passa
    }

    @Test
    public void testCodonAnalise() throws IOException {
        // Criar arquivo de teste
        createTestFile(testScientificName1 + ".fasta", ">Seq1\nATGCGTACGTAGCTAG");

        geneticaFacade.CodonAnalise(testScientificName1 + ".fasta");
        // Como não podemos verificar diretamente o resultado da análise,
        // assumimos que se não houver exceção, o teste passa
    }

    @Test
    public void testAnaliseCompleta() {
        geneticaFacade.AnaliseCompleta(testScientificName1, testScientificName2);
        
        File file1 = new File(testScientificName1 + ".fasta");
        File file2 = new File(testScientificName2 + ".fasta");
        
        assertTrue("O arquivo FASTA para " + testScientificName1 + " deve ser criado", file1.exists());
        assertTrue("O arquivo FASTA para " + testScientificName2 + " deve ser criado", file2.exists());
    }

    private void createTestFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }
}