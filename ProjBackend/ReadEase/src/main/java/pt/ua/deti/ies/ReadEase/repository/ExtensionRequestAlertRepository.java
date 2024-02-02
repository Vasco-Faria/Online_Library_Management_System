package pt.ua.deti.ies.ReadEase.repository;

import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.BookReserves;
import pt.ua.deti.ies.ReadEase.model.ExtensionRequestAlert;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ExtensionRequestAlertRepository extends JpaRepository<ExtensionRequestAlert, Long> {

    boolean existsByReservation(BookReserves reservation); 
    
}
