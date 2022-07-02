package org.layermark.lib.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithPasswordDto {

    private long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;
}
