package corp.addressbook.service;


import corp.addressbook.dto.AdUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LdapService {

    private final LdapTemplate ldapTemplate;

    private String getAttributeValue(Attribute attr) {
        if (attr == null) {
            return null;
        }
        try {
            Object value = attr.get();
            return value != null ? value.toString() : null;
        } catch (NoSuchElementException | NamingException e) {
            return null;
        }
    }

    private AdUser getAttributes(Attributes attrs){
        AdUser user = new AdUser();

        user.setAccountName(getAttributeValue(attrs.get("sAMAccountName")));
        user.setDisplayName(getAttributeValue(attrs.get("displayName")));
        user.setMail(getAttributeValue(attrs.get("mail")));
        user.setPhone(getAttributeValue(attrs.get("telephoneNumber")));
        user.setOffice(getAttributeValue(attrs.get("physicalDeliveryOfficeName")));
        user.setTitle(getAttributeValue(attrs.get("title")));
        user.setDepartment(getAttributeValue(attrs.get("department")));

        return user;
    }

    public List<AdUser> getAllUsers(String name) {
        List<AdUser> users = ldapTemplate.search(
                LdapQueryBuilder.query()
                        .where("objectCategory").is("person")
                        .and("displayName").like("*"),
                this::getAttributes
        );

        return name == null ? users :
                users.stream()
                    .filter(user -> user.getDisplayName() != null)
                    .filter((AdUser user) -> user.getDisplayName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
    }
}
