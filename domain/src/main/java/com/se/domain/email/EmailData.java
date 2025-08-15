package com.se.domain.email;

import java.util.List;

import com.se.domain.configuration.ConfigurationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents data for the email.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailData {
    private String senderName;
    private String senderEmail;
    private String recipientName;
    private String recipientEmail;
    private String message = "";
    private String configurationId;
    private String configurationName;
    private Boolean expressDelivery;
    private String host;
    private String captchaResponse;
    private ConfigurationStatus configurationStatus;
    List<Product> products;
    private String diameter;
    private List<String> images;

    /**
     * Copy constructor.
     *
     * @param emailData {@link EmailData} instance to copy.
     */
    public EmailData(EmailData emailData) {
        this.senderName = emailData.senderName;
        this.senderEmail = emailData.senderEmail;
        this.recipientName = emailData.recipientName;
        this.recipientEmail = emailData.recipientEmail;
        this.message = emailData.message;
        this.configurationId = emailData.configurationId;
        this.host = emailData.host;
        this.captchaResponse = emailData.captchaResponse;
        this.configurationStatus = emailData.configurationStatus;
        this.expressDelivery = emailData.expressDelivery;
        this.products = emailData.products;
        this.diameter = emailData.diameter;
        this.images = emailData.images;
    }

     /**
     * Represents customized product data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private String productType;
        private List<Device> devices;

        /**
         * Copy constructor.
         *
         * @param product {@link Product} instance to copy.
         */
        public Product(Product product) {
            this.productType = product.productType;
            this.devices = product.devices;
        }
    }

     /**
     * Represents customized product data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Device {
        private String catalog;
        private String description;
        private String quantity;
        private String link;

        /**
         * Copy constructor.
         *
         * @param device {@link Device} instance to copy.
         */
        public Device(Device device) {
            this.catalog = device.catalog;
            this.description = device.description;
            this.quantity = device.quantity;
            this.link = device.link;
        }
    }
}
