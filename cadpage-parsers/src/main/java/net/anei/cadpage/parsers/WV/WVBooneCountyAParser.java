package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class WVBooneCountyAParser extends DispatchEmergitechParser {

  public WVBooneCountyAParser() {
    super(true, CITY_LIST, "BOONE COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "paging@boonewv.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) body = subject + ": " + body;
    if (!super.parseMsg(body,  data)) return false;
    if (data.strCity.equals("U.S. RT 119") ||  data.strCity.equals("UPPER RT 85")) data.strCity = "";
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), MAP_CITIES);
  }

  private static final String[] CITY_LIST = {

    // Incorporated Cities
    "DANVILLE",
    "MADISON",
    "SYLVESTER",
    "WHITESVILLE",

    // Unincorporated towns and cities - Many of which Google cannot find!
    "ANDREW",
    "ASHFORD",
    "BALD KNOB",
    "BANDYTOWN",
    "BARRETT",
    "BIGSON",
    "BIG UGLY",
    "BIM",
    "BLOOMINGROSE",
    "BLUE PENNANT",
    "BOB WHITE",
    "BRADLEY",
    "BRANHAM HEIGHTS",
    "BRUSHTON",
    "BULL CREEK",
    "CAMEO",
    "CAMP CREEK",
    "CAZY",
    "CAZY BOTTOM",
    "CLINTON",
    "CLINTON CAMP",
    "CLOTHIER",
    "COMFORT",
    "COOPERTOWN",
    "COSTA",
    "COXS FORK",
    "DARTMONT",
    "DODDSON FORK",
    "DODSON JUNCTION",
    "DOG HOLLOW OF MORR",
    "DRAWDY",
    "EASLY",
    "EDEN",
    "ELK RUN JUNCTION",
    "EUNICE",
    "EMMONS",
    "FOCH",
    "FORK CREEK",
    "FOSTER",
    "FOSTER HOLLOW",
    "FOSTER HOLLOW LEFT",
    "FOSTERVILLE",
    "GARRISON",
    "GORDON",
    "GREENVIEW",
    "GREENWOOD",
    "GRIPPE",
    "HADDLETON",
    "HAVANA",
    "HEWETT",
    "HOLLY HILLS",
    "HOPKINS FORK",
    "INDIAN CREEK",
    "JANIE",
    "JEFFREY",
    "JOES CREEK",
    "JULIAN",
    "KEITH",
    "KIRBYTON",
    "KOHLSAAT",
    "LANTA",
    "LAUREL CITY",
    "LAUREL ESTATES",
    "LICK CREEK",
    "LINDYTOWN",
    "LITTLE HORSE CREEK",
    "LORY",
    "LOW GAP",
    "MANILA",
    "MARNIE",
    "MARTHATOWN",
    "MAXINE",
    "MEADOW FORK",
    "MIDDLE HORSE CREEK",
    "MIDWAY",
    "MILLTOWN",
    "MISSOURI FORK",
    "MORRISVALE",
    "MUD RIVER",
    "NELSON",
    "NELLIS",
    "NEWPORT",
    "NORTH FORK",
    "ORGAS",
    "OTTAWA",
    "PD FORK",
    "PEYTONA",
    "PONDCO",
    "POWELL CREEK",
    "PRENTER",
    "PRICE HILL",
    "PRICE HOLLOW OF AS",
    "QUINLAND",
    "RACINE",
    "RACINE HILL",
    "RAMAGE",
    "RIDGEVIEW",
    "ROBINSON",
    "ROCK CASTLE",
    "ROCK CREEK",
    "ROUNDBOTTOM OF PEY",
    "RUMBLE",
    "SAND LICK OF PRENT",
    "SECOAL",
    "SENG CREEK",
    "SETH",
    "SHARLOW",
    "SHORT CREEK",
    "SIX MILE",
    "SOUTH MADISON",
    "SPARS CREEK",
    "SPRING HOLLOW",
    "SPRUCE LAUREL",
    "STRINGTOWN",
    "TONEYS BRANCH",
    "TURTLE CREEK",
    "TWILIGHT",
    "TWIN POPLARS",
    "UNEEDA",
    "VAN",
    "WASHINGTON HEIGHTS",
    "WHARTON",
    "WILLIAMS MOUNTAIN"
  };

  private static final Properties MAP_CITIES = buildCodeTable(new String[]{
    "BIG UGLY",             "DANVILLE",
    "BULL CREEK",           "WHARTON",
    "CAZY BOTTOM",          "WHARTON",
    "CLINTON CAMP",         "WHARTON",
    "COXS FORK",            "DANVILLE",
    "DODDSON FORK",         "SPURLOCKVILLE",
    "DOG HOLLOW OF MORR",   "SPURLOCKVILLE",
    "DRAWDY",               "1",
    "GREENWOOD",            "WHARTON",
    "INDIAN CREEK",         "RACINE",
    "JOES CREEK",           "DANVILLE",
    "LITTLE HORSE CREEK",   "JULIAN",
    "LAUREL CITY",          "OTTAWA",
    "LAUREL ESTATES",       "BOB WHITE",
    "MANILA",               "2",
    "MAXINE",               "1",
    "MEADOW FORK",          "2",
    "MISSOURI FORK",        "HEWETT",
    "NEWPORT",              "DANVILLE",
    "PD FORK",              "FOSTER",
    "PRICE HOLLOW OF AS",   "ASHFORD",
    "QUINLAND",             "2",
    "ROBINSON",             "MADISON",
    "ROCK CASTLE",          "1",
    "ROCK CREEK",           "FOSTER",
    "ROUNDBOTTOM OF PEY",   "PEYTONA",
    "SAND LICK OF PRENT",   "SETH",
    "SPARS CREEK",          "DANVILLE",
    "SENG CREEK",           "1",
    "SHORT CREEK",          "RACINE",
    "SPRING HOLLOW",        "WHARTON",
    "STRINGTOWN",           "JEFFREY",
    "TONEYS BRANCH",        "1",
    "TWIN POPLARS",         "ORGAS"
  });
}
