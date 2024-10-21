package storage;

import author.Author;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import manga.Manga;

import java.lang.reflect.Type;

/**
 * This package private class defines a custom <code>Gson</code> serializer for the <code>Author</code> class because
 * the native one cannot handle the circular reference (bidirectional navigability) between an <code>Author</code>
 * and their <code>Manga</code>. Specifically, it manually sets the <code>author</code> of each <code>Manga</code>
 * instance that is deserialized.
 */
class AuthorDeserializer implements JsonDeserializer<Author> {
    @Override
    public Author deserialize(JsonElement json, Type typeOfAuthor, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String authorName = jsonObject.get("authorName").getAsString();
        Author author = new Author(authorName);

        JsonArray mangaArray = jsonObject.getAsJsonArray("mangaList");
        for (JsonElement mangaElement : mangaArray) {
            Manga manga = context.deserialize(mangaElement, Manga.class);
            manga.setAuthor(author);
            author.addManga(manga);
        }
        return author;
    }
}

