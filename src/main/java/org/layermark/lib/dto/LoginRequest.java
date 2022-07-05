package org.layermark.lib.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequest {

    private String email;

    private String password;

}