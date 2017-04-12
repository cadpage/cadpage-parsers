package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYSuffolkCountyGParser extends FieldProgramParser {
  
  public NYSuffolkCountyGParser() {
    super("SUFFOLK COUNTY", "NY",
          "CALL! TOA:TIMEDATE/d! ADDR! PLACE APT CS:X SRC ID INFO+? UNIT UNIT+");
    setupMultiWordStreets("INDIAN HEAD");
  }
  
  @Override
  public String getFilter() {
    return "commackfd@gmail.com,commackfd2@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds =  body.split("\n");
    if (flds.length > 1) return parseFields(body.split("\n"), 3, data);
    return parseNewFormat(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "'");
      field = stripFieldEnd(field, "\"");
      Parser p = new Parser(field);
      String state = p.getLast(',');
      if (!state.equals("NY")) abort();
      data.strCity = p.getLast(',');
      String address = p.get();
      if (address.length() == 0) abort();
      parseAddress(address, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  private class MyUnitField extends InfoField {
    public MyUnitField() {
      super("[A-Z]+-?\\d+", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?) TOA: (\\d\\d:\\d\\d) (\\d\\d-\\d\\d-\\d\\d) ([^a-z]*?) (?:CS: ([^a-z]*?) )?(\\S*[a-z].*?) (\\d{4}-\\d{6})");
  
  private boolean parseNewFormat(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    setFieldList("CALL TIME DATE ADDR APT PLACE X SRC ID");
    data.strCall = match.group(1).trim();
    data.strTime = match.group(2);
    data.strDate = match.group(3).replace('-', '/');
    parseAddress(StartType.START_ADDR, match.group(4).trim(), data);
    data.strPlace = getLeft();
    String cross = getOptGroup(match.group(5));
    cross = stripFieldEnd(cross, "/");
    cross = stripFieldStart(cross, "/");
    data.strCross = cross;
    data.strSource = match.group(6).trim();
    data.strCallId = match.group(7);
    return true;
  }
}
