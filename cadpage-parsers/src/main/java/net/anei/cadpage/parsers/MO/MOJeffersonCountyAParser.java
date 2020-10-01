package net.anei.cadpage.parsers.MO;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJeffersonCountyAParser extends FieldProgramParser {

  String address;
  String callerAddress;
 
  public MOJeffersonCountyAParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "MO",
          "( SELECT/R Times_For:SRC! Event_Number:ID! Call_Entered:SKIP! Event_Type:CALL! INFO/RN+ " +
          "| Location:ADDR/S? EID:ID? TYPE_CODE:CALL! TIME:TIME? CALLER_ADDR:CADDR? Comments:MAP? Disp:UNIT% )");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.startsWith("EVENT TIMES FOR ")) {
      setSelectValue("R");
      return super.parseFields(body.split("\n"), data);
    }
    
    setSelectValue("");
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
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  private static final Pattern X_PREFIX_PTN = Pattern.compile("(?:X|X-|X-ST) +(.*)");
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
        } else if ((match = X_PREFIX_PTN.matcher(tmp)).matches()) {
          data.strCross = match.group(1);
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
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" \t", " ").replaceAll("\t", "  ");
      super.parse(field, data);
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
    "BARNHART",         "BARNHART",
    "BYRNES MILL",      "BYRNES MILL",
    "CEDARHILL",        "CEDAR HILL",
    "CRYSTALCT",        "CRYSTAL CITY",
    "CRYSTAL CITY",     "CRYSTAL CITY",
    "DESOTO",           "DESOTO",
    "DITTMER",          "DITTMER",
    "FENTON",           "FENTON",
    "FESTUS",           "FESTUS",
    "HERCU",            "HERCULANEUM",
    "HERCULANEUM",      "HERCULANEUM",
    "HIGH RIDGE",       "HIGH RIDGE",
    "HILLSBORO",        "HILLSBORO",
    "HOUSESPRG",        "HOUSE SPRINGS",
    "HOUSE SPRINGS",    "HOUSE SPRINGS",
    "IMPERIAL",         "IMPERIAL",
    "JEFFERSON COUNTY", "",
    "KIMMSWICK",        "KIMMSWICK",
    "OLYMPIAN VILLAGE", "OLYMPIAN VILLAGE",
    "PEVELY",           "PEVELY",
  });
}