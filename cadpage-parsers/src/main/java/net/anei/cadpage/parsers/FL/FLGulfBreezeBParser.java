package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class FLGulfBreezeBParser extends MsgParser {

  public FLGulfBreezeBParser() {
    super("GULF BREEZE", "FL");
    setFieldList("ADDR APT PLACE X CALL ID UNIT GPS INFO");
  }
  
  @Override
  public String getFilter() {
    return "FFIRE@gulfbreezefl.gov,noreply@gulfbreezepolice.com,Some.One@YourOrg.com";
  }

  private static final Pattern GPS_MARK_PTN = Pattern.compile(" *(?:\\||\\[[ \\(]+\\]) *");
  private static final Pattern STREET_NO_PTN = Pattern.compile("^(\\d+)  +");
  private static final Pattern ADDR_TERM_PTN = Pattern.compile("  +| +#([^ ]*) +");
  private static final Pattern UNIT_PTN = Pattern.compile(" +((?:LADDER|FIRE|EMS) +\\d+)$");
  private static final Pattern ID_PTN = Pattern.compile(" +([A-Z]{4}\\d{2}[A-Z0-9]{4,})$");
  private static final Pattern PLACE_PTN = Pattern.compile("[^\\(\\[]*\\(.*?\\)");
  private static final Pattern CROSS_PTN = Pattern.compile("\\bX2\\[(.*?)\\]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    
    // Eliminate the FLGulfBreezeA messages
    if (subject.equals("FIRE PAGE")) return false;
    
    // Strip out GPS coordinates
    Matcher match = GPS_MARK_PTN.matcher(body);
    if (match.find()) {
      String tail = body.substring(match.end());
      body = body.substring(0,match.start());
      
      if (tail.startsWith("Coordinates:")) {
        setGPSLoc(tail.substring(12), data);
      } else {
        data.strSupp = tail;
      }
    }
    
    // There are occasion values we have to pull from the end of what is left
    match = UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = match.group(1);
      body = body.substring(0,match.start());
    }
    
    match = ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(0,match.start());
    }
    
    body = STREET_NO_PTN.matcher(body).replaceFirst("$1 ");
    match = ADDR_TERM_PTN.matcher(body);
    if (!match.find()) return false;
    parseAddress(body.substring(0,match.start()), data);
    data.strApt = append(data.strApt, "-", getOptGroup(match.group(1)));
    String tail = body.substring(match.end());
    
    match = PLACE_PTN.matcher(tail);
    if (match.lookingAt()) {
      data.strPlace = match.group();
      tail = tail.substring(match.end()).trim();
    }
    
    match = CROSS_PTN.matcher(tail);
    if (match.find()) {
      data.strPlace = append(data.strPlace, " - ", tail.substring(0,match.start()).trim());
      data.strCross = match.group(1);
      tail = tail.substring(match.end()).trim();
    }
    
    String call = CALL_LIST.getCode(tail, true);
    if (call != null) {
      data.strCall = call;
      String place = tail.substring(0,tail.length()-call.length()).trim();
      data.strPlace = append(data.strPlace, " - ", place);
    } else {
      data.strCall = tail;
    }
    return true;
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(
      "BOX ALARM",
      "BOX ALARM FIRE ALARM",
      "FIRE ALARM",
      "FIRE INVESTIGATION",
      "GULF BREEZE PKWY M.V.A.",
      "Hazmat Incident",
      "MEDIAN M.V.A.",
      "MEDICAL ASSIST",
      "MEDICAL CALL",
      "MOTOR VEHICLE ACCIDENT BOX ALARM EXTRACATION",
      "PENSACOLA BEACH FOR AGENCY ASSIST TO FIRE/EMS BOX ALARM",
      "PENSACOLA BEACH FOR AGENCY ASSIST TO FIRE/EMS BOX ALARM FIRE ALARM",
      "PUBLIC ASSIST",
      "PUBLIC ASSIST LIFT ASSIST",
      "STRUCTURE FIRE BOX ALARM",
      "WATER RESCUE"
 );
}
