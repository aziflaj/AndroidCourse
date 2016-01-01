package com.aziflaj.hackernewsreader;

public class Utils {
    public static final String BASE_URL = "https://hacker-news.firebaseio.com/v0";
    public static final String ITEM_FORMAT = "item/%d.json";
    public static final String TOP_STORIES = "topstories.json";

    public static final String DB_NAME = "news";
    public static final String DROP_TABLES_SQL = "DROP TABLE IF EXISTS news;";
    public static final String CREATE_TABLES_SQL = "CREATE TABLE IF NOT EXISTS news ( " +
            " id INT, title TEXT, url TEXT);";
    public static final String INSERT_STATEMENT = "INSERT INTO news (id, title, url) VALUES (?, ?, ?);";
    public static final String SELECT_SQL = "SELECT id as _id, title, url FROM news;";
}
