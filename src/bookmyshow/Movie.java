package bookmyshow;

public class Movie {
    private final String movieId;
    private final String title;
    private final int durationMinutes;
    private final String genre;

    public Movie(String movieId, String title, int durationMinutes, String genre) {
        this.movieId = movieId;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
    }

    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getGenre() { return genre; }

    @Override
    public String toString() { return title + " (" + genre + ", " + durationMinutes + "min)"; }
}

