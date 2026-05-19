package com.omnia.backend.service;

import com.omnia.backend.exception.BusinessException;
import com.omnia.backend.model.dto.EmployeeDTO;
import com.omnia.backend.model.entities.Employee;
import com.omnia.backend.repository.EmployeeProjectRepository;
import com.omnia.backend.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para validar la lógica de negocio de empleados.
 * Estos tests demuestran el cumplimiento de los requisitos de la práctica final:
 * - Altas con validación de duplicados.
 * - Inmutabilidad de campos clave (NIF, Fecha Alta).
 * - Control de integridad referencial en bajas (Lógica y Física).
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeProjectRepository assignmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDTO testDto;

    @BeforeEach
    void setUp() {
        testDto = new EmployeeDTO();
        testDto.setNif("12345678Z");
        testDto.setFirstName("Susana");
        testDto.setLastName("Bitar");
        testDto.setSecondLastName("Azevedo");
        testDto.setBirthDate(LocalDate.of(1998, 10, 10));
        testDto.setPhone1("666777888");
        testDto.setPhone2("999888777");
        testDto.setEmail("susana@omnia.com");
        testDto.setHireDate(LocalDate.now());
        testDto.setMaritalStatus("S");
        testDto.setHasUniversityEducation("S");
    }

    @Test
    @DisplayName("Garantiza que un empleado se guarda correctamente si no hay duplicados")
    void saveEmployee_Success() {
        when(employeeRepository.findByNif(testDto.getNif())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> {
            Employee e = i.getArgument(0);
            e.setIdEmployee(1);
            return e;
        });

        EmployeeDTO saved = employeeService.saveEmployee(testDto);

        assertThat(saved.getIdEmployee()).isNotNull();
        assertThat(saved.getNif()).isEqualTo(testDto.getNif());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Bloquea el alta si el NIF ya existe en el sistema")
    void saveEmployee_DuplicateNif_ThrowsException() {
        when(employeeRepository.findByNif(testDto.getNif())).thenReturn(Optional.of(new Employee()));

        assertThatThrownBy(() -> employeeService.saveEmployee(testDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El NIF indicado ya está registrado en el sistema.");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verifica que no se puede cambiar el NIF en una actualización")
    void updateEmployee_ChangeNif_Forbidden() {
        Employee current = new Employee();
        current.setIdEmployee(1);
        current.setNif("00000000A"); // NIF original diferente
        current.setHireDate(testDto.getHireDate());

        when(employeeRepository.findById(1)).thenReturn(Optional.of(current));

        assertThatThrownBy(() -> employeeService.updateEmployee(1, testDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("inmutables");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Valida el proceso de baja lógica (Soft Delete) marcando la fecha actual")
    void deactivateEmployee_Success() {
        Employee current = new Employee();
        current.setIdEmployee(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(current));
        when(assignmentRepository.findProjectDescriptionsByEmployeeId(1)).thenReturn(Set.of());

        employeeService.deactivateEmployee(1);

        assertThat(current.getTerminationDate()).isEqualTo(LocalDate.now());
        verify(employeeRepository).save(current);
    }

    @Test
    @DisplayName("Impide la baja (Lógica o Física) si el empleado tiene proyectos activos")
    void deactivateEmployee_WithAssignments_Forbidden() {
        Employee current = new Employee();
        current.setIdEmployee(1);
        current.setFirstName("Susana");
        current.setLastName("Bitar");
        current.setSecondLastName("Azevedo");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(current));
        // Simulamos que tiene proyectos asignados
        when(assignmentRepository.findProjectDescriptionsByEmployeeId(1))
                .thenReturn(Set.of("Proyecto de Innovación 2026"));

        assertThatThrownBy(() -> employeeService.deactivateEmployee(1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Proyecto de Innovación 2026");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verifica que la baja física elimina el registro permanentemente")
    void deleteEmployee_Physical_Success() {
        Employee current = new Employee();
        current.setIdEmployee(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(current));
        when(assignmentRepository.findProjectDescriptionsByEmployeeId(1)).thenReturn(Set.of());

        employeeService.deleteEmployee(1);

        verify(employeeRepository).delete(current);
    }
}
