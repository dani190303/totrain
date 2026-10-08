package com.repsdb.repsdb.dto;

import java.time.LocalDate;

public class ProgresoEjercicioDTO {
    private LocalDate fecha;
    private Integer totalRepeticiones;
    private Integer totalSeries;
    private Integer maxRepeticiones;

    public ProgresoEjercicioDTO(LocalDate fecha, Integer totalRepeticiones, Integer totalSeries, Integer maxRepeticiones) {
        this.fecha = fecha;
        this.totalRepeticiones = totalRepeticiones;
        this.totalSeries = totalSeries;
        this.maxRepeticiones = maxRepeticiones;
    }

    // Getters y Setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getTotalRepeticiones() {
        return totalRepeticiones;
    }

    public void setTotalRepeticiones(Integer totalRepeticiones) {
        this.totalRepeticiones = totalRepeticiones;
    }

    public Integer getTotalSeries() {
        return totalSeries;
    }

    public void setTotalSeries(Integer totalSeries) {
        this.totalSeries = totalSeries;
    }

    public Integer getMaxRepeticiones() {
        return maxRepeticiones;
    }

    public void setMaxRepeticiones(Integer maxRepeticiones) {
        this.maxRepeticiones = maxRepeticiones;
    }
}
