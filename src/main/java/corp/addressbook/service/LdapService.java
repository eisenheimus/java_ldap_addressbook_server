package corp.addressbook.service;


import corp.addressbook.dto.AdUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LdapService {

    private final LdapTemplate ldapTemplate;

    public List<AdUser> getAllUsers(String name) {
        List<AdUser> users = ldapTemplate.search(
                LdapQueryBuilder.query()
                        .where("objectCategory").is("person")
                        .and("displayName").like("*"),
                (AttributesMapper<AdUser>) attrs -> {

                    AdUser user = new AdUser();

                    Attribute samAttr = attrs.get("sAMAccountName");
                    user.setAccountName(samAttr != null ? (String) samAttr.get() : null);

                    Attribute nameAttr = attrs.get("displayName");
                    user.setDisplayName(nameAttr != null ? (String) nameAttr.get() : null);

                    Attribute mailAttr = attrs.get("mail");
                    user.setMail(mailAttr != null ? (String) mailAttr.get() : null);

                    Attribute phoneAttr = attrs.get("telephoneNumber");
                    user.setPhone(phoneAttr != null ? (String) phoneAttr.get() : null);

                    Attribute officeAttr = attrs.get("physicalDeliveryOfficeName");
                    user.setOffice(officeAttr != null ? (String) officeAttr.get() : null);

                    Attribute titleAttr = attrs.get("title");
                    user.setTitle(titleAttr != null ? (String) titleAttr.get() : null);

                    Attribute departmentAttr = attrs.get("department");
                    user.setDepartment(departmentAttr != null ? (String) departmentAttr.get() : null);

                    return user;
                }
        );

        return name == null ? users :
                users.stream()
                    .filter(user -> user.getDisplayName() != null)
                    .filter((AdUser user) -> user.getDisplayName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
    }
}
