package manga;

import author.Author;
import storage.ExcludeInSerializationAnnotation;

import java.util.Objects;

public class Manga {
    private String mangaName;
    @ExcludeInSerializationAnnotation
    private Author author;
    private String deadline;

    public Manga(String mangaName, Author author) {
        this.mangaName = mangaName;
        this.author = author;
        this.deadline = "None";
    }

    //@@author xenthm
    public Manga(String mangaName, Author author, String deadline) {
        this.mangaName = mangaName;
        this.author = author;
        this.deadline = deadline;
    }

    //@@author
    public String getMangaName() {
        return mangaName;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDeadline() {
        return deadline;
    }

    public void addDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void deleteDeadline() {
        this.deadline = "None";
    }

    //@@author averageandyyy
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Manga manga = (Manga) o;

        return mangaName.equals(manga.getMangaName()) && author.equals(manga.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mangaName, author);
    }
}
