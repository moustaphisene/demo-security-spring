package sen.bank.demospring6security.web;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sen.bank.demospring6security.constant.AppConstants;
import sen.bank.demospring6security.entity.LoginRequestDTO;
import sen.bank.demospring6security.entity.LoginResponseDTO;
import sen.bank.demospring6security.entity.Mandate;
import sen.bank.demospring6security.repository.MandateRepository;
import sen.bank.demospring6security.services.MandateService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final MandateRepository mandateRepository;
    private final PasswordEncoder passwordEncoder;
    private final MandateService mandateService;
    private final AuthenticationManager authenticationManager;
    private final Environment environment;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Mandate mandate){
        try {
            String hashPwd = passwordEncoder.encode(mandate.getPwd());
            mandate.setPwd(hashPwd);
            Mandate saveMandate = mandateRepository.save(mandate);
            if (saveMandate.getId()>0){
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("User details are succefully registered");

            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occured:" + e.getMessage());
        }
    }

//    @RequestMapping("/user")
//    public Mandate getUserDetailsAfterLogin(Authentication authentication) {
//        Optional<Mandate> optionalMandate = MandateRepository.findByEmail(authentication.getName());
//        return optionalMandate.orElse(null);
//    }

//    @GetMapping("/user")
//    public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication) {
//        return mandateRepository.findByEmail(authentication.getName())
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("User not found with email: " + authentication.getName()));
//    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Mandate> optional = mandateRepository.findByEmail(authentication.getName());

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with email: " + authentication.getName());
        }
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> apiLogin (@RequestBody LoginRequestDTO loginRequest) {
        String jwt = "";
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(),
                loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
            if (null != environment) {
                String secret = environment.getProperty(AppConstants.jwt_secret_key,
                        AppConstants.jwt_secret_default_value);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                jwt = Jwts.builder().issuer("SENE BANK").subject("JWT Token")
                        .claim("username", authenticationResponse.getName())
                        .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new java.util.Date())
                        .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                        .signWith(secretKey).compact();
            }
        }
        return ResponseEntity.status(HttpStatus.OK).header(AppConstants.jwt_header,jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }


}
