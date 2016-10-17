package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXFortBendCountyBParser extends FieldProgramParser {

  public TXFortBendCountyBParser() {
    super(TXFortBendCountyParser.CITY_CODES, "FORT BEND COUNTY", "TX", 
          "DATETIME! Incident_Number:ID! Incident_Type:CALL! Location:ADDR! Map_Page:MAP! Units:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "stationalerting@fsfd.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("String Match - ")) return false;
    return super.parseFields(body.split("\n+"), 6, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("[EF]\\d{9}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern ADDR_INTERSECT_PTN = Pattern.compile("(.*) - +<.*>");
  private class MyAddressField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - btwn ");
      if (pt >= 0) {
        data.strCross = field.substring(pt+8).trim();
        field = field.substring(0,pt);
      } else {
        Matcher match = ADDR_INTERSECT_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
        } else {
          field = stripFieldEnd(field,  "-");
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
}
