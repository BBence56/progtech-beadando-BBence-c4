package nye.bence.util;

import nye.bence.game.Board;
import nye.bence.user.Player;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

public class GameSaveManager {

    private static final String SAVE_DIR = "saves";

    static {
        // Create the saves directory if it doesn't exist
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
    }

    public static void saveGame(Board board, Player player) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);

            // Player element
            Element playerElement = doc.createElement("player");
            playerElement.setAttribute("name", player.getName());
            playerElement.setAttribute("wins", String.valueOf(player.getWins()));
            rootElement.appendChild(playerElement);

            // Board element
            Element boardElement = doc.createElement("board");
            rootElement.appendChild(boardElement);

            int[][] boardArray = board.getBoard();
            for (int i = 0; i < Board.SIZE_Y; i++) {
                for (int j = 0; j < Board.SIZE_X; j++) {
                    Element cell = doc.createElement("cell");
                    cell.setAttribute("x", String.valueOf(j));
                    cell.setAttribute("y", String.valueOf(i));
                    cell.setAttribute("value", String.valueOf(boardArray[i][j]));
                    boardElement.appendChild(cell);
                }
            }

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(SAVE_DIR, player.getName() + "_save.xml"));

            transformer.transform(source, result);

            System.out.println("Game saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Board loadGame(Player player) {
        try {
            File file = new File(SAVE_DIR, player.getName() + "_save.xml");
            if (!file.exists()) {
                System.out.println("No saved game found for this player.");
                return null;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            Board board = new Board();
            int[][] boardArray = board.getBoard();

            Element boardElement = (Element) doc.getElementsByTagName("board").item(0);
            for (int i = 0; i < boardElement.getElementsByTagName("cell").getLength(); i++) {
                Element cell = (Element) boardElement.getElementsByTagName("cell").item(i);
                int x = Integer.parseInt(cell.getAttribute("x"));
                int y = Integer.parseInt(cell.getAttribute("y"));
                int value = Integer.parseInt(cell.getAttribute("value"));
                boardArray[y][x] = value;
            }

            System.out.println("Game loaded successfully!");
            return board;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}