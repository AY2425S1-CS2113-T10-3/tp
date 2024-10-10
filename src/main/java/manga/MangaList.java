package manga;

import java.util.ArrayList;

public class MangaList extends ArrayList<Manga> {
    public void print() {
        for (int i = 0; i < size(); i++) {
            System.out.println((i + 1) + ". " + get(i).getMangaName() + " | Deadline: " + get(i).getDeadline());
        }
    }
}
