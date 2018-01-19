package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.ZipStateTable;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXTravisCountyBParser extends FieldProgramParser {
  
  public TXTravisCountyBParser() {
    super("TRAVIS COUNTY", "TX", 
          "CALL:ID! PLACE:PLACE! ADDRESS:ADDRCITY! ADDRESS_LINE_2:INFO! ID:SKIP! PRIORITY:CALL! DATE:DATETIME! INFO/N+ UNIT:UNIT! INFO:INFO/N! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@medapoint.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Trip Notification ")) return false;
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      String nextFld = getRelativeField(+1);
      if (nextFld.startsWith("ADDRESS:")) {
        nextFld = nextFld.substring(8).trim();
        int pt = nextFld.indexOf(',');
        if (pt >= 0) nextFld = nextFld.substring(0,pt).trim();
        if (field.equals(nextFld)) return;
      }
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*?)[, ]+(\\d{5})");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        String place = field.substring(0, pt).trim();
        field = field.substring(pt+3).trim();
        if (!place.equalsIgnoreCase(data.strPlace)) data.strPlace = append(data.strPlace, " - ", place);
      }
      
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        zip = match.group(2);
      } else {
        field = stripFieldEnd(field, ",");
      }
      super.parse(field, data);
      if (zip != null && data.strCity.length() == 0) data.strCity = zip;
      
      // They are dispatching calls to locations in 4 states, and the only way we can identify
      // which state is this is by looking up the zip code?
      data.defState = "";
      if (zip != null) {
        String state = ZipStateTable.lookup(zip);
        if (state != null) data.strState = state;
      }
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d) - (\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }
}
