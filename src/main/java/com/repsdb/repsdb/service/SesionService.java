package com.repsdb.repsdb.service;

import com.repsdb.repsdb.dto.ProgresoEjercicioDTO;
import com.repsdb.repsdb.dto.SesionDTO;
import com.repsdb.repsdb.dto.SesionRegistroDTO;

import java.util.List;

public interface SesionService {
    List<SesionDTO> findAll();
    SesionDTO findById(Integer id);
    SesionDTO create(SesionRegistroDTO dto);
    SesionDTO update(Integer id, SesionRegistroDTO dto);
    void delete(Integer id);
    SesionDTO saveSeries(Integer id, Integer ejercicioId, List<Integer> repeticiones);
    List<ProgresoEjercicioDTO> getEvolucionEjercicio(Integer ejercicioId);
}
