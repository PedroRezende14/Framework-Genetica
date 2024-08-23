package Genetica;

/**
 * A classe GenomeAnalysisFacade fornece uma interface simplificada para as principais
 * funcionalidades de análise de genoma, incluindo download de sequências FASTA,
 * comparação de sequências e análise de frequência de códons.
 * @author rezende
 */
public class GeneticaFacade{
    private FastaBaixar fastaBaixar;


    /**
     * Construtor que inicializa as classes necessárias para as análises.
     */
    public GeneticaFacade() {
        this.fastaBaixar = new FastaBaixar();
    }

    /**
     * Baixa a sequência FASTA para uma espécie específica.
     *
     * @param scientificName O nome científico da espécie.
     */
    public void BaixarFasta(String scientificName) {
        fastaBaixar.baixar(scientificName);
    }

    /**
     * Compara duas sequências FASTA.
     *
     * @param file1 O nome do primeiro arquivo FASTA.
     * @param file2 O nome do segundo arquivo FASTA.
     */
    public void CompararArquivos(String file1, String file2) {
        FastaComparar.CompararArquivos(file1, file2);
    }

    /**
     * Analisa a frequência de códons em uma sequência FASTA.
     *
     * @param fastaFile O nome do arquivo FASTA a ser analisado.
     */
    public void CodonAnalise(String fastaFile) {
        Genetica.Codon.CodonAnalise(fastaFile);
    }

    /**
     * Realiza uma análise completa: baixa duas sequências FASTA, compara-as e
     * analisa a frequência de códons em ambas.
     *
     * @param scientificName1 O nome científico da primeira espécie.
     * @param scientificName2 O nome científico da segunda espécie.
     */
    public void AnaliseCompleta(String scientificName1, String scientificName2) {
        // Download da primeira sequência
        BaixarFasta(scientificName1);

        try {
            // Pausa de 6 segundos antes de realizar o próximo download
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Download da segunda sequência
        BaixarFasta(scientificName2);

        // Nomes dos arquivos FASTA
        String file1 = scientificName1 + ".fasta";
        String file2 = scientificName2 + ".fasta";

        // Comparação das sequências
        CompararArquivos(file1, file2);

        // Análise de códons para ambas as sequências
        CodonAnalise(file1);
        CodonAnalise(file2);

        System.out.println("Análise completa realizada para " + scientificName1 + " e " + scientificName2);
    }
}