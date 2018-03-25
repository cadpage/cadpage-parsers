package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class WIGrantCountyParser extends FieldProgramParser {
  
  public WIGrantCountyParser() {
    super("GRANT COUNTY", "WI", 
          "ID! T:CALL! A:ADDR_CITY_ST_ZIP! L:PLACE_APT! P:NAME! M:NONE! N:PLACE! D:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "noreply@co.grant.wi.gov";
  }
  
  private static final Pattern RR_PTN = Pattern.compile("(?:(.*?) )?Description of Call (.*?) Location of Call (.*?) Nearest intersection if known: (.*?) Dispatcher notes: *(.*?) (Unit response times:) (.*)");
  private static final Pattern TIME_BRK_PTN = Pattern.compile("; +| +(?=Time Call Was Recieved:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = RR_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("PLACE CALL APT ADDR CITY ST X INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strPlace = getOptGroup(match.group(1));
      data.strCall = match.group(2);
      parseAddress(true, match.group(3).trim(), data);
      data.strCross = removeNone(match.group(4).trim());
      String info = match.group(5).trim();
      data.strSupp = match.group(6) + '\n' + TIME_BRK_PTN.matcher(match.group(7)).replaceAll("\n");
      parseInfo(info, data);
      return true;
    }
    
    if (body.startsWith("# ")) {
      body = body.substring(2).trim();
      body = stripFieldEnd(body, " In Progress");
      return super.parseMsg(body, data);
    }
    
    return false;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d\\d-\\d{5}", true);
    if (name.equals("ADDR_CITY_ST_ZIP")) return new MyAddressCityStateZipField();
    if (name.equals("PLACE_APT")) return new MyPlaceAptField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("NONE")) return new SkipField("None");
    return super.getField(name);
  }
  
  private class MyAddressCityStateZipField extends Field {
    @Override
    public void parse(String field, Data data) {
      parseAddress(false, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY ST";
    }
  }
  
  private class MyPlaceAptField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      parsePlaceApt(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      parseInfo(field, data);
    }
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*), *([A-Z]{2})(?: (\\d{5}))?");
  
  private void parseAddress(boolean leadApt, String addr, Data data) {
    String zip = null;
    Matcher match = ADDR_ST_ZIP_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      data.strState =  match.group(2);
      zip = match.group(3);
    }
    
    int pt = addr.lastIndexOf(',');
    if (pt >= 0) {
      data.strCity = addr.substring(pt+1).trim();
      addr = addr.substring(0,pt).trim();
    }
    
    if (leadApt) {
      if (addr.startsWith("None ")) {
        addr = addr.substring(5).trim();
        parseAddress(addr, data);
      } else {
        parseAddress(StartType.START_OTHER, FLAG_ANCHOR_END, addr, data);
        String place = getStart();
        if (data.strAddress.length() == 0) {
          parseAddress(place, data);
        } else {
          parsePlaceApt(place, data);
        }
      }
    }
    
    else {
      parseAddress(addr, data);
    }
    if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|LOT|SUITE) +(.*)", Pattern.CASE_INSENSITIVE);
  
  private void parsePlaceApt(String field, Data data) {
    Matcher match = APT_PTN.matcher(field);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", field);
    } else {
      data.strPlace = append(data.strPlace, " - ", field);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = removeNone(field);
      super.parse(field, data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = removeNone(field);
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - ");
  
  private void parseInfo(String field, Data data) {
    for (String part : field.split("; ")) {
      part = part.trim();
      Matcher match = INFO_PFX_PTN.matcher(part);
      if (match.lookingAt()) part = part.substring(match.end()).trim();
      part = removeNone(part);
      data.strSupp = append(data.strSupp, "\n", part);
    }
  }
  
  private static String removeNone(String field) {
    if (field.equals("None")) return "";
    return field;
  }
}
