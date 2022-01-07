package movie.rental.repository;

import movie.rental.entity.Rental;
import movie.rental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByUser(User user);
}
