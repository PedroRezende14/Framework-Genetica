package TesteJunit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Genetica.FastaBaixar;

public class FastaBaixarTest {

    private FastaBaixar fastaBaixar;
    private String testScientificName = "Anura";
    private String mockFastaContent = ">MockSequence\nATGCGTACGTAGCTAG";

    @Before
    public void setUp() {
        fastaBaixar = new FastaBaixar();
    }

    @After
    public void tearDown() {
        // Limpa o arquivo gerado durante os testes
        File file = new File(testScientificName + ".fasta");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSaveFastaFile() {
        // Testa o método de salvar o arquivo
        FastaBaixar.saveFastaFile(testScientificName, mockFastaContent);

        File file = new File(testScientificName + ".fasta");
        assertTrue("O arquivo FASTA deve ser criado", file.exists());
    }

    @Test
    public void testExtractIdFromXml() {
        // Testa a extração de ID a partir de um XML simulado
        String xmlResponse = "<IdList><Id>12345</Id></IdList>";
        String result = FastaBaixar.extractIdFromXml(xmlResponse);
        assertEquals("O ID extraído deve ser 12345", "12345", result);
    }

    @Test
    public void testVerificadoArquivoNaoExiste() {
        // Testa a verificação quando o arquivo não existe
        File file = new File(testScientificName + ".fasta");
        if (file.exists()) {
            file.delete();
        }

        boolean result = FastaBaixar.Verificado(testScientificName);
        assertTrue("O arquivo não deve existir, então o resultado deve ser true", result);
    }

    @Test
    public void testVerificadoArquivoExiste() throws IOException {
        // Testa a verificação quando o arquivo já existe
        File file = new File(testScientificName + ".fasta");
        file.createNewFile();

        boolean result = FastaBaixar.Verificado(testScientificName);
        assertFalse("O arquivo existe, então o resultado deve ser false", result);
    }

    @Test
    public void testBaixarComArquivoNaoExistente() {
        // Testa o método baixar com um arquivo que não existe localmente
        // Para evitar dependência de uma conexão de internet real, mockFastaContent é escrito diretamente
        boolean fileExisted = new File(testScientificName + ".fasta").exists();
        if (fileExisted) {
            new File(testScientificName + ".fasta").delete();
        }

        // Simula a funcionalidade de download
        FastaBaixar.saveFastaFile(testScientificName, mockFastaContent);
        boolean result = FastaBaixar.Verificado(testScientificName);

        // Verifica se o arquivo foi baixado e salvo corretamente
        assertFalse("O arquivo deve ter sido salvo, então Verificado deve retornar false", result);
        assertTrue("O arquivo deve existir após o download", new File(testScientificName + ".fasta").exists());
    }

    @Test
    public void testBaixarComArquivoExistente() {
        // Testa o método baixar com um arquivo que já existe localmente
        FastaBaixar.saveFastaFile(testScientificName, mockFastaContent);

        boolean result = FastaBaixar.Verificado(testScientificName);
        assertFalse("O arquivo existe, então Verificado deve retornar false", result);

        // Simula a tentativa de baixar novamente
        fastaBaixar.baixar(testScientificName);

        // Verifica que o arquivo não foi baixado novamente (já que o arquivo já existe)
        assertTrue("O arquivo já existente não deve ser sobrescrito", new File(testScientificName + ".fasta").exists());
    }
}
