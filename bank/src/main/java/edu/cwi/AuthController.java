package edu.cwi;

import io.javalin.http.Context;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthController {
    
    record Pair(String a, String b) {}

    private static final Map<Pair, UserAccount> userMap = Map.of(
        new Pair("tbrady", "password"), new UserAccount(UUID.randomUUID(), "Tom Brady", AppRole.CUSTOMER, 750.00, Map.of("1", "Deposited $750.00")),
        new Pair("kmoore", "password"), new UserAccount(UUID.randomUUID(), "Kellen Moore", AppRole.CUSTOMER, 500.00, Map.of("1", "Deposited $500.00"))
    );

    public static Optional<UserAccount> getUser(String id) {
        Collection<UserAccount> accounts = userMap.values();

        for (UserAccount account : accounts) {

            if (account.id().toString().equals(id)) {
                return Optional.of(account);
            }
        }

        return Optional.empty();
    }

    public static void authenticate(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        Pair credentials = new Pair(username, password);
        UserAccount user = userMap.get(credentials);

        if(user != null) {
        
            ctx.cookieStore().set("user-id", user.id().toString());
            ctx.cookieStore().set("user-role", user.role());
            ctx.redirect("/account");
        }
        else {
            ctx.cookieStore().clear();
            ctx.redirect("/unauthorized.html");
        }
    }

    public static void logout(Context ctx) {
        ctx.cookieStore().clear();
        ctx.redirect("/");
    }
}
