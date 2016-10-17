package net.anei.cadpage.parsers.ID;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class IDKootenaiCountyParser extends FieldProgramParser {
  
  public IDKootenaiCountyParser() {
    super(CITY_CODES, "KOOTENAI COUNTY", "ID",
          "SRC CALL ADDR UNIT! UNIT/S+? INFO/N+? CH INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "911alert@kcgov.us,777";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "KOOTENAI COUNTY SHERIFF");
    body = stripFieldStart(body, ":");
    int pt = body.indexOf("\nSent by CLI");
    if (pt < 0) pt = body.indexOf("\n0000 Confirm");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    return parseFields(body.split("\n"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new  MyAddressField();
    if (name.equals("UNIT")) return new UnitField("(?!OPS)(?:\\d?[A-Z]|[A-Z]+\\d+[A-Z]*|[A-Z]*DOL[A-Z]*|\\d{1,4})", true);
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      parseAddress(p.get(';'), data);
      String place = p.get();
      if (place.startsWith("#")) {
        data.strApt = append(data.strApt, "-", place.substring(1).trim());
      } else {
        data.strPlace = place;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Pattern CH_PTN = Pattern.compile("OPS *(?:CHANNEL +)?([^ ]+) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyChannelField extends ChannelField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CH_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strChannel = "OPS" + match.group(1);
      data.strSupp = append(data.strSupp, "\n", match.group(2));
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  private static Pattern GPS_PTN = Pattern.compile("CALLBACK=([-0-9\\(\\)]+) LAT=([-+]?\\d+\\.\\d{4,}) LON=([-+]?\\d+\\.\\d{4,}) UNC=\\d+");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
        return;
      }
      
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ATH", "ATHOL",
      "BA",  "BAYVIEW",
      "BLA", "BLANCHARD",
      "CDA", "COEUR D'ALENE",
      "DG",  "DALTON GARDENS",
      "FL",  "FERNAN LAKE",
      "HA",  "HAYDEN",
      "HAR", "HARRISON",
      "HAU", "HAUSER",
      "HL",  "HAYDEN LAKE",
      "KEL", "KELLOGG", 
      "MOS", "MOSCOW",
      "PF",  "POST FALLS",
      "RA",  "RATHDRUM",
      "RL",  "ROSE LAKE",
      "SL",  "SPIRIT LAKE"
  });
}
