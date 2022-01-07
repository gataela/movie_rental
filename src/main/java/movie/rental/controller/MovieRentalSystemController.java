package movie.rental.controller;

import movie.rental.entity.Movie;
import movie.rental.entity.User;
import movie.rental.repository.MovieRepository;
import movie.rental.repository.RentRepository;
import movie.rental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

//- when you want to use swagger - check what happened with swagger
//@RestController
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
        //		shows the users_old.html template:
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





    @RequestMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId).get();
        byte[] imageContent = movie.getImage();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }


    @GetMapping("/rentMovie/{id}")
    @ResponseBody
    public Movie rentMovie(@PathVariable("id") Long movieId) {
        //TODO - get somehow the user and insert new entry in DB
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
        Movie movie = new Movie();
        // We can use this attribute "movie" to perform server-side rendering of the HTML with using Thymeleaf.
        // We set movie object as "movie"
        model.addAttribute("movie", movie);
        //	shows the create_movie.html template:
        return "create_movie";
    }

    @PostMapping("/saveMovie")
    // This means that this method will be executed when user sends POST Requests to "/saveUser"
    // In our case, "http://localhost:8080/saveUser"
    public String saveMovie(@ModelAttribute("movie") Movie movie) {
        //	@ModelAttribute  binds the object called "movie" of request body from the POST request into the movie parameter of the save() method.
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


//    //method for users list viewing + pagination. Default is 6 items per page;
//    @GetMapping("/users")
//    public String showListOfUsers(Model model, @RequestParam(defaultValue = "0") int page) {
//        model.addAttribute("data", userRepository.findAll());
//        model.addAttribute("currentPage", page);
//        return "users";
//    }


    //method update for find & edit item by item Id;
    @GetMapping("/edit")
    @ResponseBody
    public Optional<User> update(Long id) {
        return userRepository.findById(id);
    }

    //methode delete for removing item from Jpa data base. Item finding implements also by item id;
    @GetMapping("/delete")
    public String delete(Long id) {
        userRepository.deleteById(id);
        logger.info("User has been removed. Users id: " + id);
        return "redirect:/users";
    }


    @RequestMapping(value = "/checkUser", method = RequestMethod.POST)
    public String checkUser(String movieId, User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());
        if(userFromDb == null){
            logger.info("New user was created " + user.getEmail());
            userRepository.save(user);
        }

        movieRepository.findById(Long.parseLong(movieId));
        return "redirect:/";
    }

}
