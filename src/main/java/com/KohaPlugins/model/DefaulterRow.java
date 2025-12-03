package com.KohaPlugins.model;

public class DefaulterRow {
    private String cardnumber;
    private String borrower_no;
    private String def_name;

    public DefaulterRow() {}

    public DefaulterRow(String cardnumber, String patNo, String firstname) {
        this.cardnumber = cardnumber;
        this.borrower_no = patNo;
        this.def_name = firstname;
    }

    public String getCardnumber() { return cardnumber; }
    public void setCardnumber(String cardnumber) { this.cardnumber = cardnumber; }

    public String getborrower_no() { return borrower_no; }
    public void setborrower_no(String patNo) { this.borrower_no = patNo; }

    public String getdef_name() { return def_name; }
    public void setdef_name(String firstname) { this.def_name = firstname; }
}
