package com.example.stub;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/app/v1")
public class StubController {

    @GetMapping("/getRequest")
    public ResponseEntity<String> getRequest(@RequestParam int id, @RequestParam String name) throws IOException, InterruptedException {
        if (id <= 10 || name.length() <= 5) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Invalid id or name length");
        }

        //задержка
        if (id > 10 && id < 50) {
            Thread.sleep(1000);
        } else {
            Thread.sleep(500);
        }

        String response = new String(Files.readAllBytes(Paths.get("src/main/resources/getAnswer.txt")));
        response = response.replace("{id}", String.valueOf(id))
                           .replace("{name}", name);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/postRequest")
    public ResponseEntity<String> postRequest(@RequestBody PostRequestBody requestBody) throws IOException {
        if (requestBody.getName().isEmpty() || requestBody.getSurname().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Name or surname cannot be empty");
        }

        String response = new String(Files.readAllBytes(Paths.get("src/main/resources/postAnswer.txt")));
        response = response.replace("{name}", requestBody.getName())
                           .replace("{surname}", requestBody.getSurname())
                           .replace("{age}", String.valueOf(requestBody.getAge() == null ? 123 : requestBody.getAge()))
                           .replace("{age}*2", String.valueOf((requestBody.getAge() == null ? 123 : requestBody.getAge()) * 2));

        return ResponseEntity.ok(response);
    }
}
