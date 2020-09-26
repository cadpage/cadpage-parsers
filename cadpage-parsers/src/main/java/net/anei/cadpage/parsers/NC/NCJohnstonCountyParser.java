package net.anei.cadpage.parsers.NC;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCJohnstonCountyParser extends DispatchOSSIParser {
  
  private static final Pattern ENROUTE_PTN = Pattern.compile("CAD:([A-Z0-9]+),Enroute,.*");
  
  private String lastCrossPlaceFld;
  
  public NCJohnstonCountyParser() {
    super(CITY_CODES, "JOHNSTON COUNTY", "NC",
           "( CALL ADDR/Z CITY! APT? X_PLACE_INFO+ | " +
             "CALL CITY ADDR! APT? X_PLACE_INFO+ | " + 
             "CH? SRC+? CODE? CALL ADDR ( CITY! INFO+ | APT? X_PLACE_INFO+? DATETIME! UNIT CITY% ) )");
  }

  @Override
  public String getFilter() {
    return "CAD@johnstonnc.com,duane1409@gmail.com,93001";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // Enroute messages parse as general alerts with a unit field
    Matcher match = ENROUTE_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "GENERAL ALERT";
      data.strUnit = match.group(1);
      data.strPlace = body;
      return true;
    }
    
    lastCrossPlaceFld = "";
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT PLACE";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("OPS.*|.*FR|VPR.*|2ND", true);
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("CODE")) return new CodeField("\\d{1,3}[A-Z]\\d\\d[A-Za-z]?", true);
    if (name.equals("APT")) return new AptField("APT.*|SUITE.*|LOT.*");
    if (name.equals("X_PLACE_INFO")) return new MyCrossPlaceInfoField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MySourceField extends SourceField {
    public MySourceField() {
      setPattern("[A-Z][A-Z0-9]{1,4}", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strSource = append(data.strSource, ",", field);
    }
  }
  
  private static final Pattern NS_CROSS_PTN = Pattern.compile("(.*?) *\\(S\\) *(.*?) *\\(N\\)|(?:[NSEW]B +)?DIST:.*");
  private class MyCrossPlaceInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      // see if it has the expected (N)..(S) pattern
      // Or looks like a decimal number
      // If it is, put it in the place or cross street field
      // depending on whether or not it looks like an address
      Matcher match = NS_CROSS_PTN.matcher(field);
      if (match.matches()) {
        String tmp = match.group(2);
        if (tmp != null) {
          String prefix = match.group(1);
          if (prefix.length() > 0) {
            if (NUMERIC.matcher(prefix).matches()) {
              data.strApt = append(data.strApt, "-", match.group(1));
            } else {
              data.strPlace = append(data.strPlace, " - ", prefix);
            }
          }
          field = tmp;
        }
        data.strPlace = append(data.strPlace, " - ", field);
        return;
      }
      
      // If duplicate of last field, skip it
      if (field.equals(lastCrossPlaceFld)) return;
      lastCrossPlaceFld = field;
      
      // See if it looks like a cross street or a place name
      if (field.contains("70 BUS HWY") || isValidAddress(field)) {
        data.strCross = append(data.strCross, " / ", field); 
      } else {
        data.strSupp = append(data.strSupp, " / ", cleanWirelessCarrier(field));
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE X INFO";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d/[\\d:/ ]*");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // Looser pattern match standard than the default date/time field
      // if we only have the first 3 characters of what looks like a date/time
      // field, that is good enough
      if (!DATE_TIME_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return US70BUSHWY_PTN.matcher(addr).replaceAll("70 BUS");
  }
  Pattern US70BUSHWY_PTN = Pattern.compile("\\b70 BUS HWY\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANGI", "ANGIER",
      "ARCL", "ARCHER LODGE",
      "BENS", "BENSON",
      "CLAY", "CLAYTON",
      "DUNN", "DUNN",
      "ERWN", "ERWIN",
      "FOUR", "FOUR OAKS",
      "GARN", "GARNER",
      "KENL", "KENLY",
      "MICR", "MICRO",
      "MIDD", "MIDDLESEX",
      "NEWT", "NEWTON GROVE",
      "PINE", "PINE LEVEL",
      "PRIN", "PRINCETON",
      "RALE", "RALEIGH",
      "SELM", "SELMA",
      "SMIT", "SMITHFIELD",
      "WAKE", "WAKE COUNTY",
      "WISM", "WILSON'S MILLS",
      "WEND", "WENDELL",
      "WILL", "WILLOW SPRING",
      "WILS", "WILSON",
      "WISM", "WILSON'S MILLS",
      "ZEBU", "ZEBULON"
  });
}