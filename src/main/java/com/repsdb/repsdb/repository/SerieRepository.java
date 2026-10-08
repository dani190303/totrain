package com.repsdb.repsdb.repository;

import com.repsdb.repsdb.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {
    List<Serie> findByEjercicioIdOrderBySesionFechaAsc(Integer ejercicioId);
    void deleteBySesionIdAndEjercicioId(Integer sesionId, Integer ejercicioId);
}
