package org.layermark.lib.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {
    private long id;
    private Date startDate;
    private Date endDate;

    private long userId;
    private long bookId;
}
