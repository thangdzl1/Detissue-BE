package com.DIY.Detissue.controller;

import com.DIY.Detissue.exception.CustomException;
import com.DIY.Detissue.payload.request.SignupRequest;
import com.DIY.Detissue.payload.response.BaseResponse;
import com.DIY.Detissue.service.Imp.UserServiceImp;
import com.DIY.Detissue.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid SignupRequest signupRequest, BindingResult bindingResult) {
        // Get list error
        List<FieldError> listError = bindingResult.getFieldErrors();
        for (FieldError errors : listError) {
            throw new RuntimeException(errors.getDefaultMessage());
        }

        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setData(userServiceImp.addUser(signupRequest));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestParam String username,
            @RequestParam String password
    ) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(token);
        String jwt = jwtHelper.generateToken("Hello login");
        userServiceImp.updateLoginTime(username);

        //Nếu chứng thực thành công sẽ chạy code tiếp theo còn nếu thất bại thì sẽ văng lỗi chưa chứng thực
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setData(jwt);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}