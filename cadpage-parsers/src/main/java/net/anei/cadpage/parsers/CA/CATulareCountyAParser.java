package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Tulare County, CA
*/

public class CATulareCountyAParser extends DispatchA49Parser {

  public CATulareCountyAParser() {
    super(CITY_CODES, "TULARE COUNTY","CA", CALL_CODES);
  }

  @Override
  public String getFilter() {
    return "ADSI_CAD@co.tulare.ca.us";
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = RNN_PTN.matcher(addr).replaceAll("RD $1");
    addr = ANN_PTN.matcher(addr).replaceAll("AVE $1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern RNN_PTN = Pattern.compile("\\bR(\\d+) RD\\b");
  private static final Pattern ANN_PTN = Pattern.compile("\\bA(\\d+) AVE?\\b");

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AL", "ALPAUGH",
      "AW", "ALLENSWORTH",
      "BA", "BADGER",
      "BW", "BOB WILEY DETENTION FACILITY",
      "CH", "CALIFORNIA HOT SPRINGS",
      "CN", "CAMP NELSON",
      "CO", "COUNTY",
      "CR", "CORCORAN",
      "CS", "CEDAR SLOPE",
      "CU", "CUTLER",
      "DC", "DUCOR",
      "DE", "DELANO",
      "DI", "DINUBA",
      "EM", "EARLIMART",
      "EX", "EXETER",
      "FA", "FAMILY SUPPORT",
      "FM", "FARMERSVILLE",
      "FS", "FOUNTAIN SPRINGS",
      "GO", "GOSHEN",
      "HQ", "HEADQUARTERS",
      "ID", "IDLEWILD",
      "IV", "IVANHOE",
      "JD", "JOHNSONDALE",
      "KB", "KINGSBURG",
      "KM", "KENNEDY MEADOWS",
      "LC", "LINNELL CAMP",
      "LD", "LONDON",
      "LE", "LEMON COVE",
      "LI", "LINDSAY",
      "MJ", "MAIN JAIL",
      "NP", "ASH MOUNTAIN",
      "OC", "ORANGE COVE",
      "OR", "OROSI",
      "PF", "PINE FLAT",
      "PH", "PANORAMA HEIGHTS",
      "PL", "PLAINVIEW",
      "PO", "POPLAR",
      "PP", "POSO PARK",
      "PR", "PROBATION",
      "PV", "PORTERVILLE",
      "PX", "PIXLEY",
      "PY", "POSEY",
      "QA", "QUAKING ASPEN",
      "RG", "RICHGROVE",
      "RV", "RESERVES",
      "RY", "REEDLEY",
      "SM", "STRATHMORE",
      "SP", "SPRINGVILLE",
      "SQ", "SEQUOIA PARK",
      "SU", "SULTANA",
      "SV", "SEVILLE",
      "TB", "TERRA BELLA",
      "TC", "CORRECTIONAL CENTER",
      "TH", "THREE RIVERS",
      "TP", "TIPTON",
      "TR", "TRAVER",
      "TU", "TULARE",
      "VI", "VISALIA",
      "WD", "WOODLAKE",
      "WK", "WAUKENA",
      "WV", "WOODVILLE",
      "YM", "YETTEM"
  });

  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "ALRM", "ALARM SOUNDING",
      "FDEB", "DEBRIS,TRASH",
      "FGRS", "GRASS FIRE",
      "FUNK", "UNKNOWN TYPE FIRE",
      "FVEH", "VEHICLE FIRE",
      "FWLD", "WILDLAND FIRE (SRA)",
      "M83",  "GUN SHOT WOUND",
      "MED",  "BASIC MEDICAL AID",
      "MISC", "MISCELLANEOUS CALLS",
      "MVA",  "TRAFFIC ACCIDENT"
  });
}
