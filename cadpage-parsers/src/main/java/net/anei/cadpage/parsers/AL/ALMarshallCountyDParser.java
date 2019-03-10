package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALMarshallCountyDParser extends FieldProgramParser {
  
  public ALMarshallCountyDParser() {
    super("MARSHALL COUNTY", "AL", 
          "ID CALL ADDRCITY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD DISPATCH") && !subject.equals("CAD INCIDENT")) return false;
    body = stripFieldStart(body,  "1/1:");
    String[] flds = body.split("\n");
    if (flds.length < 3) flds = body.split("\\|");
    return parseFields(flds, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CAD #(\\d{8}-\\d{6}):", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?)(?: (?:APT|RM|LOT) +(.*?))?(?: *\\[(.*)\\])?(?: *\\(([-+]?[.0-9]+, *[-+]?[.0-9]+)\\))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      String apt = null;
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
        data.strUnit = getOptGroup(match.group(3));
        setGPSLoc(getOptGroup(match.group(4)), data);
      }
      super.parse(field, data);
      if (apt != null) data.strApt = append(data.strApt, "-", apt.trim());
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT GPS";
    }
  }
  
  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) +([-A-Z0-9]+)\\b[ .;]*(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "APT " + super.getFieldNames();
    }
  }
}
