package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTFlatheadCountyBParser extends FieldProgramParser {
  
  public MTFlatheadCountyBParser() {
    super("FLATHEAD COUNTY", "MT", 
          "For:CALL! From:NAME! PHONE! Pgr_or_ext:EXT! Location:ADDR! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "mt1callbackup@bullittmail.com,mtonecall@montanasky.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("MessageID=")) return false;
    data.strCallId = subject.substring(10).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PHONE")) return new PhoneField(": *(.*)", true);
    if (name.equals("EXT")) return new MyExtField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyExtField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      data.strPhone = append(data.strPhone, " #", field);
    }
  }
  
  private static final Pattern INFO_PTN = Pattern.compile("([A-Za-z ]*?)\\s*:\\s*(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) {
        String info = match.group(2);
        if (info.length() == 0) return;
        field = match.group(1) + ": " + info;
      }
      super.parse(field, data);
    }
  }
}
