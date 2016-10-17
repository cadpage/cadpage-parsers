package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class CTFairfieldCountyAParser extends DispatchA3Parser {

  public CTFairfieldCountyAParser() {
    super("CAD:", CITY_LIST, "FAIRFIELD COUNTY", "CT", 
          "MASH Line16:INFO Line17:INFO Line18:INFO", FA3_NBH_PLACE_OFF);
    setBreakChar('=');
  }

  @Override 
  protected boolean parseMsg(String body, Data data) {
    return super.parseMsg(body, data, false);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MyMashField();
    return super.getField(name);
  }

  private static final Pattern MASH = Pattern.compile("(.*?) *\\b((?:Landmark Comment:|Geo Comment:|NBH:).*?)? *(EMS[A-Z]{3,}|FD[A-Z]{3,}|MISC|MVAI|OTAGE) +(.*?) *(?:(\\d? ?\\d{3}-\\d{3}-\\d{4})|\\d{3}- - )? *((?:[A-Z0-9]{2,6},?)+ ?\\d?)?");

  private class MyMashField extends BaseInfo1Field {
    @Override
    public void parse(String field, Data data) {
      
      //after initial pattern matching, really all this code does is assign group contents to fields
      Matcher mat = MASH.matcher(field);
      if (!mat.matches()) abort();
      
      //save some groups as local strings for use later...
      String group1 = mat.group(1);
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_CROSS_FOLLOWS, group1.replace("//", "&").trim(), data);
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_NO_CITY, getLeft(), data);
      data.strMap = getLeft();
      
      // superclass handles landmark and geo comments, then NBH goes in place
      String group2 = mat.group(2);
      if (group2 != null) super.parse(group2.trim(), data);
      
      //find code+call in table and separate it from trailing caller name
      data.strCode = mat.group(3);
      String callName = mat.group(4);
      data.strCall = CALL_SET.getCode(callName);
      if (data.strCall != null) {
        data.strName = callName.substring(data.strCall.length()).trim();
      } else {
        data.strCall = callName;
      }
      
      //phone + unit
      data.strPhone = getOptGroup(mat.group(5));
      data.strUnit = getOptGroup(mat.group(6)).trim();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X MAP PLACE CODE CALL NAME PHONE UNIT";
    }
  }

  public static String[] CITY_LIST = new String[] { 
    "DARIEN", 
    "NORWALK", 
    "STAMFORD" };
  
  private static final CodeSet CALL_SET = new CodeSet(new String[] { 
    "UNKNOWN CARDIAC/CHEST PAIN", 
    "FALL", 
    "EMS LIFELINE", 
    "EMS MUTUAL AID", 
    "MVA", 
    "PSYCHIATRIC", 
    "EMS SEIZURE", 
    "SHORTNESS OF BREATH", 
    "EMS TRAUMA", 
    "UNCONSCIOUS/UNRESPONSIVE", 
    "EMS UNKOWN MEDICAL EMERGENCY", 
    "FIRE ALARM ACTIVATION FD RESPONSE", 
    "CO ALARM FD RESPONSE", 
    "FD RESPONSE GAS LEAK PROPANE OR NATURAL", 
    "MVA FD RESPONSE", 
    "MISC FD RESPONSE", 
    "SMOKE", 
    "MISCELLANEOUS PUBLIC", 
    "ACCIDENT WITH INJURY",
    "OTHER AGENCY"});
}