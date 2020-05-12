package com.example.myapplication.offer;

/**
 * Class of offer, that we get from Allegro API in JSON
 * Implement Comparable for sorting in array
 */
public class Offer implements Comparable {

    private String id;
    private String name;
    private String thumbnailUrl;
    private Price price;
    private String description;

    public Offer(String id, String name, String thumbnailUrl, double amount, String currency, String description) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.price = new Price(amount, currency);
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Price getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return price.getAmount();
    }

    public String getCurrency() {
        return price.getCurrency();
    }

    /**
     * Compare two offers on their price
     * @param o - second offer
     * @return
     */
    @Override
    public int compareTo(Object o) {
        Offer second = (Offer) o;
        double result = this.getPrice().getAmount() - second.getPrice().getAmount();
        if (result == 0d) {
            return (int) result;
        }
        return result < 0d ? -1 : 1;
    }
}
