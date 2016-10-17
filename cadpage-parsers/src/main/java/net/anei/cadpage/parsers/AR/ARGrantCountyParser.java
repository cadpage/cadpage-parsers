package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class ARGrantCountyParser extends FieldProgramParser {
  
  public ARGrantCountyParser() {
    super("GRANT COUNTY", "AR",
          "E:ID! ET:CALL! LOC:ADDR! T:TIME! N:NAME! NAME/CS+ C:INFO! INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "grantcounty911@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("Times -")) data.msgType = MsgType.RUN_REPORT;
    return parseFields(body.split(", "), data);
  }
  
  @Override
  public Field getField(String name) {
    if(name.equals("ID")) return new MyIdField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d?:\\d\\d?", true);
    return super.getField(name);
  }

  private static final Pattern ID_PTN = Pattern.compile("([A-Z]{2,5})\\d{9,10}");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1);
      data.strCallId = field;
    }
    
    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      while (true) {
        int pt = field.lastIndexOf('[');
        if (pt < 0) break;
        String part = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (part.startsWith(":")) {
          apt = append(part.substring(1).trim(), "-", apt);
        } else {
          data.strPlace = append(stripFieldStart(part, "@"), " - ", data.strPlace);
        }
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT";
    }
  }
}
