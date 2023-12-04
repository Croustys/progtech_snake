package db;

/**
 * Record representing the score of a player.
 * @param name name of the player
 * @param score score of the player
 */
public record Highscore(String name, int score) {}