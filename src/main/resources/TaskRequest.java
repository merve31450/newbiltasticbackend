// src/main/java/org/u2soft/billtasticbackend/dto/TaskRequest.java
package org.u2soft.billtasticbackend.dto;

/**
 * Yeni bir Task oluşturmak için istek gövdesi
 */
public record TaskRequest(
        String title,
        String collection,
        String description
) {}
