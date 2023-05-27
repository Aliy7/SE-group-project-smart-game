import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Ben Rees (2015742)
 * @version 1.0
 * This class deals with the Message of the day API. It gets the puzzle, deciphers it, and then gets the Message
 * of the day. To send the GET request I have used Java's 'HttpClient' library allowing me to send http GET requests
 * and easily access the results.
 * To do this I had to research and used these sources to help me:
 *  - https://docs.oracle.com/en/java/javase/12/docs/api/java.net.http/java/net/http/HttpClient.html (Oracle docs)
 *  - https://www.youtube.com/watch?v=5MmlRZZxTqk (Dan Vega, YouTube)
 *  - https://www.twilio.com/blog/5-ways-to-make-http-requests-in-java (Twilio blog, Matthew Gilliard)
 */
public class MessageOfTheDay {

    private static final String PUZZLE_URL = "http://cswebcat.swansea.ac.uk/puzzle"; // URL to get puzzle
    private HttpClient client = HttpClient.newHttpClient(); // Client to send GET/POST requests
    private String puzzleString;
    private String decipheredPuzzleString = ""; // Stores the deciphered puzzle string
    private char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; // So we can shift characters
    private String messageOfTheDay; // Stores the final result

    /**
     * The constructor gets the initial puzzle String using a HTTPS GET request,
     * extracts the result from the JSON response and passes the result to another
     * method for the puzzle to be solved.
     *
     * @throws IOException This is for the 'send' method related to the https module in Java
     * @throws InterruptedException This is for the 'send' method related to the https module in Java
     */
    public MessageOfTheDay() throws IOException, InterruptedException {
        HttpRequest getPuzzle = HttpRequest.newBuilder() // Create a HttpRequest
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(PUZZLE_URL))
                .build();

        HttpResponse<String> puzzleJSON = client.send(getPuzzle, HttpResponse.BodyHandlers.ofString()); // Send request
        puzzleString = puzzleJSON.body(); // Extract result from response
        decipheredPuzzleString = decipherPuzzle(); // Decipher the response
    }

    /**
     * This method gets the message of the day by constructing the final URL,
     * carefully following the instructions from the API documentation.
     * It does this by sending a GET request to the constructed URL.
     * @return Final message of the day String
     * @throws IOException This is for the 'send' method related to the https module in Java
     * @throws InterruptedException This is for the 'send' method related to the https module in Java
     */
    public String getMessageOfTheDay() throws IOException, InterruptedException {
        String messageOfTheDayURL = "http://cswebcat.swansea.ac.uk/message?solution=" + decipheredPuzzleString;
        HttpRequest getMOTD = HttpRequest.newBuilder() // Create HttpRequest
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(messageOfTheDayURL))
                .build();
        HttpResponse<String> messageJSON = client.send(getMOTD, HttpResponse.BodyHandlers.ofString()); // Send request
        messageOfTheDay = messageJSON.body(); // Extract result from response
        return messageOfTheDay;
    }

    /**
     * This method deciphers the puzzle String. Based on the API documentation, we know that the first character
     * is shifted backwards and the second character is shifted forwards and this pattern continues. Knowing this we
     * can easily determine which characters need to be shifted back and forward, this method determines this and
     * then deals with the actual passing off to shifting methods. It also constructs the other prerequisites that the
     * String requires, such as the number appended to the front and 'CS-230' appended to the back.
     * @return The complete deciphered puzzle String
     */
    private String decipherPuzzle() {

        int counter = 0; // Set a counter so we can keep track of the index from 1 instead of 0.
        for (int i = 0; i < puzzleString.length(); i++) {
            counter++;
            // If the counter is even, we shift forward. If it is odd, we shift backwards:
            if (counter % 2 == 0) {
                // Shift character at 'i' forward by 'counter':
                decipheredPuzzleString = decipheredPuzzleString + shiftForward(puzzleString.charAt(i), counter);
            } else {
                // Shift character at 'i' backwards by 'counter':
                decipheredPuzzleString = decipheredPuzzleString + shiftBackward(puzzleString.charAt(i), counter);
            }
        }
        decipheredPuzzleString = decipheredPuzzleString + "CS-230"; // Append 'CS-230' to the end of the String
        decipheredPuzzleString = decipheredPuzzleString.length() + decipheredPuzzleString; // Add length to start
        return decipheredPuzzleString;
    }

    /**
     * This method is responsible for shifting a character forward
     * helping us decipher the puzzle String. It loops through the alphabet
     * and finds the character given and then returns the index + x modulo'd to
     * 26, this allows us to loop back to 'A' if we reach 'Z'.
     * @param c The character that needs to be shifted
     * @param x How much the character should be shifted by
     * @return The new character once shifting has been complete
     */
    private char shiftForward(char c, int x) {
        int indexOfC = -1; // This will be used to store index of the given character
        // Loop through the entire alphabet:
        for (int i = 0; i < alphabet.length; i++) {
            // if current letter equals the one we need to shift, we've found the index:
            if (alphabet[i] == c) {
                indexOfC = i;
            }
        }
        return alphabet[(indexOfC + x) % alphabet.length]; // return the index + x modulo'd by 26 (compensates for IOB)
    }

    /**
     * This method is responsible for shifting a character backwards
     * helping us decipher the puzzle String. It loops through the alphabet
     * and finds the character given. But now we need to do a different calculation on the
     * return as this is a minus operation. We use the floorMod() method from the Math
     * library to make sure that the index loops around from 'A' to 'Z'
     * @param c The character that needs to be shifted
     * @param x How much the character should be shifted by
     * @return The new character once shifting has been complete
     */
    private char shiftBackward(char c, int x) {
        int indexOfC = -1; // This will be used to store index of the given character
        // Loop through the entire alphabet:
        for (int i = 0; i < alphabet.length; i++) {
            // if current letter equals the one we need to shift, we've found the index:
            if (alphabet[i] == c) {
                indexOfC = i;
            }
        }
        // Do the floorMod operation to index - x and 26, this ensures we don't get IOB error:
        return alphabet[Math.floorMod((indexOfC - x), alphabet.length)];
    }
}
