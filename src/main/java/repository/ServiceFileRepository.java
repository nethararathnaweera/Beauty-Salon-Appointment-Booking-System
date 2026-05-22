package repository;

import model.HairService;
import model.Service;
import model.SkinService;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.FileNameMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ServiceFileRepository {

    private static final String FILENAME = "services.txt";


    // Read all services from text file
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();

        File file = new File(FILENAME);

        if (!file.exists()) {
            return services;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Service service = convertLineService(line);

                if (service != null) {
                    services.add(service);
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return services;
    }


    // Save new service into file
    public void save(Service service) {
        if (service.getId() == null || service.getId().isEmpty()) {
            service.setId(UUID.randomUUID().toString());
        }

        try (BufferedWriter Writer = new BufferedWriter(new FileWriter(FILENAME, true))) {
            Writer.write(convertServiceToLine(service));
            Writer.newLine();

        } catch (IOException e) {
            System.out.println("Error writing files:" + e.getMessage());
        }

    }


    // Find service using ID
    public Service findById(String id){
        List<Service> services =findAll();

        for (Service service:services){
            if (service.getId().equals(id)){
                return service;
            }
        }return null;
    }


    // Find services using category
    public List<Service>finfByCategory(String category){
        List<Service> result = new ArrayList<>();

        for (Service service:findAll()){
            if(service.getCategory().equalsIgnoreCase(category)){
                result.add(service);
            }
        }return result;
    }

    //Update existing service
    public void update(Service updateService){
        List<Service> services = findAll();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))){
            boolean found = false;

            for (Service service :services){
                if (service.getId().equals(updateService.getId())){
                    writer.write(convertServiceToLine(service));
                    writer.newLine();
                    found=true;
                }
                else {
                    writer.write(convertServiceToLine(service));
                    writer.newLine();
                }
            }

        }catch (IOException e){
            System.out.println("Error detete from filrs:"+e.getMessage());
        }
    }


    // Delete service using ID
    public void deleteById(String id) {
        List<Service> services = findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))){
            for (Service service : services) {
                if (!service.getId().equals(id)) {
                    writer.write(convertServiceToLine(service));
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error deleting file"+e.getMessage());
        }
    }


    // Convert object into text line
    private String convertServiceToLine(Service service) {
        String extraValue="";

        if(service instanceof HairService){
            extraValue=((HairService)service).getHairType();

        } else if (service instanceof SkinService) {
            extraValue=((SkinService)service).getSkinType();

        }return service.getType()+"|"+
                service.getId()+"|"+
                service.getName()+"|"+
                service.getCategory()+"|"+
                service.getPrice()+"|"+
                service.getDuration()+"|"+
                service.getDescription()+"|"+
                extraValue;
    }


    // Convert text line into object
    private Service convertLineService(String line) {
        String[] data = line.split("\\|");

        if (data.length < 8) {
            return null;
        }

        String type = data[0];
        String id = data[1];
        String name = data[2];
        String category = data[3];
        double price = Double.parseDouble(data[4]);
        int duration = Integer.parseInt(data[5]);
        String description = data[6];
        String extravalue = data[7];

        if (type.equalsIgnoreCase("HAIR")) {

            return new HairService(
                    id,
                    name,
                    category,
                    price,
                    duration,
                    description,
                    extravalue
            );
        } else if (type.equalsIgnoreCase("SKIN")) {

            return new SkinService(
                    id,
                    name,
                    category,
                    price,
                    duration,
                    description,
                    extravalue
            );

        }return null;

    }

}