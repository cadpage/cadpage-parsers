package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHKnoxCountyAParser extends DispatchEmergitechParser {
  
  public OHKnoxCountyAParser() {
    super(CITY_LIST, "KNOX COUNTY", "OH", TrailAddrType.PLACE_INFO);
    addSpecialWords("HYATT");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "Dispatch@smtp-server.Columbus.rr.com,Dispatch@smtp-relay.gmail.com";
  }
  
  private static final Pattern END_TWP_RD_PTN = Pattern.compile(".*\\b(?:TWP|TWNSHI?P) RD|\\d+ +RD");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // We have to do a lot of massaging to get the message back to standard form
    // including recovering a square bracket unit that might have been interpreted
    // as the message subject
    if (body.startsWith("Dispatch:")) {
      body = body.substring(9).trim();
      body = stripFieldStart(body, "MSG:");
      body = stripFieldStart(body, "-");
    }
    else if (subject.length() > 0) body = '[' + subject + ']' + body;
    
    // M/A for mutual aid tends to get into the address, which messes things up
    body = body.replace(" M/A ", " MA ");

    if (!super.parseMsg(body, data)) return false;
    
    // Township road number get misinterpreted as apartments and have to be fixed
    if (data.strApt.length() > 0 && END_TWP_RD_PTN.matcher(data.strAddress).matches()) {
      data.strAddress = data.strAddress + ' ' + data.strApt;
      data.strApt = "";
    }
    
    // If Mutual aid got into address, move it to call
    if (data.strAddress.endsWith(" MA")) {
      data.strAddress = data.strAddress.substring(0, data.strAddress.length()-3).trim();
      data.strCall = "MA - " + data.strCall;
    }
    
    if (data.strSupp.startsWith("ACCESS ")) {
      data.strPlace = append(data.strPlace, " - ", data.strSupp);
      data.strSupp = "";
    }
    
    // If we did not find a proper city, see if we can get one from the start of the info section
    if (data.strCity.length() == 0) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, data.strSupp, data);
    }
    
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    
    // Don't ask me why, but this works
    if (data.strCity.equals("MONROE TWP")) data.strCity = "MT VERNON";
    
    return true;
  }
  
  @Override
  protected Result parseAddress(StartType sType, int flags, String address) {
    if (sType == StartType.START_PLACE) sType = StartType.START_ADDR;
    return super.parseAddress(sType, flags, address);
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "12619 FRED-AMITY RD",                  "+40.487865,-82.514711",
      "12731 FRED-AMITY RD",                  "+40.489436,-82.513234",
      "12810 FRED-AMITY RD",                  "+40.488212,-82.512174",
      "12842 FRED-AMITY RD",                  "+40.488442,-82.511037",
      "18069 FRED-AMITY RD",                  "+40.494719,-82.420060",
      "20624 FRED-AMITY RD",                  "+40.471702,-82.379257",
      "8540 GREEN VALLEY RD",                 "+40.423647,-82.590949",
      "9420 GREEN VALLEY RD",                 "+40.425096,-82.574815",
      "13141 HYATT RD",                       "+40.459844,-82.508014",
      "14991 OLD MANSFIELD RD",               "+40.452370,-82.487466",
      "8938 OVERLY RD",                       "+40.499124,-82.570308",
      "11950 SOUTHBAY DR",                    "+40.491517,-82.525324",
      "11580 YANKEE ST",                      "+40.532342,-82.528948"

  });
  
  private static final String[] CITY_LIST = new String[]{

    "MOUNT VERNON",
    "MT VERNON",

    "BRINKHAVEN",
    "CENTERBURG",
    "DANVILLE",
    "FREDERICKTOWN",
    "GAMBIER",
    "GANN",
    "MARTINSBURG",
    "UTICA",

    "BERLIN TWP",
    "BROWN TWP",
    "BUTLER TWP",
    "CLAY TWP",
    "CLINTON TWP",
    "COLLEGE TWP",
    "HARRISON TWP",
    "HILLIAR TWP",
    "HOWARD TWP",
    "JACKSON TWP",
    "JEFFERSON TWP",
    "LIBERTY TWP",
    "MIDDLEBURY TWP",
    "MILFORD TWP",
    "MILLER TWP",
    "MONROE TWP",
    "MORGAN TWP",
    "MORRIS TWP",
    "PIKE TWP",
    "PLEASANT TWP",
    "UNION TWP",
    "WAYNE TWP",

    "BANGS",
    "BLADENSBURG",
    "HOWARD",
    "MOUNT LIBERTY",
    
    // Morrow County
    "CHESTERVILLE",
    "SPARTA",
    
    // Richland County
    "PERRY TWP",
    
    // Coshocton County
    "BEDFORD TWP",
    
    // Licking County
    "HARTFORD",
    
    // Counties
    "ASHLAND COUNTY",
    "ASHLAND CO",
    "COSHOCTON COUNTY",
    "COSHOCTON CO",
    "DELAWARE COUNTY",
    "DELAWARE CO",
    "HOLEMS COUNTY",
    "HOLEMS CO",
    "LICKING COUNTY",
    "LICKING CO",
    "MORROW COUNTY",
    "MORROW CO",
    "RICHLAND COUNTY",
    "RICHLAND CO"
  };
}
