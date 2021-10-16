package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;



public class VAOrangeCountyParser extends SmartAddressParser {

  public VAOrangeCountyParser() {
    super("ORANGE COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "orange911@orangecountyva.gov";
  }

  private static final Pattern MASTER1 = Pattern.compile("LOC: (.*?) NATURE:\\s*");
  private static final Pattern ADDR_ID_CALL_PTN = Pattern.compile("(.*?) (\\d{4}-\\d{8}) (.*)");
  private static final Pattern APT_PTN1 = Pattern.compile("(\\d{1,4}[A-Za-z]?|[A-Za-z]$)\\b *(.*)");
  private static final Pattern APT_PTN2 = Pattern.compile("(.*)? (\\d{1,4}[A-Za-z]?|[A-Za-z])");
  private static final Pattern PHONE_PTN = Pattern.compile("(\\d{10})\\b *(.*)");
  private static final Pattern TRAIL_GPS_PTN = Pattern.compile("\\\\+ *(?:([-+]?\\d{2,3}\\.\\d{6,} +[-+]?\\d{2,3}\\.\\d{6,})|-361 -361)(?: +[- A-Z0-9]+)?$");

  private static final Pattern MASTER2 = Pattern.compile("(.*?) LOC: (.*?) ([A-Z]+)(\\d{4}-\\d{6}) BOX: (\\d+) (.*)");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("Orange911")) {
      setFieldList("ADDR APT PHONE PLACE ID CALL INFO GPS");
      Matcher match = MASTER1.matcher(body);
      if (!match.lookingAt()) return false;
      String addr = match.group(1).trim();
      String info = body.substring(match.end());

      match = ADDR_ID_CALL_PTN.matcher(addr);
      if (match.matches()) {
        addr = match.group(1).trim();
        data.strCallId = match.group(2);
        data.strCall = match.group(3).trim();
      } else {
        String call = CALL_LIST.getCode(addr, true);
        if (call != null) {
          data.strCall = call;
          addr = addr.substring(0, addr.length()-call.length()).trim();
        }
      }

      parseAddress(StartType.START_ADDR, addr, data);
      String left = getLeft();

      match = APT_PTN1.matcher(left);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        left = match.group(2);
      }

      match = PHONE_PTN.matcher(left);
      if (match.matches()) {
        data.strPhone = match.group(1);
        left = match.group(2);
      }

      if (data.strCall.length() == 0) {
        data.strCall = left;
      } else {
        match = APT_PTN2.matcher(left);
        if (match.matches()) {
          left = match.group(1).trim();
          data.strApt = append(data.strApt, "-", match.group(2));
        }
        data.strPlace = left;
      }

      match = TRAIL_GPS_PTN.matcher(info);
      if (match.find()) {
        String gps = match.group(1);
        if (gps != null) setGPSLoc(gps, data);
        info = info.substring(0,match.start()).trim();
      }

      int pt = info.indexOf("E911 Info - ");
      if (pt >= 0) info = info.substring(0,pt).trim();
      data.strSupp = info;

      return true;
    }

    Matcher match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR SRC ID BOX APT INFO");
      data.strCall = match.group(1).trim();
      parseAddress(match.group(2), data);
      data.strSource = match.group(3);
      data.strCallId = match.group(4);
      data.strBox = match.group(5);
      String extra = match.group(6);

      if (extra.startsWith("None ")) {
        extra = extra.substring(5).trim();
      } else if (extra.startsWith("APT ")) {
        extra = extra.substring(4).trim();
        int pt = extra.indexOf(' ');
        if (pt < 0) pt = extra.length();
        data.strApt = append(data.strApt, "-", extra.substring(0,pt).trim());
        extra = extra.substring(pt).trim();
      }

      extra = stripFieldStart(extra, "None ");

      for (String line : INFO_BRK_PTN.split(extra)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
      return true;
    }

    return false;
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final CodeSet CALL_LIST = new ReverseCodeSet(
      "AA- Auto Accident",
      "AA- HAZ ONLY",
      "AA- Unk/No Injuries MRP",
      "AA- W/ ENT",
      "Abdominal Pain",
      "Allergic Reaction",
      "ALOC/AMS",
      "Assist LE",
      "Back Pain",
      "Brush Fire",
      "Cardiac Arrest/CPR",
      "Cardiac or Respiratory Arrest",
      "CardiacEmergency/MI",
      "Child lockout vehicle",
      "Choking",
      "CO Alarm",
      "CP",
      "DB",
      "Diabetic Emergency",
      "Dumpster Fire",
      "Electrocution",
      "Fire Alarms",
      "Fire Alarm COM",
      "Fire Alarm NHSA",
      "Fire Alarm RES",
      "Fuel Spill Small",
      "Gas Leak Inside",
      "Gas Leak Outside",
      "Illness",
      "Injury",
      "Injury/Fall",
      "Lift Assist",
      "Lines Down",
      "Medical Alarm",
      "Minor Bleeding",
      "NEW CALL",
      "Odor Inside COM",
      "Odor Inside RES",
      "Odor Outside",
      "Outside Fire",
      "Outside Smoke",
      "Overdose/Poisoning",
      "Pedestrian Struck",
      "Public Service EMS",
      "Public Service Fire",
      "Road Hazard",
      "Seizure",
      "Severe Bleeding",
      "Smell of Smoke RES",
      "Smoke Investigation",
      "Standby EMS",
      "Standby FIRE",
      "Stroke",
      "Structure Fire COM",
      "Structure Fire NHSA",
      "Structure Fire RES",
      "Structure Fire SHED/BARN",
      "Suicide/Attempted",
      "Syncopal Episode",
      "Transfer",
      "Unconscious",
      "Unconscious/Fainting (near)",
      "UNK Medical Emergency",
      "Vehicle Fire"
  );
}
