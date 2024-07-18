package me.mortaldev.jbtutorial.modules.book;

import me.mortaldev.jbtutorial.Main;
import me.mortaldev.jbtutorial.utils.YamlUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashSet;

public class BookManager {
  private static final String PATH = Main.getInstance().getDataFolder().getAbsolutePath() + "/books/";
  private HashSet<Book> books = new HashSet<>();

  private static class SingletonHolder {
    private static final BookManager bookManager = new BookManager();
  }

  public static BookManager getInstance() {
    return SingletonHolder.bookManager;
  }

  public void loadBooks(){
    books = new HashSet<>();
    File dirPath = new File(PATH);
    if (!dirPath.exists()) {
      dirPath.mkdirs();
      YamlUtil.loadResource(PATH, "exampleBook.yml");
    }
    File[] files = dirPath.listFiles();
    if (files == null) {
      Main.log("Loaded 0 book(s)");
      return;
    }
    for (File file : files) {
      if (file.isFile()) {
        books.add(BookReader.readBook(file));
      }
    }
    Main.log("Loaded " + books.size() + " book(s)");
  }

  public HashSet<Book> getBooks() {
    return books;
  }
}
