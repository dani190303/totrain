package com.repsdb.repsdb.service;

import com.repsdb.repsdb.dto.ProgresoEjercicioDTO;
import com.repsdb.repsdb.dto.SerieDTO;
import com.repsdb.repsdb.dto.SesionDTO;
import com.repsdb.repsdb.dto.SesionRegistroDTO;
import com.repsdb.repsdb.model.Ejercicio;
import com.repsdb.repsdb.model.Serie;
import com.repsdb.repsdb.model.Sesion;
import com.repsdb.repsdb.repository.EjercicioRepository;
import com.repsdb.repsdb.repository.SerieRepository;
import com.repsdb.repsdb.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SesionServiceImpl implements SesionService {

    private final SesionRepository sesionRepository;
    private final EjercicioRepository ejercicioRepository;
    private final SerieRepository serieRepository;

    @Autowired
    public SesionServiceImpl(SesionRepository sesionRepository, 
                             EjercicioRepository ejercicioRepository, 
                             SerieRepository serieRepository) {
        this.sesionRepository = sesionRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.serieRepository = serieRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionDTO> findAll() {
        return sesionRepository.findAllByOrderByFechaDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SesionDTO findById(Integer id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada con id: " + id));
        return convertToDTO(sesion);
    }

    @Override
    public SesionDTO create(SesionRegistroDTO dto) {
        Sesion sesion = new Sesion();
        sesion.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        sesion.setNotas(dto.getNotas());
        sesion.setCreadoEn(LocalDateTime.now());
        sesion.setSeries(new ArrayList<>());

        Sesion guardada = sesionRepository.save(sesion);
        return convertToDTO(guardada);
    }

    @Override
    public SesionDTO update(Integer id, SesionRegistroDTO dto) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada con id: " + id));

        if (dto.getFecha() != null) {
            sesion.setFecha(dto.getFecha());
        }
        sesion.setNotas(dto.getNotas());

        Sesion guardada = sesionRepository.save(sesion);
        return convertToDTO(guardada);
    }

    @Override
    public void delete(Integer id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada con id: " + id));
        sesionRepository.delete(sesion);
    }

    @Override
    public SesionDTO saveSeries(Integer id, Integer ejercicioId, List<Integer> repeticiones) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada con id: " + id));

        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con id: " + ejercicioId));

        // Inicializar la lista si es nula
        if (sesion.getSeries() == null) {
            sesion.setSeries(new ArrayList<>());
        }

        // Eliminar series previas de este ejercicio en esta sesión
        sesion.getSeries().removeIf(s -> s.getEjercicio().getId().equals(ejercicioId));
        serieRepository.deleteBySesionIdAndEjercicioId(id, ejercicioId);

        // Añadir las nuevas series
        if (repeticiones != null) {
            for (int i = 0; i < repeticiones.size(); i++) {
                Serie nuevaSerie = new Serie();
                nuevaSerie.setSesion(sesion);
                nuevaSerie.setEjercicio(ejercicio);
                nuevaSerie.setRepeticiones(repeticiones.get(i));
                nuevaSerie.setOrden(i + 1);
                sesion.getSeries().add(nuevaSerie);
            }
        }

        Sesion guardada = sesionRepository.save(sesion);
        return convertToDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoEjercicioDTO> getEvolucionEjercicio(Integer ejercicioId) {
        // Verificar que el ejercicio existe
        if (!ejercicioRepository.existsById(ejercicioId)) {
            throw new RuntimeException("Ejercicio no encontrado con id: " + ejercicioId);
        }

        List<Serie> series = serieRepository.findByEjercicioIdOrderBySesionFechaAsc(ejercicioId);

        // Agrupar por fecha de sesión
        Map<LocalDate, List<Serie>> agrupadoPorFecha = series.stream()
                .collect(Collectors.groupingBy(s -> s.getSesion().getFecha()));

        // Crear DTO de progreso y ordenar cronológicamente
        return agrupadoPorFecha.entrySet().stream()
                .map(entry -> {
                    LocalDate fecha = entry.getKey();
                    List<Serie> seriesDelDia = entry.getValue();
                    int totalReps = seriesDelDia.stream().mapToInt(Serie::getRepeticiones).sum();
                    int totalSets = seriesDelDia.size();
                    int maxReps = seriesDelDia.stream().mapToInt(Serie::getRepeticiones).max().orElse(0);
                    return new ProgresoEjercicioDTO(fecha, totalReps, totalSets, maxReps);
                })
                .sorted((p1, p2) -> p1.getFecha().compareTo(p2.getFecha()))
                .collect(Collectors.toList());
    }

    private SesionDTO convertToDTO(Sesion sesion) {
        SesionDTO dto = new SesionDTO();
        dto.setId(sesion.getId());
        dto.setFecha(sesion.getFecha());
        dto.setNotas(sesion.getNotas());
        dto.setCreadoEn(sesion.getCreadoEn());

        if (sesion.getSeries() != null) {
            List<SerieDTO> seriesDTO = sesion.getSeries().stream()
                    .map(this::convertSerieToDTO)
                    .sorted((s1, s2) -> s1.getOrden().compareTo(s2.getOrden()))
                    .collect(Collectors.toList());
            dto.setSeries(seriesDTO);
        } else {
            dto.setSeries(new ArrayList<>());
        }

        return dto;
    }

    private SerieDTO convertSerieToDTO(Serie serie) {
        SerieDTO dto = new SerieDTO();
        dto.setId(serie.getId());
        dto.setEjercicioId(serie.getEjercicio().getId());
        dto.setEjercicioNombre(serie.getEjercicio().getNombre());
        dto.setRepeticiones(serie.getRepeticiones());
        dto.setOrden(serie.getOrden());
        return dto;
    }
}
