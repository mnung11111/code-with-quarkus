package org.acme.login;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.io.InputStream;

@Path("/")
public class AuthResource {

    // 1. 로그인 페이지를 보여주는 엔드포인트 (GET)
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public Response loginPage() {
        InputStream html = getClass()
                .getClassLoader()
                .getResourceAsStream("META-INF/resources/login/login.html");
        
        if (html == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(html).build();
    }

    // 2. 로그인 폼 데이터를 처리하는 엔드포인트 (POST)
    @POST
    @Path("/login_check")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // 폼 데이터 수신 형식
    public Response loginCheck(
            @FormParam("username") String username, // HTML의 name="username"과 매칭
            @FormParam("password") String password) { // 소괄호 닫고 중괄호 시작

        // [임시] 일단 로그인 성공 처리 (DB 체크 전)
        // 성공 시 특정 HTML 페이지로 리다이렉트 시킴
        return Response
                .seeOther(URI.create("/login/main_after_login.html")) // 303 See Other: GET으로 전환하여 이동
                .build(); // 세미콜론 잊지 마세요!
    } // loginCheck 메서드 끝

} // AuthResource 클래스 끝