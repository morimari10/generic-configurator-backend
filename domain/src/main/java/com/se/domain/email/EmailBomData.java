package com.se.domain.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.se.domain.bom.BomStatus;

/**
 * Represents data for the email.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailBomData {
    private String senderName;
    private String senderEmail;
    private String recipientName;
    private String recipientEmail;
    private String message = "";
    private String configurationId;
    private Boolean expressDelivery;
    private List<Product> product;
    private String host;
    private String captchaResponse;
    private BomStatus bomStatus;
    private String country;
    private String language;
    private String photoLink;
    private String configurationName;



    /**
     * Copy constructor.
     *
     * @param emailData {@link EmailBomData} instance to copy.
     */
    public EmailBomData(EmailBomData emailData) {
        this.senderName = emailData.senderName;
        this.senderEmail = emailData.senderEmail;
        this.recipientName = emailData.recipientName;
        this.recipientEmail = emailData.recipientEmail;
        this.message = emailData.message;
        this.configurationId = emailData.configurationId;
        this.host = emailData.host;
        this.captchaResponse = emailData.captchaResponse;
        this.product = emailData.product;
        this.bomStatus = emailData.bomStatus;
        this.expressDelivery = emailData.expressDelivery;
        this.country = emailData.country;
        this.language = emailData.language;
        this.photoLink = emailData.photoLink;
        this.configurationName = emailData.configurationName;
    }

    /**
     * Represents customized product data.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private String reference;
        private String description;
        private Integer quantity;
        private String crId;
        private String custoId;
        private String fileName;
        private String commercialReference;
        private String orderingInformation;
        private boolean hyve2FilePresent;


        /**
         * Copy constructor.
         *
         * @param product {@link Product} instance to copy.
         */
        public Product(Product product) {
            this.reference = product.reference;
            this.description = product.description;
            this.quantity = product.quantity;
            this.crId = product.crId;
            this.custoId = product.custoId;
            this.fileName = product.fileName;
            this.commercialReference = product.commercialReference;
            this.orderingInformation = product.orderingInformation;
            this.hyve2FilePresent = product.hyve2FilePresent;
        }
    }
}
