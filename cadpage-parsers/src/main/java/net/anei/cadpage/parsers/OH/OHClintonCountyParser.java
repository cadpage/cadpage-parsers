package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA10Parser;

/**
 * Clinton County, OH
 */
public class OHClintonCountyParser extends DispatchA10Parser {

  public OHClintonCountyParser() {
    super(CITY_LIST, "CLINTON COUNTY", "OH",
          "( UNIT ADDR CITY ST_ZIP ID INFO! INFO+? X2 END " +
          "| CALL ADDR/S PHONE? INFO! X+ )");
  }
  
  @Override
  public String getFilter() {
    return "noreply@clintonsheriff.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }
    
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("\\d{1,2}(?: ?[A-Z]+)?|IO");
    if (name.equals("PHONE")) return new PhoneField("\\d{7}");
    if (name.equals("ST_ZIP")) return new MyStateZipField();
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }
  
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyStateZipField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = STATE_ZIP_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strState = match.group(1);
      if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(2));
    }

    @Override
    public String getFieldNames() {
      return "ST CITY";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    // City
    "WILMINGTON",

    // Villages
    "BLANCHESTER",
    "CLARKSVILLE",
    "LYNCHBURG",
    "MARTINSVILLE",
    "MIDLAND",
    "NEW VIENNA",
    "PORT WILLIAM",
    "SABINA",

    // Townships
    "ADAMS",
    "CHESTER",
    "CLARK",
    "GREEN",
    "JEFFERSON",
    "LIBERTY",
    "MARION",
    "RICHLAND",
    "UNION",
    "VERNON",
    "WASHINGTON",
    "WAYNE",
    "WILSON",

    // Unicorporated villages
    "BLOOMINGTON",
    "BURTONVILLE",
    "CUBA",
    "FARMERS STATION",
    "GURNEYVILLE",
    "JONESBORO",
    "LEES CREEK",
    "LUMBERTON",
    "MCKAYS STATION",
    "MELVIN",
    "MEMPHIS",
    "MORRISVILLE",
    "NEW ANTIOCH",
    "NORTH KINGMAN",
    "OAKLAND",
    "OGDEN",
    "POWDER LICK",
    "REESVILLE",
    "SLIGO",
    "SOUTH KINGMAN",
    "WALLOPSBURG",
    "WESTBORO",
    
    // In Highland County
    "LEESBURG"

  };
}
