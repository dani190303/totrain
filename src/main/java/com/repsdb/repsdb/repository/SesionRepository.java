package com.repsdb.repsdb.repository;

import com.repsdb.repsdb.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    List<Sesion> findAllByOrderByFechaDesc();
}
