package org.ecommerce.emailservice.dtos;

import lombok.*;

/**
 * Data Transfer Object for Email information.
 * Used to encapsulate email details for sending emails.
 * This DTO field's values are varied and EmailService will use these values to send emails.
 * No Changes to be handled in the EmailService for handling various email types/formats/templates/structures.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String to;
    private String from;
    private String subject;
    private String body;
}
