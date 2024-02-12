package com.progettoweb.webmeditrackbackend.persistence.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettoweb.webmeditrackbackend.persistence.DBManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plan {
    private int id;
    private String name;
    private int length;
    private String type;

    private List<Integer> medicinesIds;
    private List<Medicine> medicines;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getLength() {
        return length;
    }
    public String getType() {
        return type;
    }
    public List<Integer> getMedicinesIds() { return medicinesIds; }
    public List<Medicine> getMedicines() { return medicines; }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void addMedicineId(int id)
    {
        if (medicinesIds == null)
            medicinesIds = new ArrayList<>();

        this.medicinesIds.add(id);
    }

    public void removeMedicineId(int id)
    {
        if (medicinesIds == null)
            medicinesIds = new ArrayList<>();

        this.medicinesIds.remove(medicinesIds.indexOf(id));
    }

    public void loadMedicinesDetail() throws IOException {
        System.out.println("aggiornando i farmaci di " + this.name);
        String dbUrl = "http://localhost:8081/api/farmaci/";

        if (medicines == null)
            medicines = new ArrayList<>();
        if (medicinesIds == null)
            medicinesIds = new ArrayList<>();

        medicines.clear();
        if (!medicinesIds.isEmpty()) {
            for (int medicineId : medicinesIds) {
                String fullUrl = dbUrl + Integer.toString(medicineId);
                URL url = new URL(fullUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                StringBuilder responseContent = new StringBuilder();
                if (responseCode == HttpURLConnection.HTTP_OK) {
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
                JsonNode jsonNode = objectMapper.readTree(result);

                System.out.println("name" + jsonNode.get("name").asText());
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

                medicines.add(medicine);

            }
        }
        System.out.println(medicines);
    }
}
