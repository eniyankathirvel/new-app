package hieutran.crud.service.impl;

import hieutran.crud.dto.EmployeeDto;
import hieutran.crud.dto.response.PageResponse;
import hieutran.crud.entity.Employee;
import hieutran.crud.exception.ResourceConflictException;
import hieutran.crud.exception.ResourceNotFoundException;
import hieutran.crud.mapper.EmployeeMapper;
import hieutran.crud.repository.EmployeeRepository;
import hieutran.crud.repository.SearchRepository;
import hieutran.crud.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j //! Để sử dụng log
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private SearchRepository searchRepository;

    @Override
    public EmployeeDto createEntity(EmployeeDto employeeDto) {
        //! Chuyển đổi từ EmployeeDto sang Employee
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee != null) {
            throw new ResourceConflictException("Email is already taken!");
        }
        Employee saveEmployee = employeeRepository.save(employee);
        log.info("Employee {} has been created", saveEmployee.getId());
        //!Chuyển đổi từ Employee sang EmployeeDto (đã lưu vào database là employee còn trả về là employeeDto)
        return EmployeeMapper.mapToEmployeeDTo(saveEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        log.info("Employee {} has been found", employee.getId());
        return EmployeeMapper.mapToEmployeeDTo(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        log.info("Retrieved {} employees", employees.size());
        return employees.stream().map(EmployeeMapper::mapToEmployeeDTo).collect(Collectors.toList());
        /*
         * collect(Collectors.toList(): Chuyển đổi Stream của các đối tượng EmployeeDto trả thành một danh sách
         */
    }

    @Override
    public PageResponse<?> getAllEmployees(int page, int size, String sort) {
        int p = 0;
        if (page > 0) {
            p = page - 1;
        }

        List<Sort.Order> sortBy = new ArrayList<>();

        //Kiểm giá trị của sort
        if (StringUtils.hasLength(sort)) {
            // làm theo biểu thức chính qui
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("ASC")) {
                    sortBy.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else if (matcher.group(3).equalsIgnoreCase("DESC")) {
                    sortBy.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    // Nếu người dùng nhập không đúng định dạng thì mặc định sẽ sắp xếp theo chiều tăng dần
                    sortBy.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, size, Sort.by(sortBy));
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(employees.getTotalPages())
                .items(employees.stream().map(EmployeeMapper::mapToEmployeeDTo))
                .build();
//        return employees.map(EmployeeMapper::mapToEmployeeDTo);
    }

    @Override
    public PageResponse<?> getAllEmployeesWithSortByMultipleFields(int page, int size, String... sort) {
        int p = 0;
        if (page > 0) {
            p = page - 1;
        }

        List<Sort.Order> sortBy = new ArrayList<>();

        for (String s : sort) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("ASC")) {
                    sortBy.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else if (matcher.group(3).equalsIgnoreCase("DESC")) {
                    sortBy.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                } else {
                    // Nếu người dùng nhập không đúng định dạng thì mặc định sẽ sắp xếp theo chiều tăng dần
                    sortBy.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(p, size, Sort.by(sortBy));
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(employees.getTotalPages())
                .items(employees.stream().map(EmployeeMapper::mapToEmployeeDTo).collect(Collectors.toList()))
                .build();
//        return employees.stream().map(EmployeeMapper::mapToEmployeeDTo).collect(Collectors.toList());
    }

    @Override
    public PageResponse<?> getAllEmployeesWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        return searchRepository.getAllEmployeesWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advancedSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return searchRepository.advancedSearchEmployee(pageNo, pageSize, sortBy, search);
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findByEmail(employeeDto.getEmail());
        if (existingEmployee != null && !existingEmployee.getId().equals(employeeId)) {
            throw new ResourceConflictException("Email is already taken!");
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        Employee updateEmployee = employeeRepository.save(employee);
        log.info("Employee {} has been updated", updateEmployee.getId());
        return EmployeeMapper.mapToEmployeeDTo(updateEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        log.info("Employee {} has been deleted", employee.getId());
        employeeRepository.deleteById(employeeId);
    }
}
