package storage;

import author.Author;
import author.AuthorList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.logging.Logger;

//@@author xenthm

/**
 * Helper singleton class that deals with storage of <code>AuthorList</code> data into a <code>.json</code> file. Uses
 * the <code>Gson</code> third-party library to serialize/deserialize the <code>AuthorList</code> into
 * <code>.json</code>.
 */
public class Storage {
    // ensure that the desired output file is of type .json
    public static final String DATA_PATH = "data/catalog.json";

    // The weird syntax in the following line is required because of type erasure.
    // It allows us to capture the generic type information at runtime before it gets erased.
    private static final Type AUTHOR_LIST_TYPE = new TypeToken<AuthorList>() {
    }.getType();

    private static Logger logger;
    private static Storage storage = null;
    private static File dataFile;
    private static Gson gson;

    /**
     * Private default constructor for the <code>Storage</code> singleton. Sets up the logger, data storage file, and
     * <code>Gson</code> object.
     * <p>
     * Importantly, it makes use of the <code>ExcludeInSerializationAnnotation</code> custom exclusion strategy and
     * <code>@ExcludeInSerialization</code> annotations within the data classes to specify which attributes to
     * serialize into <code>JSON</code>. This prevents infinite recursion due to the circular reference (bidirectional
     * navigability) between an <code>Author</code> and their <code>Manga</code>.
     * </p>
     */
    private Storage() {
        assert DATA_PATH.endsWith(".json") : "data file path should be of type .json";
        logger = Logger.getLogger(this.getClass().getName());
        dataFile = new File(DATA_PATH);
        logger.info("Data file path initialized to: " + dataFile.getAbsolutePath());
        gson = new GsonBuilder()
                .setExclusionStrategies(new ExcludeInSerializationAnnotationExclusionStrategy())
                .setPrettyPrinting()
                .registerTypeAdapter(AUTHOR_LIST_TYPE, new AuthorListDeserializer())
                .registerTypeAdapter(Author.class, new AuthorDeserializer())
                .setStrictness(Strictness.LENIENT)
                .create();
    }

    public Gson getGson() {
        return gson;
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) {
        Storage.dataFile = dataFile;
        logger.info("Data file path changed to: " + dataFile.getAbsolutePath());
    }

    /**
     * Main method for initializing and referring to the <code>Storage</code> singleton.
     *
     * @return new <code>Storage</code> object if not already initialized, existing <code>Storage</code> object if
     * already initialized
     */
    public static Storage getInstance() {
        if (storage == null) {
            storage = new Storage();
            logger.info("Singleton Storage first initialized");
        }
        return storage;
    }

    /**
     * Deserializes the <code>JSON</code> contents of <code>dataFile</code> with <code>Gson</code>.
     *
     * @return <code>AuthorList</code> obtained from <code>.json</code> data storage file. If an
     * <code>IOException</code> was encountered, return <code>null</code> instead.
     */
    public AuthorList readAuthorListFromDataFile() {
        assert dataFile != null : "dataFile cannot be null";
        try (FileReader reader = new FileReader(dataFile)) {
            AuthorList authorList = gson.fromJson(reader, AUTHOR_LIST_TYPE);
            logger.info("Data restored");
            System.out.println("Data restored!");
            return authorList;
        } catch (IOException e) {
            logger.warning("Problems accessing file: " + e.getMessage());
            System.out.println("Problems accessing file, data was not restored! Continuing with an empty list.");
        } catch (JsonSyntaxException e) {
            logger.warning("JSON from file is malformed: " + e.getMessage());
            System.out.println(
                    "JSON from file is malformed, data was not restored! Continuing with an empty list."
            );
            System.out.println(
                    "If you want to try and manually fix this, CTRL-C out of the program and check catalog.json!"
            );
        } catch (JsonParseException e) {
            logger.warning(e.getMessage());
            System.out.println(
                    "Corrupted AuthorList object. Continuing with an empty list."
            );
        }
        return null;
    }

    /**
     * Checks if the data storage file <code>dataFile</code> can be found at its specified location. If not, creates the
     * corresponding directories and file, logging the process.
     *
     * @return <code>true</code> if there were issues creating the data file, <code>false</code> otherwise
     */
    private boolean createFileIfNeeded() {
        assert dataFile != null : "dataFile cannot be null";
        try {
            // check with short-circuiting if path has a parent directory,
            // then if the directories were created with mkdirs()
            if (dataFile.getParentFile() != null && dataFile.getParentFile().mkdirs()) {
                logger.info("Directories not found; created them");
            }

            // check if data file was created
            if (dataFile.createNewFile()) {
                logger.info("Data file not found; created it");
            }

            return false;
        } catch (IOException | SecurityException e) {
            logger.warning("Problems creating data file, data will not be saved!" + e.getMessage());
            System.out.println("Problems creating data file, data will not be saved!");
            return true;
        }
    }

    /**
     * Serializes the contents of the provided <code>authorList</code> into <code>JSON</code> and writes it into
     * <code>dataFile</code> with <code>Gson</code>.
     *
     * @param authorList <code>AuthorList</code> whose contents is to be saved
     */
    public void saveAuthorListToDataFile(AuthorList authorList) {
        assert authorList != null : "authorList cannot be null";
        boolean hasIssuesWithDataFile = createFileIfNeeded();
        if (!hasIssuesWithDataFile) {
            try (FileWriter writer = new FileWriter(dataFile)) {
                gson.toJson(authorList, writer);
                logger.info("Data saved");
            } catch (IOException e) {
                logger.warning("Problems saving file, data will not be saved!" + e.getMessage());
                System.out.println("Problems saving file, data will not be saved!");
            }
        }
    }
}
