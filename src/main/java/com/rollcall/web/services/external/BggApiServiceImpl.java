package com.rollcall.web.services.external;

import com.rollcall.web.dto.GameDto;
import com.rollcall.web.models.Category;
import com.rollcall.web.models.Game;
import com.rollcall.web.repository.CategoryRepository;
import com.rollcall.web.repository.GameRepository;
import com.rollcall.web.repository.UserRepository;
import com.rollcall.web.services.BggApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.persistence.EntityNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Service
public class BggApiServiceImpl implements BggApiService {


    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    public BggApiServiceImpl(GameRepository gameRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public void updateGamePhotoUrl(Long bggId, String photoUrl) {
        Game game = gameRepository.findByBggId(bggId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with bggId: " + bggId));
        game.setPhotoURL(photoUrl);
        gameRepository.save(game);
    }


    @Override
    public Long parseBggLink(String bggLink) throws MalformedURLException {
        Long id;
        String urlStr = "https://boardgamegeek.com/boardgame/5/acquire";
        URL url = new URL(bggLink);
        String path = url.getPath();
        String[] segments = path.split("/");
        // Assuming the ID is always the second segment after 'boardgame'
        String gameIdStr = segments[2]; // '5'
        return Long.parseLong(gameIdStr);
    }


    public GameDto fetchGameDetails(Long gameId) {
        String title = "";
        String type = "";
        String photoUrl = "";
        String description = "";
        Set<String> categoriesSet = new HashSet<>();
        Set<Category> categoryEntities = new HashSet<>();
        int yearPublished = 0;
        int minPlayers = 0;
        int maxPlayers = 0;
        int playingTime = 0;
        double averageRating = 0.0;
        String bggLink = "";



        try {
            URL url = new URL("https://boardgamegeek.com/xmlapi2/thing?id=" + gameId + "&stats=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            Element itemElement = (Element) doc.getElementsByTagName("item").item(0);
            type = itemElement.getAttribute("type");

            NodeList names = doc.getElementsByTagName("name");
            for (int i = 0; i < names.getLength(); i++) {
                Node node = names.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if ("primary".equals(element.getAttribute("type"))) {
                        title = element.getAttribute("value");
                        break;
                    }
                }
            }

            NodeList imageNodes = doc.getElementsByTagName("image");
            if (imageNodes.getLength() > 0) {
                photoUrl = imageNodes.item(0).getTextContent();
            }

            NodeList descriptionNodes = doc.getElementsByTagName("description");
            if (descriptionNodes.getLength() > 0) {
                description = descriptionNodes.item(0).getTextContent();
                description = description.replace("<br/>", "\n");
            }

            Element yearpublishedElement = (Element) doc.getElementsByTagName("yearpublished").item(0);
            if (yearpublishedElement != null) {
                yearPublished = Integer.parseInt(yearpublishedElement.getAttribute("value"));
            }

            Element minplayersElement = (Element) doc.getElementsByTagName("minplayers").item(0);
            if (minplayersElement != null) {
                minPlayers = Integer.parseInt(minplayersElement.getAttribute("value"));
            }

            Element maxplayersElement = (Element) doc.getElementsByTagName("maxplayers").item(0);
            if (maxplayersElement != null) {
                maxPlayers = Integer.parseInt(maxplayersElement.getAttribute("value"));
            }

            Element playingtimeElement = (Element) doc.getElementsByTagName("playingtime").item(0);
            if (playingtimeElement != null) {
                playingTime = Integer.parseInt(playingtimeElement.getAttribute("value"));
            }
            NodeList links = doc.getElementsByTagName("link");
            for (int i = 0; i < links.getLength(); i++) {
                Node node = links.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if ("boardgamecategory".equals(element.getAttribute("type"))) {
                        // Add the category value to the Set
                        categoriesSet.add(element.getAttribute("value"));
                    }
                }

            }

            for (String categoryName : categoriesSet) {
                Category category = categoryRepository.findByName(categoryName)
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(categoryName);
                            // Optionally save the newCategory to the database if needed
                            return categoryRepository.save(newCategory);
                        });
                categoryEntities.add(category);
            }


            Element averageRatingElement = (Element) doc.getElementsByTagName("average").item(0);
            if (averageRatingElement != null && !averageRatingElement.getAttribute("value").isEmpty()) {
                averageRating = Double.parseDouble(averageRatingElement.getAttribute("value"));
            }

            bggLink = "https://boardgamegeek.com/" + type + "/" + gameId;



        } catch (Exception e) {
            e.printStackTrace();
        }

        return new GameDto(gameId, title, type, photoUrl, description, yearPublished, minPlayers, maxPlayers, playingTime, averageRating, categoryEntities, bggLink);
    }

    //bgg csv fields - name,game_id,type,rating,weight,year_published,min_players,max_players,min_play_time,max_play_time,min_age,owned_by,categories,mechanics,designers,artists,publishers

    @Transactional
    public void importGamesFromCsv(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Improved split to handle commas inside quotes
                long bggId = Long.parseLong(values[1]); // Assuming BGG ID is at index 1
                // Check if game already exists to update it or create a new one
                Game game = gameRepository.findByBggId(bggId).orElse(new Game());
                // Populate or update game's properties from CSV
                game.setTitle(values[0]);
                game.setBggId(bggId);
                game.setType(values[2]);
                game.setAverageRating(Double.parseDouble(values[3]));
                game.setYearPublished(Integer.parseInt(values[5]));
                game.setMinPlayers(Integer.parseInt(values[6]));
                game.setMaxPlayers(Integer.parseInt(values[7]));
                game.setPlayingTime(Integer.parseInt(values[9])); // Set playing time as maxPlayTime for simplicity

                if (values.length > 12 && values[12] != null && !values[12].isEmpty()) {
                    // Remove leading and trailing quotes, then split by ", " assuming categories are comma-separated and possibly quoted as a whole
                    String categoriesString = values[12].replaceAll("^\"|\"$", "");
                    String[] categoryNames = categoriesString.split(",\\s*"); // Splits by comma followed by any amount of whitespace

                    Set<Category> categories = new HashSet<>();
                    for (String categoryName : categoryNames) {
                        String trimmedName = categoryName.trim(); // Trim to remove any leading/trailing whitespace not caught by split

                        Category category = categoryRepository.findByName(trimmedName)
                                .orElseGet(() -> {
                                    Category newCategory = new Category();
                                    newCategory.setName(trimmedName);
                                    return categoryRepository.save(newCategory);
                                });
                        categories.add(category);
                    }
                    game.setCategories(categories);
                }

                // Save the game to the database
                gameRepository.save(game);
                // Before saving, fetch and set the photo URL
                fetchAndUpdateGamePhotoUrl(bggId); // Fetch and update photo URL
                // Additional sleep to prevent rate limit issues, handle InterruptedException
                try {
                    Thread.sleep(1500); // Consider removing or adjusting sleep based on actual rate limit policies
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchAndUpdateGamePhotoUrl(Long bggId) {
        // Fetch the game details from BGG API
        GameDto gameDetails = fetchGameDetails(bggId);

        // Retrieve the Game entity by bggId from the database
        Game game = gameRepository.findByBggId(bggId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with bggId: " + bggId));

        // Update the Game entity with the fetched details
        game.setPhotoURL(gameDetails.getPhotoURL());
        game.setDescription(gameDetails.getDescription());
        game.setBggLink(gameDetails.getBggLink());

        // Save the updated Game entity back to the database
        gameRepository.save(game);
    }


}
