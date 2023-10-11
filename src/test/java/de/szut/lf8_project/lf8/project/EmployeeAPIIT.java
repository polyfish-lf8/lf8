package de.szut.lf8_project.lf8.project;

import de.szut.employees.EmployeeAPI;
import de.szut.lf8_project.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class EmployeeAPIIT extends AbstractIntegrationTest {
    @Test
    public void getFirstEmployee() throws Exception {
        assert EmployeeAPI.getInstance().findEmployeeById(1L, token) != null;
    }

    @Test
    public void idNotFound() throws Exception {
        assert EmployeeAPI.getInstance().findEmployeeById(999L, token) == null;
    }

    @Test
    public void getAll() throws Exception {
        assert Arrays.stream(EmployeeAPI.getInstance().getAll(token)).findAny().isPresent();
    }
}
