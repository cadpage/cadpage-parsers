package net.anei.cadpage.parsers.UT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class UTDavisCountyCParser extends FieldProgramParser {
  
  public UTDavisCountyCParser() {
    super(CITIES, "DAVIS COUNTY", "UT",
        "ADDR X? INFO+");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "DAVIS CREEK",
        "JUPITER HILLS",
        "LAKE MESA",
        "PHEASANT RUN",
        "SHOTLEY BRIDGE",
        "SPYGLASS HILL"
    );
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.indexOf('\n') >= 0) return false;
    if (!parseFields(body.split(";"), data)) return false;
    if (isPositiveId()) return true;
    return CALL_LIST.getCode(data.strCall) != null;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern PHONE_PATTERN
    = Pattern.compile("(?:\\.(\\d{3}[\\- ]\\d{3}[\\- ]\\d{4})|(\\d{3}[\\- ]\\d{4}) +/)(.*)");
  private static final Pattern TIMESTAMPED_PATTERN
    = Pattern.compile("(.*)(\\d\\d:\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4}) -(.*)");
  private static final Pattern PROQA_PATTERN
    = Pattern.compile("(.*)\\(P.*");
  private class UTDavisInfoField extends InfoField {
    protected void parseInfo(String field, Data data) {
      Matcher m = PHONE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strPhone = getOptGroup(m.group(1))+getOptGroup(m.group(2));
        field = m.group(3).trim();
      }
      m = TIMESTAMPED_PATTERN.matcher(field);
      if (m.matches()) {
        data.strSupp = append(data.strSupp, "/", m.group(1).trim());
        data.strTime = m.group(2);
        data.strDate = m.group(3);
        String trailer = m.group(4).trim();
        m = PROQA_PATTERN.matcher(trailer);
        if (m.matches()) {
          data.strName = m.group(1).trim();
          field = "";
        }
        else
          field = trailer;
      }
      data.strSupp = append(data.strSupp, "/", field);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CALL ADDR CITY X PLACE PHONE TIME DATE NAME";
    }
  }
  
  private class MyAddressField extends UTDavisInfoField {
    @Override
    public void parse(String field, Data data) {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, field, data);
      parseInfo(getLeft(), data);
    }
  }
  
  private static final Pattern UTAH_ADDRESS_PATTERN
    = Pattern.compile("[EWNS] +\\d{3,4} +[EWNS]");
  private class MyInfoField extends UTDavisInfoField {
    @Override
    public void parse(String field, Data data) {
      if(data.strCity.equals("")) {
        Matcher m = CITY_PATTERN.matcher(field);
        if (m.matches()) {
          String p = m.group(1).trim();
          data.strCity = m.group(2);
          field = m.group(3).trim();
          
          if (!p.toUpperCase().startsWith("GROUP")) {
            if (isValidAddress(p))
              data.strCross = p;
            else {
              m = UTAH_ADDRESS_PATTERN.matcher(p);
              if (m.matches()) {
                data.strCross = p;
              } else {
                data.strPlace = p;
              }
            }
          }
        }
      }
      parseInfo(field.trim(), data);
    }
  }
  
  private static final String[] CITIES = {
      "BOUNTIFUL",
      "CENTERVILLE",
      "CLEARFIELD",
      "CLINTON",
      "FARMINGTON",
      "FRUIT HEIGHTS",
      "KAYSVILLE",
      "LAYTON",
      "NORTH SALT LAKE",
      "SOUTH WEBER",
      "SUNSET",
      "SYRACUSE",
      "WEST BOUNTIFUL",
      "WEST POINT",
      "WOODS CROSS",  
  };
  
  private static final Pattern CITY_PATTERN;
  static {
    String connect = "(.*)\\b(";
    StringBuilder sb = new StringBuilder();
    for (String city : CITIES) {
      sb.append(connect);
      sb.append(city);
      connect = "|";
    }
    sb.append(")\\b(.*)");
    CITY_PATTERN = Pattern.compile(sb.toString());
  }
  
  private static CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ACC PI ALPHA",
      "ACC PI BRAVO",
      "ALLERGIC REACT",
      "ASSAULT MEDICAL",
      "CHEST PAIN",
      "CO DETECT ALARM",
      "FALL",
      "FIRE ALARM COMM",
      "FIRE ALARM RESD",
      "FIRE ASSIST",
      "FIRE DUMPSTER",
      "FIRE GRASS",
      "FIRE STRUCTURE",
      "FIRE VEHICLE TS",
      "FIRE WASHDOWN",
      "GAS LEAK/SMELL",
      "FULL ARREST",
      "HEART PROBLEM",
      "HEMORRHAGE",
      "ILLEGAL BURN",
      "MEDICAL",
      "MEDICAL ALARM",
      "MEDICAL STANDBY",
      "OVERDOSE",
      "PAGER TEST",
      "POWERLINE PROB",
      "SEIZURE",
      "SICK PERSON",
      "SMOKE",
      "SUICIDE ATTEMPT",
      "TRAUMATIC INJ",
      "UNCONSCIOUS",
      "WELFARE CHECK"
  );
}
