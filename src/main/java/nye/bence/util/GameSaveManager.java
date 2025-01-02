package nye.bence.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import nye.bence.game.Board;
import nye.bence.user.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility class for managing game saves.
 */
public final class GameSaveManager {

    /**
     * Private constructor to prevent instantiation.
     */
    private GameSaveManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Saves the game state to an XML file.
     *
     * @param board the game board
     * @param player the player
     */
    public static void saveGame(final Board board, final Player player) {
        try {
            DocumentBuilderFactory docFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);

            // Player element
            Element playerElement =
                    doc.createElement("player");
            playerElement.setAttribute("name", player.getName());
            playerElement.setAttribute("wins",
                    String.valueOf(player.getWins()));
            rootElement.appendChild(playerElement);

            // Board element
            Element boardElement = doc.createElement("board");
            rootElement.appendChild(boardElement);

            int[][] boardArray = board.getBoard();
            for (int y = 0; y < Board.SIZE_Y; y++) {
                for (int x = 0; x < Board.SIZE_X; x++) {
                    Element cell = doc.createElement("cell");
                    cell.setAttribute("value",
                            String.valueOf(boardArray[y][x]));
                    cell.setAttribute("x", String.valueOf(x));
                    cell.setAttribute("y", String.valueOf(y));
                    boardElement.appendChild(cell);
                }
            }

            // Write the content into XML file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("saves/"
                                            + player.getName() + "_save.xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the game state from an XML file.
     *
     * @param player the player
     * @return the loaded game board, or null if loading failed
     */
    public static Board loadGame(final Player player) {
        try {
            File file = new File("saves/" + player.getName() + "_save.xml");
            if (!file.exists() || file.length() == 0) {
                System.out.println("-------------------------------------");
                System.out.println("There's no save for this user.");
                return null;
            }

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList cellList = doc.getElementsByTagName("cell");
            Board board = new Board();
            int[][] boardArray = board.getBoard();

            for (int i = 0; i < cellList.getLength(); i++) {
                Element cell = (Element) cellList.item(i);
                int x = Integer.parseInt(cell.getAttribute("x"));
                int y = Integer.parseInt(cell.getAttribute("y"));
                int value = Integer.parseInt(cell.getAttribute("value"));
                boardArray[y][x] = value;
            }

            Files.delete(Path.of(file.getPath()));

            return board;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
