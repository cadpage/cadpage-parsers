package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchCiscoParser extends FieldProgramParser {
  
  boolean citySearch;
  
  public DispatchCiscoParser(String defCity, String defState) {
    this((String[])null, defCity, defState);
  }
  
  public DispatchCiscoParser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, PROGRAM_STR);
    citySearch = cityCodes != null;
  }
  
  public DispatchCiscoParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, PROGRAM_STR);
    citySearch = cityList != null;
  }
  private static final String PROGRAM_STR = "Ct:CALL! Loc:ADDR/S! Apt:APT XSt:X? Grid:MAP Units:UNIT Rmk:INFO";

  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SPECIAL_MSG_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT INFO");
      data.strUnit = match.group(1);
      String type = match.group(2).trim();
      String msg =  match.group(3).trim();
      if (type.equals("Is Clearing from")) {
        data.msgType = MsgType.RUN_REPORT;
        data.strSupp = type + " -> " + msg;
        return true;
      }
      if (type.equals("Notification")) {
        data.msgType = MsgType.GEN_ALERT;
        data.strSupp = msg;
        return true;
      }
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = type + " -> " + msg;
      return true;
    }

    if (subject.length() > 0 && !body.startsWith("Ct:")) {
      body = "Ct: " + subject + ' ' + body;
    }
    return super.parseMsg(body, data);

  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("X")) return new BaseCrossField();
    return super.getField(name);
  }
  
  private static final Pattern SPECIAL_MSG_PTN = Pattern.compile("Unit: *([A-Z0-9]+) *(.*?) -> (.*)");
  
  private class BaseUnitField extends UnitField {
    @Override 
    public void parse(String field, Data data) {
      super.parse(field.replaceAll(" +", " "), data);
    }
  }
  
  private static final Pattern D_PTN = Pattern.compile("\\bD\\b", Pattern.CASE_INSENSITIVE);
  protected class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field =  D_PTN.matcher(field).replaceAll("DR");
      super.parse(field, data);
      
      // Check for city name behind the first street of an intersection
      if (citySearch) {
        int pt = data.strAddress.indexOf('&');
        if (pt >= 0) {
          String addr1 = data.strAddress.substring(0,pt).trim();
          String addr2 = data.strAddress.substring(pt+1).trim();
          data.strAddress = "";
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr1, data);
          data.strAddress = append(data.strAddress, " & ", addr2);
        }
      }
    }
  }
  
  protected class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {

      // Abbreviating DR as D seems unusually common ??
      field =  D_PTN.matcher(field).replaceAll("DR");

      // Generally, but not always, cross street separators are not entered
      // If we have separators, go ahead and use them
      field = field.replaceAll("  +", " / ");
      if (field.contains("/")) {
        super.parse(field, data);
        return;
      }
      
      int crossFlags = FLAG_ONLY_CROSS | getExtraParseAddressFlags();
      
      // If we have city codes, try to use them to split out two cross streets
      if (citySearch) {
        String saveCity = data.strCity;
        data.strCity = "";
        data.strCross = "";
        parseAddress(StartType.START_ADDR, crossFlags | FLAG_ONLY_CITY | FLAG_CROSS_FOLLOWS, field, data);
        String cross = data.strCross;
        if (cross.length() == 0 || data.strCity.length() == 0) {
          data.strCity = saveCity;
        } else {
          data.strCross = "";
          parseAddress(StartType.START_ADDR, crossFlags | FLAG_ONLY_CITY | FLAG_ANCHOR_END, getLeft(), data);
          data.strCross = append(cross, " / ", data.strCross);
          if (data.strCross.contains("/")) return;
          
          // Even if this failed, it might have stripped a city code from the very
          // end of the address, so we will use the stripped result for any future
          // logic
          field = data.strCross;
        }
      }
      
      // No luck.  See if a specialized subclass will handle this
      if (parseSpecial(field, data)) return;

      // Next try the mundane old fashioned parse implied intersections logic
      parseAddress(StartType.START_ADDR, crossFlags | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
      if (data.strCross.contains("/")) return;
      
      // Still no luck.  Try picking off a leading cross street and adding a slash
      // at the end of it
      data.strCross = "";
      parseAddress(StartType.START_ADDR, crossFlags | FLAG_CROSS_FOLLOWS, field, data);
      data.strCross = append(data.strCross, " / ", getLeft());
      if (data.strCross.contains("/")) return;
      
      // Still no luck, try to find a cross street construct at the end of the field
      // and insert a slash ahead of it
      data.strCross = "";
      parseAddress(StartType.START_OTHER, crossFlags | FLAG_ANCHOR_END, field, data);
      data.strCross = append(getStart(), " / ", data.strCross);
    }

    protected boolean parseSpecial(String field, Data data) {
      return false;
    }
  }
}
