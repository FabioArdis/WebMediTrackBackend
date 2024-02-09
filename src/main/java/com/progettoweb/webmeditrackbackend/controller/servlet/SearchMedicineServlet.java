package com.progettoweb.webmeditrackbackend.controller.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoweb.webmeditrackbackend.persistence.DBManager;
import com.progettoweb.webmeditrackbackend.persistence.model.Medicine;
import com.progettoweb.webmeditrackbackend.persistence.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/doSearchMedicine")
public class SearchMedicineServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String dbUrl = "http://localhost:8081/api/farmaci/searchByName";
        String name = req.getParameter("name");
        String fullUrl = dbUrl + "?name=" + name;
        URL url = new URL(fullUrl);
        HttpSession session = req.getSession();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        List<Medicine> medicineList = new ArrayList<>();

        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null)
                responseContent.append(line);

            reader.close();
        } else {
            responseContent.append("ERROR:").append(responseCode);
        }

        connection.disconnect();

        String result = responseContent.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = objectMapper.readTree(result);

        for (JsonNode jsonNode : jsonArray)
        {
            Medicine medicine = new Medicine();
            medicine.setId(jsonNode.get("id").asInt());
            medicine.setName(jsonNode.get("name").asText());
            medicine.setDosage(jsonNode.get("dosage").asInt());
            medicine.setManufacture(jsonNode.get("manufacturer").asText());
            medicine.setBatchNumber(jsonNode.get("batchNumber").asInt());
            Date expirationTime = null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                expirationTime = dateFormat.parse(jsonNode.get("expirationTime").asText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            medicine.setExpirationTime(expirationTime);
            medicine.setNotes(jsonNode.get("notes").asText());

            medicineList.add(medicine);
        }

        req.setAttribute("searchMedicineList", medicineList);
        session.setAttribute("searchMedicineList", medicineList);

        resp.sendRedirect("/doSearchMeds");
    }
}
