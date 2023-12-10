package db;

import java.sql.*;
import java.util.ArrayList;

/** Singleton. Class responsible for connecting to the database and making database operations. */
public class Database {
    /** Singleton instance of the class. */
    private static Database instance;

    /** Database connection. */
    private final Connection connection;

    /**
     * Constructor.
     * @throws SQLException if a database access error occurs
     */
    private Database() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/snake?useSSL=false", "akos", "2002");
        this.connection.setAutoCommit(false);
    }

    /**
     * @return singleton instance of the class
     * @throws SQLException if a database access error occurs
     */
    public static Database instance() throws SQLException {
        if (Database.instance == null) {
            Database.instance = new Database();
        }
        return instance;
    }

    /**
     * Inserts the score of a player into the database. If the player already has a record increments their score by one.
     * @param playerName name of the player
     */
    public void insertOrUpdateScore(String playerName, final int hs) throws SQLException {
            ResultSet result = selectUserByName(playerName);

            if (result.next()) {
                int id = result.getInt(1);
                int existingScore = result.getInt(2);

                if (hs > existingScore) {
                    updateScore(id, hs);
                }
            } else {
                insert(playerName, hs);
            }

            this.connection.commit();
    }

    /**
     * @return ArrayList created from records that have a score value that is in the best 10 scores in the table.
     * @throws SQLException if a database access error occurs
     */
    public ArrayList<Highscore> getTopScores() throws SQLException {
        ArrayList<Highscore> topScores = new ArrayList<>();

        PreparedStatement selectTop = this.connection.prepareStatement("SELECT name, score FROM highscores ORDER BY score DESC LIMIT 10");
        ResultSet resultSet = selectTop.executeQuery();

        while (resultSet.next()) {
            topScores.add(new Highscore(resultSet.getString(1), resultSet.getInt(2)));
        }

        return topScores;
    }

    /**
     * @param id id of the already existing player
     * @param hs new highscore to be updated for the user
     * @throws SQLException if updating the database caught an error
     */
    private void updateScore(final int id, final int hs) throws SQLException {
        PreparedStatement updateRecord = this.connection.prepareStatement("UPDATE highscores SET score = ? WHERE id = ?");
        updateRecord.setInt(1, hs);
        updateRecord.setInt(2, id);
        updateRecord.executeUpdate();
    }

    /**
     * @param name name of the player
     * @return database rows that match the id of the name
     * @throws SQLException if selecting rows from the database catches an error
     */
    private ResultSet selectUserByName(final String name) throws SQLException {
        PreparedStatement selectPlayerID = this.connection.prepareStatement("SELECT id, score FROM highscores WHERE name = ?");
        selectPlayerID.setString(1, name);

        return selectPlayerID.executeQuery();
    }

    /**
     * @param name name of the player
     * @param hs highscore of the player
     * @throws SQLException throws if database couldn't insert record
     */
    private void insert(final String name, final int hs) throws SQLException {
        PreparedStatement insertRecord = this.connection.prepareStatement("INSERT INTO highscores (name, score) VALUES (?, ?)");
        insertRecord.setString(1, name);
        insertRecord.setInt(2, hs);

        insertRecord.executeUpdate();
    }
}