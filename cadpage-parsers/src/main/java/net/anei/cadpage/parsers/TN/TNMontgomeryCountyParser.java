package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class TNMontgomeryCountyParser extends SmartAddressParser {

  public TNMontgomeryCountyParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "TN");
    setFieldList("UNIT CALL PLACE ADDR APT CITY INFO");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "CADpage@mcgtn.net";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]*\\[\\d{1,2}\\][, ]*");
  private static final Pattern INFO_JUNK_PTN =
      Pattern.compile("^Additional info.*|^Automatic Case Number\\(s\\) issued.*|^A cellular re-bid has occurred.*|^Incident linked to.*|^Multi-Agency .*|^Multi-Jurisdiction .*|^Paging Groups Notified:.*|\\[Shared[^\\]]*\\][, ]*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD")) return false;
    boolean first = true;
    for (String part : INFO_BRK_PTN.split(body)) {
      if (first) {
        first = false;
        Parser p = new Parser(part);
        data.strUnit = p.get(' ');
        data.strCall = p.get(" LOC:");
        String addr = p.get();
        if (addr.isEmpty()) return false;
        parseAddress(StartType.START_PLACE, FLAG_ANCHOR_END, addr, data);
      } else {
        part = INFO_JUNK_PTN.matcher(part).replaceAll("").trim();
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[] {
      "101ST AIRBORNE DIVISION",
      "ALEX OVERLOOK",
      "BRITTON SPRINGS",
      "BROOK MEAD",
      "CHARLES THOMAS",
      "CREEK COYOTE",
      "FT CAMPBELL",
      "GOLF CLUB",
      "HAND VILLAGE",
      "JACKIE LORRAINE",
      "PEA RIDGE",
      "PEACHERS MILL",
      "PURPLE HEART",
      "RANCH HILL",
      "TINY TOWN",
      "WILMA RUDOLPH"
  };

  private static final String[] CITY_LIST = new String[] {

      // City
      "CLARKSVILLE",

      // Unincorporated communities
      "CUNNINGHAM",
      "NEEDMORE",
      "NEW PROVIDENCE",
      "OAKRIDGE",
      "PALMYRA",
      "PORT ROYAL",
      "SALEM",
      "SANGO",
      "SHADY GROVE",
      "SOUTH GUTHRIE",
      "SOUTHAVEN",
      "SOUTHSIDE",
      "ST BETHLEHEM",
      "WOODLAWN"
  };

}
