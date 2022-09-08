package com.flaminiovilla.teltonikaparser;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CarAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String model;
    public String yearTo;
    public String programNumber;
    public String dateFrom;


    @ElementCollection // 1
    @CollectionTable(name = "attributes", joinColumns = @JoinColumn(name = "id")) // 2
    @Column(name = "list") // 3
    private List<String> attributes;

    public CarAttribute(String nameCar, String year, String number, String from, ArrayList<String> propsOfCar) {
        this.model = nameCar;
        this.yearTo = year;
        this.programNumber = number;
        this.dateFrom = from;
        this.attributes = propsOfCar;
    }

    public CarAttribute() {

    }
}
