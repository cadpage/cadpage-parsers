package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyGParser extends FieldProgramParser {
  
  public NYNassauCountyGParser() {
    super("NASSAU COUNTY", "NY",
           "ADDR! C/S:X! ADTNL:INFO? HYDRNTS:INFO? TOA:TIME!");
  }
  
  @Override
  public String getFilter() {
    return "alertpaging@pwfd.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("** ALARM: ")) return false;
    String[] parts = subject.split("\\|");
    if (parts.length < 2 || parts.length > 3) return false;
    data.strCall = parts[1].trim();
    if (parts.length == 3) body = '['+ parts[2]+"] " + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile("(.*)\\[ *(.*?), *(\\d{5})\\ *]");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPlace = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strCity = match.group(3);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private static final Pattern X_DIR_PTN = Pattern.compile(" *\\([NSEW]\\) *");
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = X_DIR_PTN.matcher(field).replaceAll(" ").trim();
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
      
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "[");
      field = stripFieldEnd(field, "]");
      super.parse(field, data);
    }
  }
}
