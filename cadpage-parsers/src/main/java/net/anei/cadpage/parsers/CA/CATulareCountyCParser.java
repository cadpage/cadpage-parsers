package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class CATulareCountyCParser extends DispatchA19Parser {

  public CATulareCountyCParser() {
    super(CITY_CODES, "TULARE COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,dispatch@ci.porterville.ca.us";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AL", "ALPAUGH",
      "AV", "ALPINE VILLAGE",
      "AW", "ALLENSWORTH",
      "BA", "BADGER",
      "CF", "COY FLAT",
      "CH", "CALIFORNIA HOT SPRINGS",
      "CN", "PERIPOINT",
      "CO", "DRUM VALLEY",
      "CR", "CORCORAN",
      "CS", "CEDAR SLOPES",
      "CU", "ORTEGA CAMP",
      "DC", "DUCOR",
      "DI", "MONSON",
      "DL", "DELANO",
      "EM", "EARLIMART",
      "EX", "YOKOHL VALLEY",
      "FM", "UNION EDITION",
      "FS", "FOUNTAIN SRPINGS",
      "FU", "CUTLER",
      "GO", "GOSHEN",
      "HL", "HARTLAND",
      "ID", "IDLEWILD",
      "IV", "IVANHOE",
      "JD", "KERN CANYON",
      "KB", "KINGSBURG",
      "KM", "KENNEDY MEADOWS",
      "LC", "LINNEL CAMP",
      "LD", "LONDON",
      "LE", "LEMONCOVE",
      "LI", "TONYVILLE",
      "OC", "ORANGE COVE",
      "OR", "OROSI",
      "PF", "PINE FLAT",
      "PH", "PANORAMA HEIGHTS",
      "PO", "POPLAR",
      "PP", "POSO PARK",
      "PV", "PORTERVILLE",   // Was Tulle Indian Reservation
      "PX", "PIXLEY",
      "PY", "POSEY",
      "QA", "QUAKING ASPEN",
      "RG", "RICHGROVE",
      "RY", "REEDLEY",
      "SC", "SEQUOIA CREST",
      "SM", "STRATHMORE",
      "SP", "YOKOHL VALLEY",
      "SQ", "GRANT GROVE",
      "SU", "SULTANA",
      "SV", "SEVILLE",
      "TB", "TERRA BELLA",
      "TC", "PORTERVILLE",   // New
      "TH", "THREE RIVERS",
      "TP", "TIPTON",
      "TR", "TRAVER",
      "TU", "TULARE VILLA",
      "TV", "TEVISTON",
      "VI", "VISALIA SALES YARD",
      "WD", "WOODLAKE",
      "WK", "WAUKENA",
      "WS", "WILSONIA",
      "WV", "WOODVILLE",
      "YM", "YETTEM"

  });
}
