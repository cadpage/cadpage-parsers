package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.general.GeneralParser;

public class OKTulsaBParser extends GeneralParser {
  
  public OKTulsaBParser() {
    super("TULSA", "OK");
    setFieldList("CALL ADDR APT X INFO");
  }
  
  @Override
  public String getFilter() {
    return "mail.sender@cityoftulsa.org";
  }
  
  private static final Pattern MDOT_PTN = Pattern.compile("\\.{2,}");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!isPositiveId()) return false;
    body = MDOT_PTN.matcher(body).replaceAll("\n");
    return super.parseMsg(subject, body, data);
  }
}
