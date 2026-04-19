package net.anei.cadpage.parsers.GA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class GASchleyCountyAParser extends DispatchA19Parser {

  public GASchleyCountyAParser() {
    this("SCHLEY COUNTY");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  public GASchleyCountyAParser(String defCounty) {
    super(defCounty, "GA");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,middleflint@mfre911.com,dispatch@mfre911.com";
  }

  @Override
  public String getAliasCode() {
    return "GASchleyCounty";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strPlace = fixCounty(data.strPlace, data);
    data.strApt = fixCounty(data.strApt, data);
    return true;
  }

  private String fixCounty(String fld, Data data) {
    String fld1, fld2;
    int pt = fld.lastIndexOf(" - ");
    if (pt >= 0) {
      fld1 = fld.substring(0,pt).trim();
      fld2 = fld.substring(pt+3).trim();
    } else {
      fld1 = "";
      fld2 = fld;
    }
    if (COUNTY_SET.contains(fld2.toUpperCase())) {
      if (data.strCity.isEmpty()) data.strCity = fld2;
      return fld1;
    } else {
      return fld;
    }
  }

  private static final Set<String> COUNTY_SET = new HashSet<>(Arrays.asList(
      "CHATTAHOOCHEE",
      "CRAWFORD",
      "CRISP",
      "DOOLY",
      "HOUSTON",
      "LEE",
      "MACON",
      "MARION",
      "PEACH",
      "PULASKI",
      "RANDOLPH",
      "SCHLEY",
      "STEWART",
      "SUMTER",
      "TALBOT",
      "TAYLOR",
      "TERRELL",
      "UPSON",
      "WEBSTER",
      "WILCOX"
  ));

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "568 HWY 280 W",                        "+32.056300,-84.288500"
  });
}
