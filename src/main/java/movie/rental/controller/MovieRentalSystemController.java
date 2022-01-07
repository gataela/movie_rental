package movie.rental.controller;

import movie.rental.dto.MovieInfoDto;
import movie.rental.dto.RentDto;
import movie.rental.entity.Movie;
import movie.rental.entity.Rental;
import movie.rental.entity.User;
import movie.rental.repository.MovieRepository;
import movie.rental.repository.RentRepository;
import movie.rental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
public class MovieRentalSystemController {
    private static final Logger logger = LoggerFactory.getLogger(MovieRentalSystemController.class);
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RentRepository rentRepository;


    public MovieRentalSystemController(MovieRepository movieRepository, UserRepository userRepository, RentRepository rentRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
    }


    @GetMapping("/")
    public String viewHomePage(Model model) {
        //	We can use this attribute "listMovies" to perform server-side rendering of the HTML with using Thymeleaf.
        //	We set all movies data to "listMovies"
        model.addAttribute("listMovies", movieRepository.findAll());
        //		shows the index.html template:
        return "index";
    }


    @GetMapping("/users")
    public String createUser(Model model) {
        //	We can use this attribute "listUsers" to perform server-side rendering of the HTML with using Thymeleaf.
        //	We set all user data to "listUsers"
        model.addAttribute("listUsers", userRepository.findAll());
        //		shows the users.html template:
        return "users";
    }


    // This means that this method will be executed when user sends GET Requests to "/showNewUserForm"
    // In our case,  "http://localhost:8080/showNewUserForm"
    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model) {
        User user = new User();

        // We can use this attribute "user" to perform server-side rendering of the HTML with using Thymeleaf.
        // We set user object as "user"
        model.addAttribute("user", user);

        //	shows the create_user.html template:
        return "create_user";
    }


    @RequestMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId).get();
        byte[] imageContent = movie.getImage();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }


    @GetMapping("/findMovie/{id}")
    @ResponseBody
    public Movie rentMovie(@PathVariable("id") Long movieId) {
        return movieRepository.findById(movieId).get();
    }

    @GetMapping("/rentals")
    public String showRentals(Model model) {
        //	We can use this attribute "listRentals" to perform server-side rendering of the HTML with using Thymeleaf.
        //	We set all rental data to "listRentals"
        model.addAttribute("listRentals", rentRepository.findAll());
        //		shows the rentals.html template
        return "rentals";
    }


    @GetMapping("/showNewMovieForm")
    public String showNewMovieForm(Model model) {
//        Movie movie = new Movie();
//        // We can use this attribute "movie" to perform server-side rendering of the HTML with using Thymeleaf.
//        // We set movie object as "movie"
//        model.addAttribute("movie", movie);


        MovieInfoDto movieInfo = new MovieInfoDto();
        model.addAttribute("movieInfo", movieInfo);
        //	shows the create_movie_info.html template:
        return "create_movie_info";
    }

    @RequestMapping("/saveMovieInfo")
    // This means that this method will be executed when user sends POST Requests to "/saveMovie"
    // In our case, "http://localhost:8080/saveMovie"
    public String saveMovieInfo(@ModelAttribute("movieInfo") MovieInfoDto movieInfo) throws IOException {
        //	@ModelAttribute  binds the object called "movie" of request body from the POST request into the movie parameter of the save() method.
        Movie movie = new Movie();
        movie.setImage(movieInfo.getImage().getBytes());
        movie.setPrice(movieInfo.getPrice());
        movie.setDuration(movieInfo.getDuration());
        movie.setLaunch_date(movieInfo.getLaunch_date());
        movie.setDescription(movieInfo.getDescription());
        movie.setType(movieInfo.getType());
        movie.setDirector(movieInfo.getDirector());
        movie.setName(movieInfo.getName());
        movieRepository.save(movie);
        // after save the user data to database, redirect to "/index"
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }


    @PostMapping("/save")
    // This means that this method will be executed when user sends POST Requests to "/saveUser"
    // In our case, "http://localhost:8080/saveUser"
    public String saveUser(@ModelAttribute("user") User user) {
        //	@ModelAttribute  binds the object called "user" of request body from the POST request into the user parameter of the save() method.
        userRepository.save(user);
        // after save the user data to database, redirect to "/users"
        return "redirect:/users";
    }


    //method update for find & edit item by item Id;
    @GetMapping("/edit")
    @ResponseBody
    public Optional<User> update(Long id) {
        return userRepository.findById(id);
    }

    //methode delete for removing item from Jpa data base. Item finding implements also by item id;
    @GetMapping("/delete")
    public String delete(Long id) {
        User user = userRepository.findById(id).get();
        List<Rental> rentals = rentRepository.findByUser(user);
        for(Rental rental: rentals){
            rentRepository.delete(rental);
        }
        userRepository.delete(user);
        logger.info("User has been removed. Users id: " + id);
        return "redirect:/users";
    }


    @RequestMapping(value = "/rentMovie")
    public String checkUser(RentDto rentDto) {
        User userFromDb = userRepository.findByEmail(rentDto.getEmail());
        if (userFromDb == null) {
            User newUser = new User();
            newUser.setFirstName(rentDto.getFirstName());
            newUser.setLastName(rentDto.getLastName());
            newUser.setEmail(rentDto.getEmail());
            logger.info("New user was created " + rentDto.getEmail());
            userRepository.save(newUser);
            userFromDb = newUser;
        }

        Optional<Movie> optionalMovie = movieRepository.findById(rentDto.getMovieId());
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            Rental rental = new Rental();
            rental.setMovie(movie);
            rental.setDays(1);
            rental.setUser(userFromDb);
            rentRepository.save(rental);
            return "redirect:/rentals";
        }
        logger.error("Movie does not exist! This case should never occur!");
        return "redirect:/";
    }


    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }
}
