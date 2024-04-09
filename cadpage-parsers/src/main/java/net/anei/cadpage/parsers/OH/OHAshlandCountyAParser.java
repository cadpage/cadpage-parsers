package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHAshlandCountyAParser extends DispatchEmergitechParser {

  public OHAshlandCountyAParser() {
    super(true, CITY_LIST, "ASHLAND COUNTY", "OH", EMG_FLG_NO_PLACE, TrailAddrType.NONE);
  }

  @Override
  public String getFilter() {
    return "911@ashlandcountysheriff.org,txt.rprt@ashlandcountysheriff.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("\\d{4}");
  private static final Pattern SRC_ID_PTN = Pattern.compile("Run Number (\\S+) (FD\\d+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Reject any OHAshlandCountyB alerts
    if (subject.startsWith("From: ")) return false;

    body = stripFieldStart(body, "911:");
    if (SUBJECT_PTN.matcher(subject).matches()) {
      body = "[" + subject + "]" + body;
    }
    body = stripFieldStart(body, "- ");
    body = body.replace("TOWNSHIP RD", "TWP RD");
    if (!super.parseMsg(body, data)) return false;
    Matcher match = SRC_ID_PTN.matcher(data.strCallId);
    if (match.matches()) {
      data.strSource = match.group(1);
      data.strCallId = match.group(2);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final String[] CITY_LIST = new String[]{

      //City

      "ASHLAND",

      //Villages

      "BAILEY LAKES",
      "HAYESVILLE",
      "JEROMESVILLE",
      "LOUDONVILLE",
      "MIFFLIN",
      "PERRYSVILLE",
      "POLK",
      "SAVANNAH",

      //Townships

      "CLEAR CREEK",
      "GREEN",
      "HANOVER",
      "JACKSON",
      "LAKE",
      "MIFFLIN",
      "MILTON",
      "MOHICAN",
      "MONTGOMERY",
      "ORANGE",
      "PERRY",
      "RUGGLES",
      "SULLIVAN",
      "TROY",
      "VERMILLION",

      //Unincorporated communities

      "ALBION",
      "ENGLAND",
      "FIVE POINTS",
      "HEREFORK",
      "LAKE FORK",
      "MCKAY",
      "MCZENA",
      "MOHICANVILLE",
      "NANKIN",
      "NOVA",
      "PARADISE HILL",
      "REDHAW",
      "ROWSBURG",
      "RUGGLES",
      "SPRENG",
      "SULLIVAN",
      "WIDOWVILLE",

      // Holmes County
      "KNOX TWP",
      "RIPLEY TWP",
      "WASHINGTON TWP",
      "NASHVILLE",

      // Huron County
      "FITCHVILLE TWP",
      "GREENWICH TWP",
      "NEW LONDON TWP",
      "GREENWICH",
      "NEW LONDONG",

      // Knox County
      "BROWN TWP",
      "HOWARD TWP",
      "JEFFERSON TWP",
      "MONROE TWP",
      "PIKE TWP",
      "UNION TWP",
      "DANVILLE",
      "GANN",

      // Lorain County
      "BRIGHTON TWP",
      "HUNTINGTON TWP",
      "PENFIELD TWP",
      "ROCHESTER TWP",
      "WELLINGTON TWP",
      "ROCHESTER",
      "WELLINGTON",

      // Medina County
      "CHATHAM TWP",
      "HARRISVILLE TWP",
      "HOMER TWP",
      "SPENCER TWP",
      "WESTFIELD CENTER TWP",
      "LODI",
      "SPENCER",
      "WESTFIELD TWP",

      // Richland County
      "BLOOMINGGROVE TWP",
      "BUTLER TWP",
      "FRANKLIN TWP",
      "JEFFERSON TWP",
      "MADISON TWP",
      "MIFFLIN TWP",
      "MONROE TWP",
      "WASHINGTON TWP",
      "WELLER TWP",
      "WORTHINGTON TWP",
      "BELLVILLE",
      "BUTLER",
      "LUCAS",
      "MANSFIELD",

      // Wayne County
      "CHESTER TWP",
      "CLINTON TWP",
      "CONGRESS TWP",
      "FRANKLIN TWP",
      "PLAIN TWP",
      "WAYNE TWP",
      "WOOSTER TWP",
      "BURBANK",
      "CONGRESS",
      "SHREVE",
      "WEST SALEM",
      "WOOSTER"
  };
}
