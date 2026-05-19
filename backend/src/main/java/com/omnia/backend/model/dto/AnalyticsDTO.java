package com.omnia.backend.model.dto;

import java.util.List;

/**
 * Respuesta del endpoint de analítica.
 * Agrupa en un único objeto todas las métricas calculadas del sistema.
 */
public record AnalyticsDTO(
        EmployeeStats employees,
        ProjectStats projects,
        List<NameCount> locationBreakdown,
        List<NameCount> projectLoad,
        List<NameCount> tenureBreakdown,
        List<NameCount> ageBreakdown,
        List<NameCount> hiresPerYear) {

    /** Indicadores de la plantilla: totales, perfil, formación y concentración de carga. */
    public record EmployeeStats(
            int total,
            int active,
            int inactive,
            int avgAge,
            double avgTenureYears,
            int universityPct,
            int multiProjectEmployees) {}

    /** Indicadores del catálogo de proyectos: estado, cobertura y duración media. */
    public record ProjectStats(
            int total,
            int active,
            int terminated,
            double avgTeamSize,
            int uncoveredProjects,
            int avgProjectDurationDays) {}

    /** Par nombre-cantidad reutilizable en cualquier gráfico de distribución. */
    public record NameCount(String name, int count) {}
}
