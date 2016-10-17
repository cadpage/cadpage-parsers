package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class TNAndersonCountyParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" - ?|- ");
  private static final Pattern STREET_PTN = Pattern.compile("\\+[Ss]treet:");
  private static final Pattern MISSING_DELIM = Pattern.compile("(, TN)([A-Z])");
  
  public TNAndersonCountyParser() {
    super(CITY_LIST, "ANDERSON COUNTY", "TN",
           "( SRC ADDR_X_CALL ID! | CALL CALL+? ADDR/S! +St:X? EMPTY+? CALL2 CALL+? ID )");
  }
  
  @Override
  public String getFilter() {
    return "page@tnacso.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("E911 / ")) body = body.substring(6).trim();
    
    // If page contains times keywords, report as general alert
    if (body.contains(" INSRV:") || body.contains(" INSV:")) return data.parseRunReport(this, body);;
    
    body = STREET_PTN.matcher(body).replaceFirst("+St:");
    body = MISSING_DELIM.matcher(body).replaceFirst("$1 - $2");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " PLACE";
  }

  private static final Pattern SPECIAL_X_PTN = Pattern.compile("^(MM\\d+) +");
  private class MyAddressCrossCallField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf(", TN ");
      if (pt < 0) return false;
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field.substring(0,pt).trim(), data);
      field = field.substring(pt+5).trim();
      if (field.startsWith("+St:")) {
        field = field.substring(4).trim();
        Matcher match = SPECIAL_X_PTN.matcher(field);
        if (match.find()) {
          data.strCross = match.group(1);
          field  = field.substring(match.end());
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field, data);
          field = getLeft();
        }
      }
      if (field.length() == 0) abort();
      data.strCall = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X CALL";
    }
    
  }
  
  // Call field appends to previous call field with - separator
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  // Address field is identified by trailing , TN
  // And needs to replace @ with &
  private class MyAddressField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      boolean good = false;
      if (field.endsWith(" TN")) {
        good = true;
        field = field.substring(0, field.length()-3).trim();
        if (field.endsWith(",")) field = field.substring(0, field.length()-1).trim();
      }
      String city = null;
      int flags = FLAG_ANCHOR_END;
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        good = true;
        flags |= FLAG_NO_CITY;
        city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      if (!good) return false;
      
      field = field.replace('@', '&');
      parseAddress(StartType.START_ADDR, flags, field, data);
      if (city != null) {
        if (city.startsWith("+St:")) {
          city = city.substring(4).trim();
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, city, data);
          data.strCross = getStart();
        } else {
          data.strCity = city;
        }
      }
      return true;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X";
    }
  }
  
  // The info field has to do all kinds of strange things
  private class MyCall2Field extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      
      // This is really the call description
      // and what we thought was the call description is really the department
      data.strSource = data.strCall;
      data.strCall = field;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_X_CALL")) return new MyAddressCrossCallField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ID")) return new IdField("\\d+", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ANDERSONVILLE",
    "BRICEVILLE",
    "CLAXTON",
    "CLINTON",
    "DEVONIA",
    "FORK MOUNTAIN",
    "FRATERVILLE",
    "HEISKELL",
    "LAKE CITY",
    "MARLOW",
    "NORRIS",
    "OAK RIDGE",
    "OLIVER SPRINGS",
    "POWELL",
    "ROSEDALE",
    
    "CLAIRFIELD"  // ???
  };
}
