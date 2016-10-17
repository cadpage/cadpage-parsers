package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VASouthamptonCountyBParser extends FieldProgramParser {
  
  public VASouthamptonCountyBParser() {
    super(VASouthamptonCountyParser.CITY_LIST, "SOUTHAMPTON COUNTY", "VA", 
          "ADDR/S EMPTY EMPTY EMPTY TIME CALL! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "southampton911@shso.org";
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*) OCA: (\\d{4}-\\d{6})");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strCallId = match.group(2);
    }
    return parseFields(body.split(";"), 6, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" SOTH", " ");
      super.parse(field, data);
    }
  }
}
