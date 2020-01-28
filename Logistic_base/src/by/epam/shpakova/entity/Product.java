package by.epam.shpakova.entity;

public class Product {

    private String description; //какой товар

    public Product(final String descriptionVal) {
        this.description = descriptionVal;
    }

    public String getDescription() {
        return description;
    }
}
