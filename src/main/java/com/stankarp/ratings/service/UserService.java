package com.stankarp.ratings.service;

import com.stankarp.ratings.entity.User;
import com.stankarp.ratings.message.request.LoginForm;
import com.stankarp.ratings.message.request.RoleForm;
import com.stankarp.ratings.message.request.SignUpForm;
import com.stankarp.ratings.message.response.JwtResponse;
import com.stankarp.ratings.message.response.ResponseMessage;

public interface UserService {

    JwtResponse authenticateUser(LoginForm loginRequest);

    ResponseMessage registerUser(SignUpForm signUpRequest);

    User revoke(RoleForm roleForm);

    User grand(RoleForm roleForm);

}
