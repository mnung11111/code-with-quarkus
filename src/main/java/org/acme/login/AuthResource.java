package org.acme.login;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.io.InputStream;
import jakarta.inject.Inject;
import io.vertx.ext.web.RoutingContext;



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
    @Inject
    RoutingContext context;
    
    @POST // 아이디, 패스워드 전송받음
    @Path("/login_check")
    @Transactional
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginCheck(
        @FormParam("username") String username,
        @FormParam("password") String password) {
        User user = User.findByUsername(username); // 아이디 조회
            if (user == null || !user.password.equals(password)) { // 존재 확인
        return Response
            .seeOther(URI.create("/login?error=1"))
            .build();
        }
        // 세션에 로그인 정보 저장
        context.session().put("loginUser", username);
        return Response
            .seeOther(URI.create("/after_login"))
            .build();
    }

    @GET
@Path("/after_login")
@Produces(MediaType.TEXT_HTML)
public Response afterLogin() {
// 세션 체크: 로그인 안 한 사용자 차단
String loginUser = context.session().get("loginUser");
// 세션 내용 로그 출력
System.out.println("=== 세션 ID : " + context.session().id());
System.out.println("=== loginUser : " + loginUser);
if (loginUser == null) {
// 세션 없음 → 로그인 페이지로 강제 이동
return Response
.seeOther(URI.create("/login"))
.build();
}
// 세션 있음 → 로그인 후 HTML 반환
InputStream html = getClass()
.getClassLoader()
.getResourceAsStream("META-INF/resources/login/main_after_login.html");
return Response.ok(html).build();
}

@GET
@Path("/logout")
public Response logout() {
// 로그아웃 전 세션 정보 출력
System.out.println("=== 로그아웃 전 세션 ID : " + context.session().id());
System.out.println("=== 로그아웃 전 loginUser : " + context.session().get("loginUser"));
// 세션 전체 삭제
context.session().destroy();
// 로그아웃 후 세션 정보 출력
System.out.println("=== 로그아웃 후 세션 ID : " + context.session().id());
System.out.println("=== 로그아웃 후 loginUser : " + context.session().get("loginUser"));
return Response
.seeOther(URI.create("/"))
.build();
}
// AuthResource.java 아래 새로 추가
@GET
@Path("/register")
@Produces(MediaType.TEXT_HTML)
public Response registerPage() {
InputStream html = getClass()
.getClassLoader()
.getResourceAsStream(
"META-INF/resources/login/register.html");
return Response.ok(html).build();
}

} // AuthResource 클래스 끝

