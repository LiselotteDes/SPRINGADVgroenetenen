package be.vdab.groenetenen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import be.vdab.groenetenen.entities.Werknemer;

public interface WerknemerRepository extends JpaRepository<Werknemer, Long> {}
