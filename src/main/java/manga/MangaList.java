package manga;

import java.util.ArrayList;

public class MangaList extends ArrayList<Manga> {
    public void print() {
        assert size() >= 0 : "MangaList.size() must be non-negative";

        for (int i = 0; i < size(); i++) {
            System.out.println((i + 1) + ". " + get(i).getMangaName() + " | Deadline: " + get(i).getDeadline());
        }
    }
}
