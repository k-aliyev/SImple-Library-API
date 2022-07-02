package org.layermark.lib.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private long id;

    private String email;

    private String firstName;

    private String lastName;

}
