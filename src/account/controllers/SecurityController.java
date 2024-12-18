package account.controllers;

import account.service.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    @Autowired
    private LogServiceImpl logService;

    @GetMapping("/events/")
    public ResponseEntity<?> getLogs() {
        if (this.logService.getLogs().size() == 0) {
            return ResponseEntity.ok().body("{" + this.logService.getLogs() + "}");
        } else {
            return ResponseEntity.ok().body(this.logService.getLogs());
        }
    }

}
