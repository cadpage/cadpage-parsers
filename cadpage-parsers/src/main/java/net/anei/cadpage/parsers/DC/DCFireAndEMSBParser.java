package net.anei.cadpage.parsers.DC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DCFireAndEMSBParser extends FieldProgramParser {
  
  public DCFireAndEMSBParser() {
    super("DC", "",
          "Incident_#:ID! Type:CODE! Description:CALL! Location:ADDRCITY/S! Time:DATETIME! XY:GPS! END");
    setupCities(new String[]{"DC"});
  }

  @Override
  public String getLocName() {
    return "Fire and EMS, DC";
  }

  @Override
  public String getFilter() {
    return "noreply@everbridge.net";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("(CAD MSG)")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|STE) +(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional('@');
      String apt2 = p.getLastOptional(':');
      String apt1 = p.getLastOptional(',');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", cleanApt(apt1));
      data.strApt = append(data.strApt, "-", cleanApt(apt2));
    }
    
    private String cleanApt(String apt) {
      if (!apt.isEmpty()) {
        Matcher match = APT_PTN.matcher(apt);
        if (match.matches()) apt = match.group(1);
      }
      return apt;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
}
