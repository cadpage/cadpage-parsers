package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
/**
 * Craven County, NC
 * Obsolete as of 8/19/14 - replaced by NCCravenCountyBParser
 */
public class NCCravenCountyAParser extends FieldProgramParser {

  public NCCravenCountyAParser() {
    super(CITY_LIST, "CRAVEN COUNTY", "NC",
          "Location:ADDR! Call_Time:DATETIME! Narrative:INFO!");
  }

  @Override
  public String getFilter() {
    return "dispatch@cravencountync.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CC911")) return false;
    if (super.parseMsg(body, data)) return true;
    data.parseGeneralAlert(this, body);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern RR_TRACKS_PTN = Pattern.compile("(RAILROAD TRACKS) */? *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("<UNKNOWN>")) {
        data.strAddress = "<UNKNOWN>";
        data.strCall = field.substring(9).trim();
        return;
      }
      String call = CALL_SET.getCode(field);
      if (call != null) {
        data.strCall = call;
        field = field.substring(0,field.length()-call.length()).trim();
        Result res = parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field);
        if (res.getCity().length() > 0) {
          res.getData(data);
        } else {
          Matcher match = RR_TRACKS_PTN.matcher(field);
          if (match.matches()) {
            data.strAddress = append(match.group(1), " & ", match.group(2));
          } else {
            parseAddress(StartType.START_ADDR, FLAG_NO_CITY, field, data);
            data.strAddress = append(data.strAddress, " & ", stripFieldStart(getLeft(), "/"));
          }
        }
      } else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, field, data);
        data.strCall = getLeft();
        if (data.strCall.length() == 0) {
          data.strCall = data.strAddress;
          data.strAddress = "";
        }
      }
      if (data.strCity.equalsIgnoreCase("Jones")) data.strCity = "Jones County";
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY CALL";
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("\\s*(?:E911 Info .*)?(?:(3[45]\\.\\d{4,} +-7[67]\\.\\d{4,})|-361 +-361)$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(getOptGroup(match.group(1)), data);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }

  private static CodeSet CALL_SET = new ReverseCodeSet(
      "Alarm Business",
      "Assault",
      "Check on welfare",
      "Citizen Assist",
      "Concealed Handgun Permit",
      "Death",
      "Disturbance/Unknown Type",
      "Fire Alarm Business",
      "Fire Alarm Residence",
      "Fire Forest/Woods",
      "Fire Grass/Brush",
      "Fire Hazardous Cond. Gas Oil",
      "Fire Illegal Burning",
      "Fire Structure",
      "Fire Trash",
      "Fire Unknown Size or Type",
      "Fire Vehicle",
      "Gas Leak Confirmed",
      "Gas Leak Non-confirmed",
      "Medical",
      "Mental Subject",
      "Missing",
      "Mutual Aid Request",
      "New Call",
      "Person Complaint",
      "Special Operation",
      "Stranded Motorist",
      "Suicide/Attempted",
      "Susp Vehicle",
      "Traffic All Other",
      "TS",
      "Veh Crash-Injury",
      "Veh Crash-Property",
      "Water Rescue"
  );

  private static final String[] CITY_LIST = new String[]{
    "BRICES CREEK",
    "BRIDGETON",
    "COVE CITY",
    "DOVER",
    "ERNUL",
    "FAIRFIELD HARBOUR",
    "HAVELOCK",
    "JAMES CITY",
    "NEUSE FOREST",
    "NEW BERN",
    "RIVER BEND",
    "TRENT WOODS",
    "VANCEBORO",
    "EMUL",
    "HARLOWE",
    "CHERRY POINT",
    "CHERRY BRANCH",
    "ADAMS CREEK",
    "FORT BARNWELL",

    "JONES",   // Jones County

    // Lenoir County
    "GRIFTON"
  };

}
