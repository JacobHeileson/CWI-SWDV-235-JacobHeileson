package edu.cwi;

import java.util.Map;
import java.util.UUID;
import java.text.DecimalFormat;

public record UserAccount (UUID id, String name, AppRole role, Double balance, Map<String, String> transactions) {

    public String formatBalance() {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        return df.format(this.balance);
    }
}
