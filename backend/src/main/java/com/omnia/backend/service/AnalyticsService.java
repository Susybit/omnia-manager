package com.omnia.backend.service;

import com.omnia.backend.model.dto.AnalyticsDTO;
import com.omnia.backend.model.dto.AnalyticsDTO.EmployeeStats;
import com.omnia.backend.model.dto.AnalyticsDTO.NameCount;
import com.omnia.backend.model.dto.AnalyticsDTO.ProjectStats;
import com.omnia.backend.model.dto.AssignmentDTO;
import com.omnia.backend.model.entities.Employee;
import com.omnia.backend.model.entities.Project;
import com.omnia.backend.repository.EmployeeProjectRepository;
import com.omnia.backend.repository.EmployeeRepository;
import com.omnia.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Calcula las métricas de analítica a partir de los datos reales de la base de datos.
 * Cada tabla se consulta una sola vez y los resultados se reutilizan entre bloques.
 */
@Service
public class AnalyticsService {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository assignmentRepository;

    public AnalyticsService(EmployeeRepository employeeRepository,
                            ProjectRepository projectRepository,
                            EmployeeProjectRepository assignmentRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Construye el DTO completo de analítica realizando una única lectura por tabla.
     * Las asignaciones se cargan una vez y se reutilizan en todos los cálculos que las necesitan.
     *
     * @return {@link AnalyticsDTO} con todos los indicadores calculados.
     */
    @Transactional(readOnly = true)
    public AnalyticsDTO compute() {
        List<Employee> allEmployees  = employeeRepository.findAll();
        List<Employee> active        = allEmployees.stream().filter(e -> e.getTerminationDate() == null).toList();
        List<Employee> inactive      = allEmployees.stream().filter(e -> e.getTerminationDate() != null).toList();

        List<Project> allProjects    = projectRepository.findAll();
        List<Project> activeProjects = allProjects.stream().filter(p -> p.getTerminationDate() == null).toList();

        List<AssignmentDTO> assignments = assignmentRepository.findAllFlat();

        Map<Integer, Long> loadByProject = assignments.stream()
                .collect(Collectors.groupingBy(a -> a.getIdProject(), Collectors.counting()));

        Set<Integer> activeProjectIds = activeProjects.stream()
                .map(Project::getIdProject)
                .collect(Collectors.toSet());

        // Empleados con 2 o más proyectos activos simultáneos — indicador de concentración de carga
        int multiProject = (int) assignments.stream()
                .filter(a -> activeProjectIds.contains(a.getIdProject()))
                .collect(Collectors.groupingBy(a -> a.getIdEmployee(), Collectors.counting()))
                .values().stream()
                .filter(count -> count >= 2)
                .count();

        return new AnalyticsDTO(
                buildEmployeeStats(active, inactive, multiProject),
                buildProjectStats(allProjects, activeProjects, loadByProject),
                buildLocationBreakdown(activeProjects),
                buildProjectLoad(activeProjects, loadByProject),
                buildTenureBreakdown(active),
                buildAgeBreakdown(active),
                buildHiresPerYear(allEmployees)
        );
    }

    // -----------------------------------------------------------------
    // Bloques de cálculo
    // -----------------------------------------------------------------

    private EmployeeStats buildEmployeeStats(List<Employee> active, List<Employee> inactive,
                                             int multiProject) {
        LocalDate today = LocalDate.now();

        int avgAge = (int) Math.round(active.stream()
                .filter(e -> e.getBirthDate() != null)
                .mapToLong(e -> ChronoUnit.YEARS.between(e.getBirthDate(), today))
                .average().orElse(0));

        // Antigüedad en años con un decimal de precisión
        double avgTenure = active.stream()
                .filter(e -> e.getHireDate() != null)
                .mapToLong(e -> ChronoUnit.DAYS.between(e.getHireDate(), today))
                .average().orElse(0) / 365.25;

        return new EmployeeStats(
                active.size() + inactive.size(),
                active.size(),
                inactive.size(),
                avgAge,
                Math.round(avgTenure * 10.0) / 10.0,
                pct(active.stream().filter(e -> "S".equals(e.getHasUniversityEducation())).count(), active.size()),
                multiProject
        );
    }

    private ProjectStats buildProjectStats(List<Project> all, List<Project> active,
                                           Map<Integer, Long> loadByProject) {
        double avgTeam = active.stream()
                .mapToLong(p -> loadByProject.getOrDefault(p.getIdProject(), 0L))
                .average().orElse(0);

        int uncovered = (int) active.stream()
                .filter(p -> loadByProject.getOrDefault(p.getIdProject(), 0L) == 0)
                .count();

        // Duración media solo para proyectos con fecha de fin registrada
        int avgDuration = (int) Math.round(all.stream()
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null)
                .mapToLong(p -> ChronoUnit.DAYS.between(p.getStartDate(), p.getEndDate()))
                .average().orElse(0));

        return new ProjectStats(
                all.size(), active.size(), all.size() - active.size(),
                Math.round(avgTeam * 10.0) / 10.0,
                uncovered,
                avgDuration
        );
    }

