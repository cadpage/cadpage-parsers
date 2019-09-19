package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VAWaynesboroBParser extends DispatchOSSIParser {
  
  public VAWaynesboroBParser() {
    super(CITY_CODES, "WAYNESBORO", "VA",
          "( CANCEL ADDR CITY? WAYNDIST:SKIP? " +  
          "| FYI CALL ADDR! X_PLACE+? CITY " + 
          ") INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ci.waynesboro.va.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject VAWaynesboroA -> VAAugustaCounty calls
    if (subject.length() == 0) return false;
    
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?\\d[A-Z]?)-(?!BLK)(?:AP|RM)?(\\S+) +(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        apt = match.group(2);
        field = append(match.group(1), " ", match.group(3));
      }
      super.parse(field, data);
      data.strApt = append(apt, "-", data.strApt);
    }
    
  }
  
  private class MyCrossPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (checkAddress(field) >= STATUS_STREET_NAME) {
        data.strCross = append(data.strCross, "/", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AG",   "AUGUSTA",
      "AGCO", "AUGUSTA",
      "NELS", "NELSON",
      "WAYN", "WAYNESBORO",

  });

}
