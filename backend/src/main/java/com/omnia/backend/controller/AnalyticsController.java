package com.omnia.backend.controller;

import com.omnia.backend.model.dto.AnalyticsDTO;
import com.omnia.backend.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de analítica.
 * Devuelve todas las métricas del sistema en una sola llamada para minimizar
 * la latencia del cliente y mantener la lógica de cálculo en el servidor.
 */
@RestController
@RequestMapping("/analytics")
@Tag(name = "Analítica")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Devuelve el conjunto completo de métricas calculadas sobre los datos reales.
     *
     * @return {@link AnalyticsDTO} con indicadores de plantilla, proyectos, sedes y carga.
     */
    @GetMapping
    @Operation(summary = "Métricas globales del sistema")
    public AnalyticsDTO getAnalytics() {
        return analyticsService.compute();
    }
}
