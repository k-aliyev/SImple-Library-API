package org.layermark.lib.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {

    private long id;

    private String firstName;

    private String lastName;
}
