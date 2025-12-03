package corp.addressbook.controller;

import corp.addressbook.dto.AdUser;
import corp.addressbook.service.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LdapController {

    private final LdapService ldapService;

    @GetMapping("/users")
    public List<AdUser> getUsers(@RequestParam(required = false) String name) {
        return ldapService.getAllUsers(name);
    }
}
