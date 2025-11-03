package net.anei.cadpage.parsers.DC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DCFireAndEMSParser extends FieldProgramParser {
  
  public DCFireAndEMSParser() {
    super(CITY_LIST, "", "DC", 
          "Incident_Notification:EMPTY! Incident_#:ID! Type:CODE! Description:CALL! Location:ADDR/S! " + 
              "UNID:SKIP! Units_Assigned:UNIT! Time:TIME! Lat/Lon:GPS! Comments:EMPTY! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "automated_dwfemsdev@dc.go";
  }

  @Override
  public String getLocName() {
    return "Fire and EMS, DC";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("</li><li>", "\n");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_BRK_PTN = Pattern.compile(" *[,:][@ ]*");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|LOT|ROOM|RM) +(.*)|\\d{1,4}[A-Z]?|[A_Z]");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String part : ADDR_BRK_PTN.split(field)) {
        Matcher match;
        if (data.strAddress.isEmpty()) {
          super.parse(part, data);
        } else if ((match = ADDR_APT_PTN.matcher(part)).matches()) {
          String apt = match.group(1);
          if (apt == null) apt = part;
          data.strApt = append(data.strApt, "-", apt);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" | ", ",");
      super.parse(field, data);
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("(\\d\\d)(\\d\\d) *hrs");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1)+':'+match.group(2);
    }
  }
  
  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d\\d\\d *hrs +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "<li>");
      field = stripFieldEnd(field, "</li>");
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final String[] CITY_LIST = new String[] {"DC"};
}