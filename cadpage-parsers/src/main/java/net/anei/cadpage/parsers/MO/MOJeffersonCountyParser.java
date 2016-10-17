package net.anei.cadpage.parsers.MO;


import java.util.Properties;
import java.util.regex.Matcher;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJeffersonCountyParser extends FieldProgramParser {

  String address;
  String callerAddress;
 
  public MOJeffersonCountyParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "MO",
          "Location:ADDR/S? TYPE_CODE:CALL! CALLER_ADDR:CADDR? Comments:INFO? Disp:UNIT%");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    address = callerAddress = null;
    if (!super.parseMsg(body, data)) return false;
    if (address  == null) {
      address = callerAddress;
      callerAddress = null;
    }
    if (address == null) return true;
    
    if (callerAddress == null) {
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, address, data);
    } else {
      String callerAddress2 = callerAddress.replace(", ", " ");
      Result res1 = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END | FLAG_CHECK_STATUS, address);
      Result res2 = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END | FLAG_CHECK_STATUS, callerAddress2);
      int stat1 = res1.getStatus();
      if (stat1 == STATUS_INTERSECTION) stat1++;
      if (stat1 > res2.getStatus()) {
        res1.getData(data);
        data.strSupp = append(data.strSupp, "\n", "Caller Addr:" + callerAddress);
      }
      else if (res2.getStatus() > stat1) {
        res2.getData(data);
        address = stripFieldStart(address, "@");
        data.strPlace = append(address, " - ", data.strPlace);
      }
      else {
        res2.getData(data);
        String addr2 = data.strAddress;
        String city2 = data.strCity;
        data.strAddress = data.strCity = "";
        res1.getData(data);
        if (data.strAddress.equals(addr2)) {
          if (data.strCity.length() == 0) data.strCity = city2;
        } else {
          data.strSupp = append(data.strSupp, "\n", "Caller Addr:" + callerAddress);
        }
      }
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CADDR")) return new MyCallerAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      
      // If address contains GPS coordinates, make sure skip over any
      // colons it might contain
      int pt = 0;
      Matcher match = GPS_PATTERN.matcher(field);
      if (match.find()) pt = match.end();
      
      field = field.replace(";", ":");
      pt = field.indexOf(':', pt);
      if (pt >= 0) {
        String tmp = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (tmp.startsWith("@")) {
          data.strPlace = tmp.substring(1).trim();
        } else if (tmp.startsWith("X-ST ")) {
          data.strCross = tmp.substring(5).trim();
        } else if (tmp.startsWith("APT")) {
          data.strApt = tmp.substring(3).trim();
        } else if (tmp.startsWith("UNIT ")) {
          data.strApt = tmp;
        } else if (isValidAddress(tmp)) {
          data.strCross = tmp;
        } else if (tmp.contains(" ") || tmp.length() > 4) {
          data.strPlace = tmp;
        } else {
          data.strApt = tmp;
        }
      }
      address = field;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X APT PLACE";
    }
  }
  
  private class MyCallerAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0) callerAddress = field;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY INFO";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
    "ARN",  "ARNOLD",
    "BM",   "BYRNES MILL",
    "HB",   "HILLSBORO",
    "DES",  "DESOTO",
    "CNTY", "",
    "FES",  "FESTUS",
    "HERC", "HERCULANEUM",
    "PEV",  "PEVELY",
    "CCTY", "CRYSTAL CITY",
    "KMM",  "KIMMSWICK",
    "OLYV", "OLYMPIAN VILLAGE",
    "SLCO", "ST LOUIS COUNTY",
    "WACO", "",   // ???

    "ARNOLD",           "ARNOLD",
    "BYRNES MILL",      "BYRNES MILL",
    "HILLSBORO",        "HILLSBORO",
    "DESOTO",           "DESOTO",
    "JEFFERSON COUNTY", "",
    "FESTUS",           "FESTUS",
    "HERCULANEUM",      "HERCULANEUM",
    "PEVELY",           "PEVELY",
    "CRYSTAL CITY",     "CRYSTAL CITY",
    "KIMMSWICK",        "KIMMSWICK",
    "OLYMPIAN VILLAGE", "OLYMPIAN VILLAGE",
    
    "FENTON",           "FENTON",
    "HIGH RIDGE",       "HIGH RIDGE",
    "HOUSE SPRINGS",    "HOUSE SPRINGS"
    

  });
}