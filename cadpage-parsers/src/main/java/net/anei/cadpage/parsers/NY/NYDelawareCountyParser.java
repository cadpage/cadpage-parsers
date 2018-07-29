package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYDelawareCountyParser extends FieldProgramParser {
  
  public NYDelawareCountyParser() {
    super("DELAWARE COUNTY", "NY", 
          "CALL ADDRCITY ID! CALLER:NAME! END");
  }
  
  @Override
  public String getFilter() {
    return "delco911@co.delaware.ny.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("FLEISCHMANS FD: ")) return false;
    body = body.substring(16).trim();
    return parseFields(body.split("\\|"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("CAD\\d\\d-\\d{6}", true);
    return super.getField(name);
  }
  
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        String zip = match.group(2);
        city = p.getLastOptional(',');
        if (zip != null && city.length() == 0) city = zip;
      }
      data.strCity = city;
      parseAddress(p.get(), data);
    }
  }
}
