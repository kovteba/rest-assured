package kovteba.controller;

import kovteba.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/controller")
public class Controller {

    @GetMapping("")
    public ResponseEntity<Response> controller() {

        return ResponseEntity.ok(new Response("CONTROLLER"));
    }

    @GetMapping("/{number}")
    public ResponseEntity<Response> controller1(@RequestParam String value, @PathVariable String number) {

        return ResponseEntity.ok(new Response("CONTROLLER_1"));
    }

}
