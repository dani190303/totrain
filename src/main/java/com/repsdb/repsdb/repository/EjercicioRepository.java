package com.repsdb.repsdb.repository;

import com.repsdb.repsdb.model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    Optional<Ejercicio> findByNombre(String nombre);
}
