package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * San Luis Obispo County, CA
 */
public class CASanLuisObispoCountyAParser extends FieldProgramParser {
  
  public CASanLuisObispoCountyAParser() {
    super("SAN LUIS OBISPO COUNTY", "CA",
           "RA:SKIP! ADDRCITY X CALL UNK! Map:MAP! ID UNIT! INFO");
  }
  
  @Override
  public String getFilter() {
    return "slucad@fire.ca.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("Close: Inc#")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    return super.parseFields(body.split(";"), data);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      data.strApt = p.getLastOptional('#');
      data.strPlace = p.getOptional('@');
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("<a href=\"http://maps.google.com/\\?q=([-+0-9\\.,]+)\">");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) {
        data.strGPSLoc = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("Inc# +(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "SL",     "SAN LUIS OBISPO",
      "SLO_CO", ""
  });
}
