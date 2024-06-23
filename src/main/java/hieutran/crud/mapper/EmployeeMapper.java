package hieutran.crud.mapper;

import hieutran.crud.dto.EmployeeDto;
import hieutran.crud.entity.Employee;

public class EmployeeMapper {
    //! Chuyển đổi từ Employee sang EmployeeDto
    public static EmployeeDto mapToEmployeeDTo(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }

    //! Chuyển đổi từ EmployeeDto sang Employee
    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        return Employee.builder()
//                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
    }
}
