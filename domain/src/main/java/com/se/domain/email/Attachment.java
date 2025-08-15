package com.se.domain.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.activation.DataSource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    private DataSource dataSource;
    private String fileName;
    private String description;
}
