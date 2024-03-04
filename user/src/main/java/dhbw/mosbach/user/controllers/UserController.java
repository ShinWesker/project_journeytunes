package dhbw.mosbach.user.controllers;

import dhbw.mosbach.user.clients.EmailClient;
import dhbw.mosbach.user.cryptography.HashConfig;
import dhbw.mosbach.user.cryptography.SHA;
import dhbw.mosbach.user.models.Token;
import dhbw.mosbach.user.models.User;
import dhbw.mosbach.user.requestclasses.*;
import dhbw.mosbach.user.services.TokenService;
import dhbw.mosbach.user.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/users/api/v1")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final EmailClient emailClient;

    @PostMapping("/create")
    public ResponseEntity<GetUser> createUser(@RequestBody CreateUser createUser) {
        User user;

        if (userService.getUserByName(createUser.getName()) == null) {
            user = userService.saveUser(createUser.createUser());
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        String emailBody = """
                <!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>Verify Email</title></head><body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;"><div style="max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1);"><h2 style="color: #333;">Verify!</h2><p style="color: #666;"><a href="http://%s:%s/users/api/v1/verify?id=%d">Click to verify...</a></p><hr style="border: 1px solid #ddd;"><p style="color: #999; font-size: 12px;">Diese E-Mail wurde automatisch generiert. Bitte antworten Sie nicht darauf.</p></div></body></html>
                """.formatted("10.50.15.51", "8222", user.getId());

        Email email = new Email(user.getEmail(), "Verify Account E-Mail", emailBody);
        try {
            emailClient.sendEmail(email);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(new GetUser(user.getId(), user.getName(), user.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginUser loginUser) {
        User user = userService.getUserByName(loginUser.getName());

        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (!user.isVerified())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (!isPasswordCorrect(loginUser.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Token token = createUniqueTokenForUser(user);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String authorization) {
        String tokenValue = extractTokenValue(authorization);
        Token token = tokenService.getTokenByValue(tokenValue);
        if (token == null) {
            return ResponseEntity.notFound().build();
        }

        tokenService.deleteToken(token.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/verify")
    public ResponseEntity<Object> verifyToken(@RequestBody RequestToken token) {
        boolean tokenExists = tokenService.existsByTokenValue(token.getToken());
        return tokenExists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/get")
    public ResponseEntity<GetUser> getUser(@RequestBody RequestToken reqToken) {
        Token token = tokenService.getTokenByValue(reqToken.getToken());
        if (token == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userService.getUserById(token.getUserId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new GetUser(user.getId(), user.getName(), user.getEmail()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GetUser> getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GetUser(user.getId(), user.getName(), user.getEmail()));
    }

    @GetMapping("/verify")
    public String verify(@RequestParam int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return generateVerificationErrorPage();
        }
        user.setVerified(true);
        userService.saveUser(user);
        return generateVerificationSuccessPage();
    }

    private boolean isPasswordCorrect(String rawPassword, String hashedPassword) {
        return Objects.equals(new SHA().hash(rawPassword, HashConfig.INSTANCE.getKey()), hashedPassword);
    }

    private Token createUniqueTokenForUser(User user) {
        Token token = new Token(user.getId());
        while (tokenService.existsByTokenValue(token.getValue())) {
            token = new Token(user.getId());
        }
        tokenService.saveToken(token);
        return token;
    }

    private String extractTokenValue(String authorizationHeader) {
        return authorizationHeader.split(" ")[1];
    }

    private String generateVerificationErrorPage() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Email Verification Error</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            background-color: #f4f4f4;
                        }
                        .container {
                            text-align: center;
                        }
                        h1 {
                            color: #d9534f; /* Red color for error message */
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Could not verify your E-Mail</h1>
                    </div>
                </body>
                </html>
                """;
    }

    private String generateVerificationSuccessPage() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Email Verification Success</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            background-color: #f4f4f4;
                        }
                        .container {
                            text-align: center;
                        }
                        h1 {
                            color: #333;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Successfully verified your E-Mail</h1>
                    </div>
                </body>
                </html>
                """;
    }
}
