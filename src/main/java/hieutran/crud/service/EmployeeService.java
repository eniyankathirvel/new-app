package hieutran.crud.service;

import hieutran.crud.dto.EmployeeDto;
import hieutran.crud.dto.response.PageResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEntity(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long employeeId);

    List<EmployeeDto> getAllEmployees();

    PageResponse<?> getAllEmployees(int page, int size, String sort);

    PageResponse<?> getAllEmployeesWithSortByMultipleFields(int page, int size, String... sort);

    PageResponse<?> getAllEmployeesWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advancedSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search);

    EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);

    void deleteEmployee(Long employeeId);
}
