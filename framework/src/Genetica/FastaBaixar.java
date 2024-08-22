package Genetica;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

/**
 * A classe FastaBaixar é responsável por baixar e salvar sequências FASTA de espécies
 * a partir do NCBI (National Center for Biotechnology Information) usando seu nome científico.
 * 
 * <p>Esta classe utiliza a API E-utilities do NCBI para buscar e recuperar sequências de nucleotídeos
 * no formato FASTA. Ela também verifica se o arquivo FASTA já existe localmente antes de fazer o download.</p>
 * @author rezende
 */

public class FastaBaixar{
	
	 /**
     * Baixa e salva a sequência FASTA para o nome científico fornecido.
     * 
     * @param scientificName O nome científico da espécie para a qual a sequência FASTA será baixada.
     */
	
    public void baixar (String scientificName) {
    	
    	// Implementação do método
    	
        if(Verificado(scientificName)) {
	        String fastaContent = getFastaContent(scientificName);
	        if (fastaContent != null && !fastaContent.isEmpty()) {
	            saveFastaFile(scientificName, fastaContent);
	        } else {
	            System.out.println("O conteúdo FASTA está vazio. Verifique os logs para mais informações.");
	        }
        }
    }

    /**
     * Obtém o conteúdo FASTA do NCBI para o nome científico fornecido.
     * 
     * @param scientificName O nome científico da espécie.
     * @return Uma String contendo o conteúdo FASTA, ou null se não for encontrado.
     */
    
    public static String getFastaContent(String scientificName) {
    	// Implementação do método
        try {
            String encodedName = URLEncoder.encode(scientificName, StandardCharsets.UTF_8.toString());
            String searchUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=nucleotide&term="
                    + encodedName + "[Organism]&retmax=1&usehistory=y";
            System.out.println("Enviando requisição para: " + searchUrl);
            String xmlResponse = sendGetRequest(searchUrl);
            System.out.println("Resposta XML recebida: " + xmlResponse);
            String id = extractIdFromXml(xmlResponse);
            if (id == null) {
                System.out.println("ID não encontrado no XML.");
                return null;
            }
            String fastaUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id="
                    + id + "&rettype=fasta&retmode=text";
            System.out.println("Enviando requisição para obter FASTA: " + fastaUrl);
            String fastaContent = sendGetRequest(fastaUrl);
            System.out.println("Conteúdo FASTA recebido: " + fastaContent);
            return fastaContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Envia uma requisição GET para a URL especificada e retorna a resposta.
     * 
     * @param urlString A URL para a qual a requisição será enviada.
     * @return Uma String contendo a resposta da requisição.
     * @throws Exception Se ocorrer um erro durante a requisição.
     */
    
    public static String sendGetRequest(String urlString) throws Exception {
    	 // Implementação do método
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Erro HTTP: " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errorLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            System.out.println("Resposta de erro: " + errorResponse.toString());
            return null;
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append("\n");
        }
        in.close();
        connection.disconnect();
        return content.toString();
    }

    /**
     * Extrai o ID da sequência do XML de resposta do NCBI.
     * 
     * @param xmlResponse A resposta XML do NCBI.
     * @return O ID da sequência como uma String, ou null se não for encontrado.
     */
    
    public static String extractIdFromXml(String xmlResponse) {
        // Implementação do método
        if (xmlResponse == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("<Id>(\\d+)</Id>");
        Matcher matcher = pattern.matcher(xmlResponse);
        if (matcher.find()) {
            String id = matcher.group(1);
            System.out.println("ID extraído: " + id);
            return id;
        } else {
            System.out.println("ID não encontrado no XML.");
        }
        return null;
    }

    /**
     * Salva o conteúdo FASTA em um arquivo local.
     * 
     * @param scientificName O nome científico da espécie, usado para nomear o arquivo.
     * @param fastaContent O conteúdo FASTA a ser salvo.
     */
    public static void saveFastaFile(String scientificName, String fastaContent) {
    	// Implementação do método
        try (FileWriter fileWriter = new FileWriter(scientificName + ".fasta")) {
            fileWriter.write(fastaContent);
            System.out.println("Arquivo " + scientificName + ".fasta salvo com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica se o arquivo FASTA para o nome científico já existe localmente.
     * 
     * @param scientificName O nome científico da espécie.
     * @return true se o arquivo não existir e puder ser baixado, false caso contrário.
     */
    public static boolean Verificado(String scientificName) {
        // Implementação do método
    		String fasta = scientificName+".fasta";
            File arquivo = new File(fasta);

            if (arquivo.exists()) {
                System.out.println("O arquivo " + fasta + " ja existe.");
                return false;
            } else {
                return true;
            }	
    }
}