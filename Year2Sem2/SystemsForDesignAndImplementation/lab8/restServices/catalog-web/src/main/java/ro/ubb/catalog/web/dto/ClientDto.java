package ro.ubb.catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class ClientDto extends BaseDto {

    private String name;
    private int age;
    private Integer address;
    private String email;
}
