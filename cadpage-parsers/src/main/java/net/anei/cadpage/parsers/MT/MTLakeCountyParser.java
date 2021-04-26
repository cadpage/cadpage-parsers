package net.anei.cadpage.parsers.MT;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class MTLakeCountyParser extends DispatchA49Parser {

  public MTLakeCountyParser() {
    this("LAKE COUNTY", "MT");
  }

  MTLakeCountyParser(String defCity, String defState) {
    super(defCity, defState, CALL_CODES);
  }

  @Override
  public String getFilter() {
    return "cadpage@e9.com";
  }

  @Override
  public String getAliasCode() {
    return "MTLakeCounty";
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "ALARMBUR", "ALARM BURGLAR",
      "ALARMFIR", "ALARM FIRE",
      "ALARMMED", "ALARM MEDICAL",
      "AMB",      "AMBULANCE",
      "AMBASST",  "AMBULANCE ASSIST",
      "AMBFLU",   "AMBULANCE COVID SYMPTOMS",
      "AMBTRANS", "AMBULANCE TRANSPORT",
      "ASSAULT",  "ASSAULT",
      "CRISIS",   "SUICIDE ATTEMPT",
      "FIREBUIL", "FIRE BUILDING",
      "FIREDEB",  "FIRE DEBRIS",
      "FIREGRAS", "FIRE GRASS",
      "FIREMISC", "FIRE MISC",
      "FIREVEH",  "FIRE VEHICLE",
      "FIREWOOD", "FIRE WOODS",
      "GOVASST",  "GOVERNMENT ASSIST",
      "LAWASST",  "LAW ENFORCEMENT ASSIST",
      "MISSING",  "MISSING PERSON",
      "PAGETEST", "PAGER TEST",
      "PFMAIP",   "LAW ENFORCEMENT",
      "PROPNGAS", "PROPANE GAS",
      "PUBASST",  "PUBLIC ASSIST",
      "RDDEBRIS", "ROAD DEBRIS",
      "SINKWATR", "WATER RESCUE - BOAT SINKING",
      "SUICATT",  "SUICIDE ATTEMPT",
      "UNK-F",    "VEHICLE CRASH UNK INJURY",
      "UTILITY",  "UTILITY LINE",
      "VEHCRASH", "VEHICLE CRASH",
      "VEHINJUR", "VEHICLE CRASH WITH INJURY",
      "VEHPROP",  "VEHICLE CRASH NO INJURY",
      "VEHSLID",  "VEHICLE SLID OFF ROAD",
      "VOIDTEST", "PAGER TEST",
      "WTROTHER", "WATER OTHER",
      "WTRRESCU", "WATER RESCUE"
  });

}
