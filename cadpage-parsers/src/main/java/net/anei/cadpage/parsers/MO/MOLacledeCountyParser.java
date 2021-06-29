package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOLacledeCountyParser extends DispatchA33Parser {

  public MOLacledeCountyParser() {
    super("LACLEDE COUNTY", "MO");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "LACLEDECOES@OMNIGO.COM,noreply@omnigo.com";
  }

  private static final Pattern CROSS_TYPE_PTN = Pattern.compile("(.*?)(?:CON|COUNTY|RURAL|ION|EEN|UT|HWES|(?<=\\b(?:AVE?|DR|LN|MO|RD|ST|\\d{1,3}|^))[EH])");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CROSS_TYPE_PTN.matcher(data.strCross);
    if (match.matches()) data.strCross = match.group(1).trim();

    if (data.strState.isEmpty()) {
      data.strCross = stripFieldEnd(data.strCross, "FIRE");
      if (data.strCross.equals("MO")) {
        data.strState = data.strCross;
        data.strCross = "";
      } else if (data.strCross.endsWith(", MO")) {
        String city = data.strCross.substring(0, data.strCross.length()-4).trim();
        data.strCity = append(data.strCity, " ", city);
        data.strState = "MO";
        data.strCross = "";
      } else if (isCity(data.strCross)) {
        data.strCity = data.strCross;
        data.strCross = "";
      }
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CONWAY",
      "LEBANON",
      "RICHLAND",
      "VILLAGES",
      "EVERGREEN",
      "PHILLIPSBURG",
      "STOUTLAND",

      // Townships
      "AUGLAIZE",
      "ELDRIDGE",
      "FRANKLIN",
      "GASCONADE",
      "LEBANON",
      "OSAGE",
      "PHILLIPSBURG",
      "SPRING HOLLOW",
      "WASHINGTON",

      // Census-designated place
      "Bennett Springs",

      // Other unincorporated places
      "ABO",
      "AGNES",
      "BIDWELL",
      "BROWNFIELD",
      "BRUSH CREEK",
      "CAFFEYVILLE",
      "CARROL JUNCTION",
      "CASE",
      "COMPETITION",
      "DELMAR",
      "DOVE",
      "DREW",
      "DRYNOB",
      "ELDRIDGE",
      "FALCON",
      "GRACE",
      "HAZELGREEN",
      "IRA",
      "LYNCHBURG",
      "LYONS",
      "MORGAN",
      "NEBO",
      "OAKLAND",
      "ORIGANNA",
      "ORLA",
      "PEASE",
      "PROSPERINE",
      "RADAR",
      "RUSS",
      "SAINT ANNIE",
      "SLEEPER",
      "SOUTHARD",
      "WINNIPEG"
  };

}
