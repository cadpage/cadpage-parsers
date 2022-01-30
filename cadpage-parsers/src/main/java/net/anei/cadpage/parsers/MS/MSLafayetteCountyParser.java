package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MSLafayetteCountyParser extends DispatchGlobalDispatchParser {

  private static final Pattern DATE_TIME_DISPATCH_PTN = Pattern.compile(" *\\[(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d) [A-Z0-9]+\\] *", Pattern.CASE_INSENSITIVE);

  public MSLafayetteCountyParser() {
    super("LAFAYETTE COUNTY", "MS");
    setupCallList(CALL_LIST);
    removeWords("TERRACE");
    setupMultiWordStreets(
        "ALL AMERICAN",
        "CHARLESTON COURT",
        "COUNTRY CLUB",
        "ED PERRY",
        "FOREST GROVE",
        "GALTNEY LOTT",
        "HOME DEPOT",
        "LOGAN LEE",
        "MARTIN LUTHER KING JR",
        "NORTH POINTE",
        "NORTHPOINTE LAKE",
        "OFFICE PARK",
        "REGIONAL CENTER",
        "ROCK SPRING",
        "SHADY OAKS",
        "SOUTH COMMERCE",
        "ST ANDREWS",
        "STUDENT UNION",
        "TOWN CENTER",
        "VAN BUREN",
        "WHISPERING VALLEY"
    );
  }

  @Override
  public String getFilter() {
    return "E911OMG@HOTMAIL.COM";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if(!subject.equals("911Page")) return false;

    if (!super.parseMsg(subject, body, data)) return false;

    Matcher match = DATE_TIME_DISPATCH_PTN.matcher(data.strSupp);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strSupp = append(data.strSupp.substring(0,match.start()), " ", data.strSupp.substring(match.end()));
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_IGNORE_AT;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ALARM MEDICAL",
      "AMBULANCE CALL CITY",
      "AMBULANCE CALL COUNTY",
      "AMBULANCE CALL UNIVERSITY",
      "ASSISTANCE CALL",
      "CONTROL BURN",
      "DETAIL",
      "DOMESTIC IN PROGRESS",
      "DOMESTIC NOT IN PROGRESS",
      "FIRE",
      "FIRE ALARM",
      "FIRE DUMPSTER",
      "FIRE ELETC/GAS",
      "FIRE GRASS",
      "FIRE STRUCTURE RES/BUSINESS",
      "FIRE VEHICLE",
      "FUNERAL DETAIL",
      "GAS LEAK",
      "INFORMATION",
      "SEARCH & RESCUE",
      "SMELL OF SMOKE",
      "STRUCTURE FIRE",
      "SUICIDE ATTEMPT",
      "SUICIDE THREATS",
      "SUSPICIOUS ACTIVITY",
      "TA EMS",
      "TA TRAFFIC ACCIDENT",
      "WELFARE CONCERN"

 );
}
