package com.rollcall.web.services.external;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.rollcall.web.dto.GameDto;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;

@Service
public class BGGApiDetailsFetcher {
    public GameDto fetchGameDetails(long gameId) {
        String photoUrl = "";
        String description = "";
        try {
            URL url = new URL("https://api.geekdo.com/xmlapi/boardgame/" + gameId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);

            NodeList imageNodes = doc.getElementsByTagName("image");
            if (imageNodes.getLength() > 0) {
                photoUrl = imageNodes.item(0).getTextContent();
            }

            NodeList descriptionNodes = doc.getElementsByTagName("description");
            if (descriptionNodes.getLength() > 0) {
                description = descriptionNodes.item(0).getTextContent();
                description = description.replace("<br/>", "\n"); // Optional: Convert line breaks if needed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GameDto(photoUrl, description);
    }

    public static void main(String[] args) {
    }

}

