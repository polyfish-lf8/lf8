package de.szut.employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.employees.dto.EmployeeResponseDTO;
import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import de.szut.lf8_project.utils.HttpsRequests;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class EmployeeAPI {
    private static EmployeeAPI instance;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpsRequests client = new HttpsRequests();
    private final String baseEmployeesURL = "https://employee.szut.dev/employees";

    public EmployeeResponseDTO findEmployeeById(Long id, String oauthToken) throws IOException, InvalidDataException {
        Request request = new Request.Builder()
                .url(String.format("%s/%d", baseEmployeesURL, id))
                .header("Authorization", oauthToken)
                .get()
                .build();

        Response response = client.makeRequest(request);
        if(response.code() == 404)
            return null;
        else if(response.code() != 200 || response.body() == null)
            throw new InvalidDataException(String.format("Something went wrong during the request to the employees API. Status code: %d", response.code()));

        return mapper.readValue(response.body().string(), EmployeeResponseDTO.class);
    }

    public EmployeeResponseDTO[] getAll(String oauthToken) throws IOException {
        Request request = new Request.Builder()
                .url(baseEmployeesURL)
                .header("Authorization", oauthToken)
                .get()
                .build();

        Response response = client.makeRequest(request);
        if(response.code() != 200)
            throw new InvalidDataException(String.format("Something went wrong during the request to the employees API. Status code: %d", response.code()));

        return mapper.readValue(response.body().string(), EmployeeResponseDTO[].class);
    }

    public static EmployeeAPI getInstance() {
        if(instance == null)
            instance = new EmployeeAPI();

        return instance;
    }
}