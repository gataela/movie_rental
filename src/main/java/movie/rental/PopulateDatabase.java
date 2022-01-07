package movie.rental;

import movie.rental.entity.Movie;
import movie.rental.entity.Rent;
import movie.rental.entity.User;
import movie.rental.repository.MovieRepository;
import movie.rental.repository.RentRepository;
import movie.rental.repository.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

@Component
public class PopulateDatabase {

    private final MovieRepository movieRepository;
    private final RentRepository rentRepository;
    private final UserRepository userRepository;

    public PopulateDatabase(MovieRepository movieRepository, RentRepository rentRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void createEntities() throws URISyntaxException, IOException {
        User user = new User();
        user.setEmail("oana.indrecan@gamil.com");
        user.setLastName("Indrecan");
        user.setFirstName("Oana");
        userRepository.save(user);

        Movie movie = new Movie();
        movie.setName("Spider-Man: No Way Home");
        movie.setDirector("Jon Watts");
        movie.setType("action, adventure, fantasy");
        movie.setDescription("With Spider-Man's identity now revealed, Peter asks Doctor Strange for help. " +
                "When a spell goes wrong, dangerous foes from other worlds start to appear, forcing Peter to discover what it truly means to be Spider-Man.");
        movie.setDuration(188);
        movie.setLaunch_date("2021-12-20");
        movie.setPrice(35);


        movie.setImage(Files.readAllBytes(new ClassPathResource("images/spiderman.jpeg").getFile().toPath()));
        movieRepository.save(movie);


        Rent rental = new Rent();
        rental.setDays(2);
        rental.setMovie(movie);
        rental.setUser(user);
        rentRepository.save(rental);

    }

}
