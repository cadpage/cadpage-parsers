package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class MOClintonCountyAParser extends MsgParser {

  public MOClintonCountyAParser() {
    this("CLINTON COUNTY", "MO");
  }

  protected MOClintonCountyAParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("CALL CODE ADDR APT CITY ST PLACE INFO");
  }

  @Override
  public String getFilter() {
    return "centralsquare@cameronmo.com";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern MASTER = Pattern.compile("(?:(\\d*[A-Z]+) )?None\\b *(.*)");
  private static final Pattern LEAD_CODE_PTN = Pattern.compile("(\\d*[A-Z]+) +(.*)");

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    String[] parts = INFO_BRK_PTN.split(body);
    body = parts[0];

    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      data.strCode = getOptGroup(match.group(1));
      body =  match.group(2);
    } else {
      String call = CALL_LIST.getCode(body, true);
      if (call == null) {
        match = LEAD_CODE_PTN.matcher(body);
        if (!match.matches()) return false;
        data.strCode = match.group(1);
        body =  match.group(2);
        call = CALL_LIST.getCode(body, true);
        if (call == null) return false;
      }
      if (!data.strCall.contains(call)) {
        data.strCall = append(data.strCall, " - ", call);
      }
      body = body.substring(call.length()).trim();
    }

    if (!body.isEmpty()) {
      String addr;
      int brk = findDuplicate(body);
      if (brk >= 0) {
        addr = body.substring(0, brk).trim();
        data.strPlace = body.substring(brk+addr.length()).trim();
      } else return false;

      Parser p = new Parser(addr);
      String city = p.getLastOptional(',');
      match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        city = p.getLastOptional(',');
      }
      if (!city.startsWith("-")) {
        addr = p.get();
        data.strCity = city;
      }
      parseAddress(addr, data);
    }

    for (int j = 1; j < parts.length; j++) {
      data.strSupp = append(data.strSupp, "\n", parts[j]);
    }
    return true;
  }

  private int findDuplicate(String field) {
    int k = (field.length()+1)/2;
    for (int j = k; j > 0; j--) {
      if (field.charAt(j) != ' ') k = j;
      if (field.charAt(j) == ' ' && field.charAt(j-1) != ' ') {
        if (field.substring(k).startsWith(field.substring(0, j))) return k;
      }
    }
    return -1;
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  @Override
  public boolean checkCall(String call) {
    while (true) {
      String tmp = CALL_LIST.getCode(call);
      if (tmp != null && tmp.equals(call)) return true;
      int pt = call.indexOf(" - ");
      if (pt < 0) return false;
      call = call.substring(pt+3);
    }
  }

  private static CodeSet CALL_LIST = new CodeSet(
      "911 OPEN LINE",
      "96 HOUR HOLD SERVICE",
      "ABANDONED VEHICLE",
      "ALARM - PD",
      "AMBULANCE MUTUAL AID",
      "AMBULANCE OTHER DISTRICT INTERFACILITY TRANSFER",
      "AMBULANCE OTHER DISTRICT SCENE RESPONSE",
      "AMBULANCE SCENE RESPONSE",
      "AMBULANCE SCENE RESPONSE; MVA NON INJURY",
      "ANIMAL CITY",
      "ANIMAL COUNTY",
      "ASSAULT",
      "ASSIST EMS",
      "ASSIST OUTSIDE AGENCY",
      "BROADCAST",
      "CARELESS AND IMPRUDENT DRIVER",
      "CCDC TRANSPORT",
      "CHECK THE WELFARE",
      "CITIZEN ASSIST",
      "CIVIL",
      "CONTROLLED BURN",
      "DISTURBANCE",
      "DOMESTIC VIOLENCE",
      "FIRE ADMINSTRATIVE",
      "FIRE ALARM",
      "FIRE - ASSIST EMS",
      "FIRE - CITIZEN ASSIST",
      "FIRE - GAS LEAK",
      "FIRE - MUTUAL AID",
      "FIRE - NATURAL COVER / GRASS",
      "FIRE - SMOKE INVESTIGATION",
      "FIRE - STRUCTURE",
      "FIRE TONES TEST",
      "FIRE VEHICLE",
      "FIREWORKS VIOLATION",
      "FOLLOW UP",
      "FRAUD / FORGERY",
      "FUEL REPORT",
      "HARASSMENT",
      "INFO",
      "INTERFACILITY TRANSFER",
      "JAKES LAW REQUEST",
      "MISSING PERSON",
      "MVA INJURY",
      "MVA NON INJURY",
      "MVA UNKNOWN",
      "NOISE COMPLAINT",
      "PAPER SERVICE",
      "PARKING COMPLAINT",
      "PROPERTY DAMAGE",
      "PUBLIC WORKS / UTILITIES",
      "SPECIAL ASSIGNMENT",
      "STEALING",
      "STORM SIREN TEST",
      "SUICIDAL SUBJECT",
      "SUSPICIOUS ACTIVITY - PERSON/VEHICLE",
      "TEST EQUIPMENT",
      "TRAFFIC STOP",
      "TRESPASSING",
      "VEHICLE CHECK",
      "WARRANT SERVICE"
      );
}
