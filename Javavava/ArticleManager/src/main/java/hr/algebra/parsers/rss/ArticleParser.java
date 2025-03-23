/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.parsers.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Article;
import hr.algebra.model.Author;
import hr.algebra.model.Category;
import hr.algebra.utilities.FileUtils;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author daniel.bele
 */
public class ArticleParser {

    private static final String RSS_URL = "https://screenrant.com/feed/";
    private static final String ATTRIBUTE_URL = "url";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    public static List<Article> parse() throws IOException, XMLStreamException {
        List<Article> articles = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);
        int responseCode = con.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to fetch RSS feed: HTTP error code " + responseCode);
        }
        try (InputStream is = con.getInputStream()) { // stream will close the connection
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Optional<TagType> tagType = Optional.empty();
            Article article = null;
            StartElement startElement = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        // put breakpoint here
                        if (tagType.isPresent() && tagType.get().equals(TagType.ITEM)) {
                            article = new Article();
                            articles.add(article);
                        }
                        if (tagType.isPresent() && tagType.get().equals(TagType.ENCLOSURE) && article != null) {
                            Attribute urlAttribute = startElement.getAttributeByName(new QName(ATTRIBUTE_URL));
                            if (urlAttribute != null) {
                                handlePicture(article, urlAttribute.getValue());
                            }
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        if (tagType.isPresent() && article != null) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            switch (tagType.get()) {
                                case TITLE -> {
                                    if (!data.isEmpty()) {
                                        article.setTitle(data);
                                    }
                                }
                                case LINK -> {
                                    if (!data.isEmpty()) {
                                        article.setLink(data);
                                    }
                                }
                                case AUTHOR -> {
                                    if (!data.isEmpty()) {
                                        article.addAuthorToList(new Author(data.toString()));
                                    }
                                }
                                case CATEGORY -> {
                                    if (!data.isEmpty()) {
                                        article.addCategoryToList(new Category(data.toString()));
                                    }
                                }
                                case DESCRIPTION -> {
                                    if (!data.isEmpty()) {
                                        article.setDescription(data);
                                    }
                                }
//                                case ENCLOSURE -> {
//                                    if (startElement != null && article.getPicturePath() == null) {
//                                        Attribute urlAttribute = startElement.getAttributeByName(new QName(ATTRIBUTE_URL));
//                                        if (urlAttribute != null) {
//                                            handlePicture(article, urlAttribute.getValue());
//                                        }
//                                    }
//                                }
                                case PUB_DATE -> {
                                    if (!data.isEmpty()) {
                                        LocalDateTime publishedDate = LocalDateTime.parse(data, DateTimeFormatter.RFC_1123_DATE_TIME);
                                        article.setPublishedDate(publishedDate);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return articles;

    }

    private static void handlePicture(Article article, String pictureUrl) {
        // if picture is not ok, we must continue!!!
        try {
            String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
            if (ext.length() > 4) {
                ext = EXT;
            }
            String pictureName = UUID.randomUUID() + ext;
            String localPicturePath = DIR + File.separator + pictureName;

            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
            // put breakpoint
            article.setPicturePath(localPicturePath);
        } catch (IOException ex) {
            Logger.getLogger(ArticleParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        LINK("link"),
        AUTHOR("creator"),
        CATEGORY("category"),
        DESCRIPTION("description"),
        ENCLOSURE("enclosure"),
        PUB_DATE("pubDate");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

}
