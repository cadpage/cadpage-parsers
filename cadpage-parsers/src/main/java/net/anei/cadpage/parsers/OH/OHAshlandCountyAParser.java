package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHAshlandCountyAParser extends DispatchEmergitechParser {
  
  public OHAshlandCountyAParser() {
    super("911:", CITY_LIST, "ASHLAND COUNTY", "OH", TrailAddrType.INFO);
  }
  
  @Override
  public String getFilter() {
    return "911@ashlandcountysheriff.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("\\d{4}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("911:") && SUBJECT_PTN.matcher(subject).matches()) {
      body = "911:[" + subject + "]" + body;
    }
    return super.parseMsg(body, data);
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
      "WIDOWVILLE"


  };
}
