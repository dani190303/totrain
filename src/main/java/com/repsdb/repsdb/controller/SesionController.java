package com.repsdb.repsdb.controller;

import com.repsdb.repsdb.dto.ProgresoEjercicioDTO;
import com.repsdb.repsdb.dto.SesionDTO;
import com.repsdb.repsdb.dto.SesionRegistroDTO;
import com.repsdb.repsdb.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SesionController {

    private final SesionService sesionService;

    @Autowired
    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    @GetMapping
    public ResponseEntity<List<SesionDTO>> getAllSesiones() {
        return ResponseEntity.ok(sesionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionDTO> getSesionById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(sesionService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createSesion(@RequestBody SesionRegistroDTO dto) {
        try {
            SesionDTO creada = sesionService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSesion(@PathVariable Integer id, @RequestBody SesionRegistroDTO dto) {
        try {
            SesionDTO actualizada = sesionService.update(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSesion(@PathVariable Integer id) {
        try {
            sesionService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/ejercicios/{ejercicioId}")
    public ResponseEntity<?> saveSeriesForEjercicio(
            @PathVariable Integer id,
            @PathVariable Integer ejercicioId,
            @RequestBody List<Integer> repeticiones) {
        try {
            SesionDTO actualizada = sesionService.saveSeries(id, ejercicioId, repeticiones);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/ejercicio/{ejercicioId}/evolucion")
    public ResponseEntity<?> getEvolucionEjercicio(@PathVariable Integer ejercicioId) {
        try {
            List<ProgresoEjercicioDTO> evolucion = sesionService.getEvolucionEjercicio(ejercicioId);
            return ResponseEntity.ok(evolucion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
