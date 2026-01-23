package net.anei.cadpage.parsers.IN;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class INPorterCountyParser extends DispatchH03Parser {

  public INPorterCountyParser() {
    super(CITY_CODES, "PORTER COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "PCCC@mail.porterco.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_ADD_DEFAULT_CNTY;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!super.parseMsg(subject, body, data)) return false;

    // Winfield TWP is in Lake county
    if (data.strCity.equals("Crown Point") ||
        data.strCity.equals("Winfield Twp")) data.defCity = "LAKE COUNTY";
    else if (data.strCity.equals("OOC")) {
      data.defCity = "";
      data.strCity = "";
    }
    return true;
  }

  @Override
  public boolean parseFields(String[] flds, Data data) {
    List<String> newFlds = new ArrayList<>();
    for (String fld : flds) {
      fld = fld.trim();
      if (fld.equals("LOCATION DETAILS:")) {
        newFlds.add("INCIDENT DETAILS");
        newFlds.add("LOCATION:");
      } else if (fld.equals("INCIDENT DETAILS")) {
        newFlds.add("INCIDENT:");
      } else if (fld.equals("SECONDARY RESPONSE LOCATION DETAILS:")) {
        newFlds.add("SECONDARY RESPONSE LOCATION:");
      } else {
        newFlds.add(fld);
      }
    }
    return super.parseFields(newFlds.toArray(new String[0]), data);
  }

  @Override
  public String adjustMapAddress(String addr) {
    return EST_PTN.matcher(addr).replaceAll("ESTATES");
  }
  private static final Pattern EST_PTN = Pattern.compile("\\bEST\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BHB", "Burns Harbor",
      "BSH", "Beverly Shores",
      "CHE", "Chesterton",
      "DAC", "Dune Acres",
      "HEB", "Hebron",
      "KTS", "Kouts",
      "OGD", "Ogden Dunes",
      "PTG", "Portage",
      "PTR", "Porter",
      "VAL", "Valparaiso",

      "BNT", "Boone Twp",
      "CCT", "Center Twp",
      "CTT", "Center Twp",
      "ECT", "Eagle Creek Twp",
      "JKT", "Jackson Twp",
      "LBT", "Liberty Twp",
      "MGT", "Morgan Twp",
      "PGT", "Portage Twp",
      "PLT", "Pleasant Twp",
      "PNT", "Pine Twp",
      "POT", "Porter Twp",
      "UNT", "Union Twp",
      "WCT", "Westchester Twp",
      "WGT", "Washington Twp",

      // Lake County
      "CPT", "Crown Point",
      "WNT", "Winfield Twp",

      // OUT OF COUNTY
      "OOC",      "OOC"
  });
}
