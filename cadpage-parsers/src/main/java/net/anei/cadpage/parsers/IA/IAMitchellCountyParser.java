package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IAMitchellCountyParser extends DispatchA47Parser {

  public IAMitchellCountyParser() {
    super("MCSO dispatch", CITY_LIST, "MITCHELL COUNTY", "IA", ".*");
  }

  @Override
  public String getFilter() {
    return "swmail@somcia.us";
  }

  private static final String[] CITY_LIST =new String[]{

//Cities

    "CARPENTER",
    "MCINTIRE",
    "MITCHELL",
    "ORCHARD",
    "OSAGE",
    "ST ANSGAR",
    "STACYVILLE",
    "RICEVILLE",

//Unincorporated communities

    "LITTLE CEDAR",
    "TOETERVILLE",

//Townships

    "BURR OAK",
    "CEDAR",
    "DOUGLAS",
    "EAST LINCOLN",
    "JENKINS",
    "LIBERTY",
    "MITCHELL",
    "NEWBURG",
    "OSAGE",
    "OTRANTO",
    "ROCK",
    "ST ANSGAR",
    "STACYVILLE",
    "UNION",
    "WAYNE",
    "WEST LINCOLN",

    // Floyd County
    "RUDD"

  };
}
