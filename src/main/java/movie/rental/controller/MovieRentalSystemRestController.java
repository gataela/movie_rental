package movie.rental.controller;

import movie.rental.entity.Movie;
import movie.rental.entity.User;
import movie.rental.repository.MovieRepository;
import movie.rental.repository.RentRepository;
import movie.rental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//- when you want to use swagger - check what happened with swagger
//@RestController
//http://localhost:8089/swagger-ui/index.html#/
@RestController
public class MovieRentalSystemRestController {

    private static final Logger logger = LoggerFactory.getLogger(MovieRentalSystemController.class);
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RentRepository rentRepository;


    public MovieRentalSystemRestController(MovieRepository movieRepository, UserRepository userRepository, RentRepository rentRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
    }

    //REST ENDPOINTURI

    @PostMapping("/create-movie")
    public ResponseEntity<String> createMovie(@RequestParam("name") String name,
                                              @RequestParam("description") String description,
                                              @RequestParam("launch_date") String date,
                                              @RequestParam("director") String director,
                                              @RequestParam("duration") int duration,
                                              @RequestParam("type") String type,
                                              @RequestParam("price") double price,
                                              @RequestParam("image") MultipartFile image
    ) {

        try {
            Movie movie = new Movie();
            movie.setDescription(description);
            movie.setImage(image.getBytes());
            movie.setName(name);
            movie.setLaunch_date(date);
            movie.setDirector(director);
            movie.setType(type);
            movie.setPrice(price);
            movie.setDuration(duration);

            movieRepository.save(movie);

            return new ResponseEntity<>("Movie was successfully inserted.", new HttpHeaders(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("Could not insert the movie.", new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestParam("firstName") String firstName,
                                             @RequestParam("lastName") String lastName,
                                             @RequestParam("email") String email) {

        try {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            userRepository.save(user);

            return new ResponseEntity<>("User was successfully inserted.", new HttpHeaders(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>("Could not insert the user.", new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    // POST
//    public ResponseEntity<String> rentMovie() {
    // User - username
    // Movie - movie name
    // no days
    // FindByName ? user in db, userRepository
    // user exist -> create user
    // findByName - movie in db, movieRepository
    // movie exists? no -> return, Sorry, movie does not exist
    // yes -> save - rent -> set user, set movie, set days
    // RENT -> new col date -> nu se trimite ca parameru, new ZoneDateTime -> rent movie
    //???

//        return null;
//    }
//
//    @GetMapping(path = "/image-by-name/{name:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] imageByPath(@PathVariable(name = "name") String name) {
//
//    }

    //@GetMapping
    //getMovie (name sau id)
    //getUser (name sau id)
    //getRent (username and movie name) -> list
    //getAllMovies
    //getAllUsers
    //getAllRents -> return -> Ale -> rent - > Movie1 -> on 2021
    //                         Ale -> rent -> Movie2 -> on 2022
    //                        Oana -> rent -> Movie1 -> 2022


    //getRentalsByUser (Param username) -> all movies rent by this user
    //getRentalByMovies (Param filename) -> This movie was rented by who?


    //Delete -> delete, movie, delete user -> cascade? rent
}
