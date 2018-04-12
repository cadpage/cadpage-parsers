package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class TNBradleyCountyParser extends DispatchA3Parser {
  
  private static final Pattern MARKER = Pattern.compile("CLEVELANDTN911:? Bradley CO 911:\\* +|911:\\*\\s*|CLEVELANDTN911: Bradley CO 911:\\*\\s*");
  
  public TNBradleyCountyParser() {
    super(MARKER, "BRADLEY COUNTY", "TN",
        "ADDR UNK UNK UNK UNK UNK UNK INFO1 CALL UNK UNK UNK UNK UNK UNK UNK INFO+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Bradley CO 911:*")) {
      body = "CLEVELANDTN911: " + body.replace("*", "* ");
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String getFilter() {
    return "777,93001";
  }
}
