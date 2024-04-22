package redlib.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.NeedNoPrivilege;
import redlib.backend.annotation.Privilege;
import redlib.backend.model.Token;
import redlib.backend.service.TokenService;
import redlib.backend.utils.ThreadContextHolder;

@RestController
@RequestMapping("/api/authentication")
@BackendModule({"page:页面"})
public class AuthenticationController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    @NeedNoPrivilege
    public Token login(String userId, String password, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        ipAddress = ipAddress.replace("[", "").replace("]", "");
        Token token = tokenService.login(userId, password, ipAddress, request.getHeader("user-agent"));
        Cookie cookie = new Cookie("accessToken", token.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setAttribute("hhh","test");
//        cookie.setValue(token.getAccessToken() + "|testValue");

//        Cookie customInfoCookie = new Cookie("customInfo", "testValue");
//        customInfoCookie.setPath("/");
//        customInfoCookie.setHttpOnly(true);
//        response.addCookie(customInfoCookie);
//
//
        response.addCookie(cookie);
//        response.addCookie(customInfoCookie);
        return token;
    }

    @GetMapping("getCurrentUser")
    @Privilege
    public Token getCurrentUser() {
        return ThreadContextHolder.getToken();
    }

    @GetMapping("logout")
    @Privilege
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("logout!");

        Cookie[] cookies = request.getCookies();

        if (cookies != null){
            for (Cookie cookie : cookies){
//                System.out.println((String)cookie.getValue());
                if ("accessToken".equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
    }

    @GetMapping("ping")
    @Privilege
    public void ping() {
    }

    @GetMapping("checkSession")
    @NeedNoPrivilege
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        // 获取请求中的所有Cookie
        Cookie[] cookies = request.getCookies();
        // 检查是否存在accessToken Cookie以及其值是否有效
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName()) &&
                        tokenService.isTokenValid(cookie.getValue())) {
                    return ResponseEntity.ok().body("Session is active");
                }
            }
        }
        // 如果没有有效的accessToken，返回未授权的响应
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Session is inactive");
    }
}
