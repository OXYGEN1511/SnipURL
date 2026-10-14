package com.myproject.snipurl.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClickEventDTO {
    LocalDate clickDate;
    Long count;
}
