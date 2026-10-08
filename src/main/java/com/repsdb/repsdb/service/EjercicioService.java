package com.repsdb.repsdb.service;

import com.repsdb.repsdb.dto.EjercicioDTO;
import java.util.List;

public interface EjercicioService {
    List<EjercicioDTO> findAll();
    EjercicioDTO findById(Integer id);
    EjercicioDTO create(EjercicioDTO dto);
    EjercicioDTO update(Integer id, EjercicioDTO dto);
    void delete(Integer id);
}
