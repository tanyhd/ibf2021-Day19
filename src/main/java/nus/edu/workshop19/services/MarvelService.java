package nus.edu.workshop19.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.json.*; 
import jakarta.xml.bind.DatatypeConverter;
import nus.edu.workshop19.models.Hero;

@Service
public class MarvelService {
    
    private static final String MARVEL_CHARACTERS_URL = "https://gateway.marvel.com:443/v1/public/characters";
    private static final String apikey = System.getenv("marvel_publicKey");
    private static String publicKey = System.getenv("marvel_publicKey");
    private static String privateKey = System.getenv("marvel_privateKey");
    private static String ts = "1";
    private String hash = createHash();


    public String createHash() {
        String myHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String key = ts + privateKey + publicKey;
            md.update(key.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
            return myHash;
        } catch(NoSuchAlgorithmException ex) {}
        return myHash;
    }

    public List<Hero> getHero(String hero) {

        String url = UriComponentsBuilder
                    .fromUriString(MARVEL_CHARACTERS_URL)
                    .queryParam("nameStartsWith", hero)
                    .queryParam("ts", ts)
                    .queryParam("apikey", apikey)
                    .queryParam("hash", hash)
                    .toUriString();
                    
        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        if(resp.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Error: status code %d".formatted(resp.getStatusCode().toString()));
        }
            
        String body = resp.getBody();
  
        try(InputStream is = new ByteArrayInputStream(body.getBytes())) {
    
            JsonReader reader = Json.createReader(is);
            JsonObject result = reader.readObject();

            
            JsonArray data = result.getJsonObject("data").getJsonArray("results");
            return data.stream()
                .map(v -> (JsonObject) v)
                .map(Hero::create)
                .collect(Collectors.toList());
            
        } catch (Exception e) {}
        return null;
    }

}
