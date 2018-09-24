package net.anei.cadpage.parsers.AR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;


public class ARGarlandCountyAParser extends DispatchProQAParser {
  
  private String backupCall = null;
  
  public ARGarlandCountyAParser() {
    super(CITY_LIST, "GARLAND COUNTY", "AR",
           "PRI ADDR! APT? ( PLACE CITY/Z ZIP | CITY/Z ZIP | ( PLACE CITY | CITY | ) CALL+? ) INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@lifenetems.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    backupCall = "";
    if (!super.parseMsg(body, data)) return false;
    if (data.strPlace.length() > 0 && isValidAddress(data.strPlace)) {
      if (checkAddress(data.strAddress) == STATUS_STREET_NAME) {
        data.strAddress = append(data.strAddress, " & ", data.strPlace);
      } else {
        data.strCross = append(data.strCross, " / ", data.strPlace);
      }
      data.strPlace = "";
    }
    if (data.strCall.length() == 0) {
      if (data.strPlace.length() > 0) {
        data.strCall = data.strPlace;
        data.strPlace = "";
      } else {
        data.strCall = backupCall;
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ZIP")) return new SkipField("[0-9]{5}", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern PRIORITY_PTN = Pattern.compile("(\\d)-(.*)");
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRIORITY_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPriority = match.group(1);
      
      // Call field is overridden by a later field in newer calls.  But
      // we copy it here just in case this is an older call
      backupCall = match.group(2).trim();
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM) +(.*)|LOT +.*|.{1,4}", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (!match.matches()) return false;
      String apt = match.group(1);
      if (apt == null) apt = field;
      data.strApt = apt;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("You are responding")) return false;
      if (field.equals("<PROQA_SCRIPT>")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, "/", field);
    }
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "215 HWY 290",   "+34.412122,-93.089761",
      "1144 HWY 290",  "+34.402439,-93.065960"
  });
  
  private static final String[] CITY_LIST = new String[]{
    "MOUNTAIN PINE",
    "FOUNTAIN LAKE",
    "JESSEVILLE",  // Misspelled
    "JESSIEVILLE",
    "LONSDALE",
    "HOT SPRINGS",
    "HOT SPRINGS VILLAGE",
    "LAKE HAMILTON",
    "PEARCY",
    "PINEY",
    "ROCKWELL",
    "ROYAL",
    "HALE TWP",
    "HOT SPRINGS TWP",
    "LAKE HAMILTON TWP",
    "WHITTINGTON TWP",
    
    // Hot springs county
    "BONNERDALE",
    "MALVERN",
    
    // Montgomery County
    "STORY",
    
    // Saline County
    "BENTON"
  };
}
