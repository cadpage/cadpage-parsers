package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;



public class MORayCountyParser extends FieldProgramParser {
  
  public MORayCountyParser() {
    super("RAY COUNTY", "MO", 
          "CALL ADDR CALL/SDS X DATETIME EMPTY END");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@raycounty911.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Call")) return false;
    if (body.endsWith(":")) body += ' ';
    return parseFields(body.split(": ", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.contentEquals("ADDR")) return new MyAddressField();
    if (name.contentEquals("CALL"))  return new MyCallField();
    if (name.contentEquals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d\\d +\\d\\d?:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      String zip = null;
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        zip = match.group(2);
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) data.strCity = zip;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.contentEquals("None")) return;
      super.parse(field, data);
    }
  }
}
