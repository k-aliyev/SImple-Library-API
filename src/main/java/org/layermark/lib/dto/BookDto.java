package org.layermark.lib.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private long id;

    private String name;

    private String edition;

    private String publisher;

    private boolean isAvailable;

    private long authorId;
}
