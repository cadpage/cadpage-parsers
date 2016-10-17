package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMuskingumCountyBParser extends FieldProgramParser {
  
  public OHMuskingumCountyBParser() {
    super("MUSKINGUM COUNTY", "OH", 
          "CALL ADDR_CITY_X! END");
  }
  
  @Override
  public String getFilter() {
    return "notif@mecc911.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    // Strip out leading garbage
    if (!body.startsWith("zvmg.biz,")) return false;
    int pt = body.indexOf("\n\n<HTML>");
    if (pt < 0) return false;
    body = body.substring(pt+2).trim();
    
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_CITY_X")) return new MyAddressCityCrossField(); 
    return super.getField(name);
  }
  
  private Pattern ADDR_ZIP_PTN = Pattern.compile("\\d{5}"); 
  private class MyAddressCityCrossField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.lastIndexOf('(');
        if (pt >= 0) {
          String cross = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0,pt).trim();
          cross = stripFieldStart(cross, "/");
          cross = stripFieldEnd(cross, "/");
          data.strCross = cross;
        }
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.length() > 0) {
        data.strCity = city;
        if (ADDR_ZIP_PTN.matcher(city).matches()) {
          city = p.getLastOptional(',');
          if (city.length() > 0) data.strCity = city;
        }
      }
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY X";
    }
  }
}
