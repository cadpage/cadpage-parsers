package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class OHMahoningCountyAParser extends DispatchEmergitechParser {
  
  public OHMahoningCountyAParser() {
    super(true, CITY_LIST, "MAHONING COUNTY", "OH", TrailAddrType.INFO);
    setupMultiWordStreets("COLUMBIANA CANFIELD");
  }

  @Override
  public String getFilter() {
    return "BPD911@twp.boardman.oh.us,canfieldpd@ci.canfield.oh.us,jonracco25@yahoo.com,@sebringohio.net";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern MARK_ID_PTN = Pattern.compile("(\\d{3}):");
  private static final Pattern TRUNC_GPS_PTN = Pattern.compile("[-+]?[\\d\\.]+ CF= *\\d+% UF= *\\d+ M Z= *\\d*M\\b *");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = MARK_ID_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end()).trim();
      if (subject.equals("NATURE") || subject.equals("LOCATION")) {
        subject = '[' + match.group(1) + "]- " + subject;
      }
    }

    body = body.replace('\n', ' ');
    if (!super.parseMsg(subject, body,  data)) {
      if (!isPositiveId() || subject.length() > 0 || 
          !body.contains(" BETWEEN ")) return false;
      data.initialize(this);
      if (!super.parseMsg("LOCATION: " + body, data)) return false;
    }
    
    String call = CALL_CODES.getProperty(data.strCall);
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    
    match = TRUNC_GPS_PTN.matcher(data.strSupp);
    if (match.lookingAt()) data.strSupp = data.strSupp.substring(match.end());
    return true;
  }
  
  @Override
  public String getProgram() {
    return "CODE " + super.getProgram() + " GPS";
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "ALLIANCE",
    "CAMPBELL",
    "CANFIELD",
    "COLUMBIANA",
    "SALEM",
    "STRUTHERS",
    "YOUNGSTOWN",
    "YOUNGSTOW N",

    // Villages
    "BELOIT",
    "CRAIG BEACH",
    "LOWELLVILLE",
    "NEW MIDDLETOWN",
    "POLAND",
    "SEBRING",
    "WASHINGTONVILLE",

    // Townships
    "AUSTINTOWN TWP",
    "BEAVER TWP",
    "BERLIN TWP",
    "BOARDMAN TWP",
    "CANFIELD TWP",
    "COITSVILLE TWP",
    "COITSVILL E TWP",
    "ELLSWORTH TWP",
    "GOSHEN TWP",
    "GREEN TWP",
    "JACKSON TWP",
    "MILTON TWP",
    "POLAND TWP",
    "SMITH TWP",
    "SPRINGFIELD TWP",

    // Census-designated places
    "AUSTINTOWN",
    "BOARDMAN",
    "MAPLE RIDGE",
    "MINERAL RIDGE",
  
    //Other communities
    "DAMASCUS",
    "ELLSWORTH",
    "GREENFORD",
    "LAKE MILTON",
    "NEW SPRINGFIELD",
    "NORTH BENTON",
    "N BENTON",
    "NORTH JACKSON",
    "N JACKSON",
    "NORTH LIMA",
    "PETERSBURG",
    
    // Portage County
    "DEERFIELD",
    "DEERFIELD TWP"

  };
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "6A",    "Accident With Injury",
      "F29",   "Medical Call W/O AFD Response",
      "F30",   "Transfer To Lanes - Log Reason",
      "F31",   "Medical Call With AFD Response",
      "F42",   "Fire/Burning Complaint",
      "F86",   "Odor Complaint",
      "F87",   "Fire Department Call",
      "F88",   "Township Fuel Spills",
      "F89",   "Fire Department Freeway Call",
      "F90",   "Fire Drill",
      "F91",   "Structure Fire",
      "F92",   "Vehicle Fire",
      "F93",   "Grass/Bush Fire",
      "F94",   "Jaws Of Life Call",
      "F95",   "Dumpster Fire Near Building",
      "F96",   "Dumpster Fire Not Near Building",
      "F97",   "Fire Alarm",
      "F97A",  "Fire Alarm/Trouble Position",
      "F98",   "Fire Department Mutual Aid",
      "F99",   "Chemical Spill",
      "F101",  "Carbon Monoxide Check"
  });
}
