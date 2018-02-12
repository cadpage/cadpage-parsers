package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMuskingumCountyBParser extends FieldProgramParser {
  
  public OHMuskingumCountyBParser() {
    super("MUSKINGUM COUNTY", "OH", 
          "SequenceNumber:ID! Nature:CALL! Talkgroup:CH! FreeFormatAddress:ADDR_CITY_X! XCoordinate:GPS1! YCoordinate:GPS2! CAD_Zone:MAP! ( Units:UNIT! UNIT/S+ | ) Notes:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "notif@mecc911.org";
  }
  
  private static final Pattern HTML_TR_TD_PTN = Pattern.compile("(?:</?t[rd]>)+", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = HTML_TR_TD_PTN.matcher(body).replaceAll("\n");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseFields(body.split("\n+"), data);
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
      while (field.endsWith(",")) {
        field = field.substring(0,field.length()-1).trim();
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
      String addr = p.get();
      addr = stripFieldEnd(addr, "#");
      parseAddress(addr, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY X";
    }
  }
}
