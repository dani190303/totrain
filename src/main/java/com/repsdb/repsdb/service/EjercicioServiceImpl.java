package com.repsdb.repsdb.service;

import com.repsdb.repsdb.dto.EjercicioDTO;
import com.repsdb.repsdb.model.Ejercicio;
import com.repsdb.repsdb.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    @Autowired
    public EjercicioServiceImpl(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjercicioDTO> findAll() {
        return ejercicioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EjercicioDTO findById(Integer id) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con id: " + id));
        return convertToDTO(ejercicio);
    }

    @Override
    public EjercicioDTO create(EjercicioDTO dto) {
        if (ejercicioRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un ejercicio con el nombre: " + dto.getNombre());
        }
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre(dto.getNombre());
        ejercicio.setGrupoMuscular(dto.getGrupoMuscular());
        ejercicio.setDescripcion(dto.getDescripcion());
        ejercicio.setCreadoEn(LocalDateTime.now());
        
        Ejercicio guardado = ejercicioRepository.save(ejercicio);
        return convertToDTO(guardado);
    }

    @Override
    public EjercicioDTO update(Integer id, EjercicioDTO dto) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con id: " + id));
        
        ejercicioRepository.findByNombre(dto.getNombre())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException("Ya existe otro ejercicio con el nombre: " + dto.getNombre());
                    }
                });

        ejercicio.setNombre(dto.getNombre());
        ejercicio.setGrupoMuscular(dto.getGrupoMuscular());
        ejercicio.setDescripcion(dto.getDescripcion());

        Ejercicio guardado = ejercicioRepository.save(ejercicio);
        return convertToDTO(guardado);
    }

    @Override
    public void delete(Integer id) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con id: " + id));
        ejercicioRepository.delete(ejercicio);
    }

    private EjercicioDTO convertToDTO(Ejercicio ejercicio) {
        EjercicioDTO dto = new EjercicioDTO();
        dto.setId(ejercicio.getId());
        dto.setNombre(ejercicio.getNombre());
        dto.setGrupoMuscular(ejercicio.getGrupoMuscular());
        dto.setDescripcion(ejercicio.getDescripcion());
        dto.setCreadoEn(ejercicio.getCreadoEn());
        return dto;
    }
}
