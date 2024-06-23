package hieutran.crud.repository;

import hieutran.crud.criteria.EmployeeSearchCriteriaQueryConsumer;
import hieutran.crud.criteria.SearchCriteria;
import hieutran.crud.dto.response.PageResponse;
import hieutran.crud.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllEmployeesWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        StringBuilder sqlQuery = new StringBuilder("SELECT e FROM Employee e WHERE 1=1");

        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" AND (lower(e.firstName) LIKE lower(:firstName))");
            sqlQuery.append(" OR (lower(e.lastName) LIKE lower(:lastName))");
            sqlQuery.append(" OR (lower(e.email) LIKE lower(:email))");
        }

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
//            (firstName)(:)(ASC|DESC)
            if (matcher.find()) {
                sqlQuery.append(String.format(" ORDER BY e.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo); // vị trí bắt đầu lấy dữ liệu
        selectQuery.setMaxResults(pageSize); // số lượng bản ghi lấy ra
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("firstName", "%" + search + "%");
            selectQuery.setParameter("lastName", "%" + search + "%");
            selectQuery.setParameter("email", "%" + search + "%");
        }
        List employees = selectQuery.getResultList();
        System.out.println(employees);
        // query ra list employee theo search và sort

        //query so records
        StringBuilder sqlCountQuery = new StringBuilder("SELECT count(*) FROM Employee e WHERE 1=1");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" AND (lower(e.firstName) LIKE lower(?1))");
            sqlCountQuery.append(" OR (lower(e.lastName) LIKE lower(?2))");
            sqlCountQuery.append(" OR (lower(e.email) LIKE lower(?3))");
        }
        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, "%" + search + "%");
            selectCountQuery.setParameter(2, "%" + search + "%");
            selectCountQuery.setParameter(3, "%" + search + "%");
        }
        Long totalElement = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElement);
        Page<?> page = new PageImpl<Object>(employees, PageRequest.of(pageNo, pageSize), totalElement);
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }

    public PageResponse<?> advancedSearchEmployee(int pageNo, int pageSize, String sortBy, String... search) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        //lay ra danh sach employee
        if (search != null) {
            for (String s : search) {
                Pattern pattern = Pattern.compile("(\\w+?)([:><])(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        //lay ra so luong ban ghi
        List<Employee> employees = getEmployee(pageNo, pageSize, criteriaList, sortBy);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(employees.size() / pageSize + 1)
                .items(employees)
                .build();
    }

    private List<Employee> getEmployee(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        // xu ly cac dieu kien tim kiem
        Predicate predicate = criteriaBuilder.conjunction();
        EmployeeSearchCriteriaQueryConsumer queryConsumer = new EmployeeSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(ASC|DESC|asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("ASC")) {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                } else if (matcher.group(3).equalsIgnoreCase("DESC")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }

}
