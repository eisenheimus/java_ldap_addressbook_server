package corp.addressbook.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdUser {
    private String accountName;
    private String displayName;
    private String mail;
    private String phone;
    private String office;
    private String title;
    private String department;
}
