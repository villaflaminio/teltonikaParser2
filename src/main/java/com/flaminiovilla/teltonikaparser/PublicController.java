package com.flaminiovilla.teltonikaparser;

import lombok.AllArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;


import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Controller
@RequestMapping("api/public")
public class PublicController {

    @Autowired
    CarAttributeRepository carAttributeRepository;
    @GetMapping("/get")
    public ResponseEntity<?> getValue() throws TikaException, IOException, SAXException {

        return ResponseEntity.ok(carAttributeRepository.findAll());

    }

    @GetMapping("/parse")
    public ResponseEntity<?> parse() throws TikaException, IOException, SAXException {
        System.out.println("start");
        String header = "Ignition\n" +
                "Key inserted\n" +
                "Engine working\n" +
                "Ready to drive \n" +
                "Engine is working on CNG\n" +
                "The lock is active\n" +
                "Lock request\n" +
                "Handbrake\n" +
                "Footbrake\n" +
                "Status of the hazard warning lights switch\n" +
                "Front left door\n" +
                "Front right door\n" +
                "Rear left door\n" +
                "Rear right door\n" +
                "Roof\n" +
                "Trunk cover\n" +
                "Engine cover (Hood)\n" +
                "Charging cable connected\n" +
                "HV battery charging\n" +
                "Electric engine working\n" +
                "Car locked with the factory remote\n" +
                "Central locking status\n" +
                "Factory alarm\n" +
                "Emulated alarm\n" +
                "Original remote - CLOSE\n" +
                "Original remote - OPEN\n" +
                "The original remote of the car - Rearming\n" +
                "The original remote of the car - Open trunk\n" +
                "Parking\n" +
                "Reverse\n" +
                "Neutral\n" +
                "Drive\n" +
                "Engine is working on LPG\n" +
                "Parking lights\n" +
                "Dipped headlights\n" +
                "Full beam headlights\n" +
                "Rear fog lights\n" +
                "Front fog lights\n" +
                "Driver’s seat belt\n" +
                "Passenger’s seat belt\n" +
                "Left rear seat belt\n" +
                "Right rear seat belt\n" +
                "Middle rear seat belt\n" +
                "Oil pressure / level\n" +
                "Low AdBlue level\n" +
                "Low CNG Level\n" +
                "Total mileage of the vehicle (dashboard)\n" +
                "Vehicle mileage - (counted)\n" +
                "Total fuel consumption\n" +
                "Total fuel consumption - (counted)\n" +
                "Fuel level (in percent)\n" +
                "Fuel level (in liters)\n" +
                "Engine speed (RPM)\n" +
                "Engine temperature\n" +
                "Vehicle speed\n" +
                "AdBlue level (in percent)\n" +
                "AdBlue level (in liters)\n" +
                "Accelerator pedal position\n" +
                "VIN number\n" +
                "HV battery level\n" +
                "Total CNG use\n" +
                "Total CNG consumption\n" +
                "CNG level (in percent)\n" +
                "CNG level (in kilograms)\n" +
                "Vehicle's range on HV battery\n" +
                "Vehicle's range \n" +
                "Vehicle's range on CNG\n" +
                "Time to the end of the HV battery charge\n" +
                "CNG level (in bars)\n" +
                "CNG level use\n" +
                "Total LNG use – (counted)\n" +
                "LNG level (in percent)\n" +
                "LNG level (in kilograms)\n" +
                "Rear compartment temperature\n" +
                "Active gear number\n" +
                "Road sign speed limit\n" +
                "HV battery pack voltage\n" +
                "Minimum HV battery cell voltage\n" +
                "Maximum HV battery cell voltage\n" +
                "Alternative control of central locking\n" +
                "All doors lock\n" +
                "All doors unlock\n" +
                "Trunk release\n" +
                "Factory alarm control (arming/disarming)\n" +
                "Contr.c.lock. when ign.is on\n" +
                "Engine start/drive-away lock engaged\n" +
                "Engine start/drive-away lock disengaged\n" +
                "Alternative turn signal control\n" +
                "Turn signal control\n" +
                "Horn control\n" +
                "Comfort closure\n" +
                "Lock\n" +
                "Speed limit with value\n" +
                "End of speed limit with value\n" +
                "Exceeding the speed specified by the sign";

        String[] arrayHeader = header.split("\n");


        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File(
                "C:\\Users\\f.villa\\Downloads\\CAN-CONTROL_IMMO_list_2022_07_27_en.pdf"));

        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata, pcontext);

        String output = handler.toString();

        String[] lines = output.split("\n");

        String[] typeAndValue = Arrays.stream(lines).filter(val -> val.contains("year") || val.contains("+")).toArray(String[]::new);
        ;
        //getting the content of the document
        System.out.println(typeAndValue);

        for (int i = 0; i < typeAndValue.length; i++) {
            String s = typeAndValue[i];
            if (s.contains(")")) {

                //  1) ABARTH 124 SPIDER                                        year: 2016=> program № 12259 from 2020-06-30 to ABARTH 124 SPIDER , year: 2016 ,program № 12259, from 2020-06-30
                String[] splitYear = s.split("year:");
                String type = splitYear[0].replace(" ", "");
                String[] splitMaggiore = splitYear[1].split("=>");
                String[] splitNumber = splitMaggiore[1].split("№");
                String[] splitfrom = splitNumber[1].split("from");

                String nameCar =splitYear[0].split("\\)")[1];
                String year = splitMaggiore[0];
                String number = splitfrom[0];
                String from = splitfrom[1];
                nameCar = nameCar.trim();

                if(nameCar.contains("("))
                    nameCar += ")";

                System.out.println("nameCar: " + nameCar);
                System.out.println("year: " + year);
                System.out.println("number: " + number);
                System.out.println("from: " + from);
                System.out.println();

                String plusRow = typeAndValue[i+1].trim();
                String[] splitPlus = plusRow.split(" ");
                System.out.println(plusRow);

                ArrayList<String> propsOfCar = new ArrayList<>();
                for (int j = 0; j < arrayHeader.length; j++) {
                    if( j < splitPlus.length && splitPlus[j].contains("+")){
                        propsOfCar.add(arrayHeader[j]);
                    }
                }
                CarAttribute carAttributes = new CarAttribute(nameCar, year, number, from, propsOfCar);
                carAttributeRepository.save(carAttributes);
                System.out.println(propsOfCar);
            }
        }



        return ResponseEntity.ok("ok");
    }



}
