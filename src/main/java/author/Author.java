package author;

import manga.Manga;
import manga.MangaList;

import java.util.Objects;

public class Author {
    private String authorName;
    private MangaList mangaList;

    public Author(String authorName) {
        this.authorName = authorName;
        this.mangaList = new MangaList();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public MangaList getMangaList() {
        return mangaList;
    }

    public boolean hasManga(Manga manga) {
        assert manga != null : "manga must not be null";
        return mangaList.contains(manga);
    }

    public void addManga(Manga manga) {
        assert manga != null : "manga must not be null";
        mangaList.add(manga);
    }

    public void deleteManga(Manga manga) {
        assert manga != null : "manga must not be null";
        mangaList.remove(manga);
    }

    public Manga getManga(String name) {
        assert !name.isEmpty() : "manga name must not be empty";
        for (Manga manga : mangaList) {
            if (manga.getMangaName().equals(name)) {
                return manga;
            }
        }
        return null;
    }

    //@@author averageandyyy
    @Override
    public boolean equals(Object o) {
        // Check if they are the same object in memory
        if (this == o) {
            return true;
        }

        // Check for the correct type
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // Cast the incoming object as an Author
        Author otherAuthor = (Author) o;

        // Two authors are considered the same if they have the same name
        return authorName.equals(otherAuthor.getAuthorName());
    }

    //@@author averageandyyy
    @Override
    public int hashCode() {
        return Objects.hash(authorName);
    }

    //@@author xenthm
    public void printMangaList() {
        mangaList.print();
    }
}