    private List<NameCount> buildLocationBreakdown(List<Project> active) {
        return active.stream()
                .collect(Collectors.groupingBy(Project::getLocation, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new NameCount(e.getKey(), e.getValue().intValue()))
                .sorted(Comparator.comparingInt(NameCount::count).reversed())
                .toList();
    }

    private List<NameCount> buildProjectLoad(List<Project> active, Map<Integer, Long> loadByProject) {
        return active.stream()
                .map(p -> new NameCount(
                        p.getDescription(),
                        loadByProject.getOrDefault(p.getIdProject(), 0L).intValue()))
                .filter(nc -> nc.count() > 0)
                .sorted(Comparator.comparingInt(NameCount::count).reversed())
                .limit(8)
                .toList();
    }

    /**
     * Agrupa los empleados activos en tramos de antigüedad ordenados cronológicamente.
     */
    private List<NameCount> buildTenureBreakdown(List<Employee> active) {
        LocalDate today = LocalDate.now();
        Map<String, Integer> buckets = new LinkedHashMap<>();
        buckets.put("< 1 año",    0);
        buckets.put("1-3 años",   0);
        buckets.put("3-5 años",   0);
        buckets.put("5-10 años",  0);
        buckets.put("+10 años",   0);

        for (Employee e : active) {
            if (e.getHireDate() == null) continue;
            long years = ChronoUnit.YEARS.between(e.getHireDate(), today);
            if      (years < 1)  buckets.merge("< 1 año",   1, Integer::sum);
            else if (years < 3)  buckets.merge("1-3 años",  1, Integer::sum);
            else if (years < 5)  buckets.merge("3-5 años",  1, Integer::sum);
            else if (years < 10) buckets.merge("5-10 años", 1, Integer::sum);
            else                 buckets.merge("+10 años",  1, Integer::sum);
        }

        return buckets.entrySet().stream()
                .map(e -> new NameCount(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * Agrupa los empleados activos en tramos de edad para el análisis generacional.
     */
    private List<NameCount> buildAgeBreakdown(List<Employee> active) {
        LocalDate today = LocalDate.now();
        Map<String, Integer> buckets = new LinkedHashMap<>();
        buckets.put("< 25 años",  0);
        buckets.put("25-35 años", 0);
        buckets.put("35-45 años", 0);
        buckets.put("45-55 años", 0);
        buckets.put("+55 años",   0);

        for (Employee e : active) {
            if (e.getBirthDate() == null) continue;
            int age = (int) ChronoUnit.YEARS.between(e.getBirthDate(), today);
            if      (age < 25) buckets.merge("< 25 años",  1, Integer::sum);
            else if (age < 35) buckets.merge("25-35 años", 1, Integer::sum);
            else if (age < 45) buckets.merge("35-45 años", 1, Integer::sum);
            else if (age < 55) buckets.merge("45-55 años", 1, Integer::sum);
            else               buckets.merge("+55 años",   1, Integer::sum);
        }

        return buckets.entrySet().stream()
                .map(e -> new NameCount(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * Agrupa el total de empleados (activos e inactivos) por año de alta para la línea de evolución.
     * El resultado viene ordenado cronológicamente para que el gráfico se renderice correctamente.
     */
    private List<NameCount> buildHiresPerYear(List<Employee> all) {
        return all.stream()
                .filter(e -> e.getHireDate() != null)
                .collect(Collectors.groupingBy(
                        e -> String.valueOf(e.getHireDate().getYear()),
                        Collectors.counting()))
                .entrySet().stream()
                .map(e -> new NameCount(e.getKey(), e.getValue().intValue()))
                .sorted(Comparator.comparing(NameCount::name))
                .toList();
    }

    private int pct(long part, int total) {
        return total == 0 ? 0 : (int) Math.round(part * 100.0 / total);
    }
}
