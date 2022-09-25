package net.anei.cadpage.parsers.VA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAGraysonCountyParser extends DispatchSouthernParser {

  public VAGraysonCountyParser() {
    this("GRAYSON COUNTY", "VA");
  }

  VAGraysonCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          DSFLG_PROC_EMPTY_FLDS|DSFLG_ADDR|DSFLG_BAD_PLACE|DSFLG_X|DSFLG_NAME|DSFLG_PHONE|DSFLG_CODE|DSFLG_UNIT1|DSFLG_ID |DSFLG_TIME);
  }

  @Override
  public String getAliasCode() {
    return "VAGraysonCounty";
  }

  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (!super.parseMsg(body, data)) return false;

    if (data.strCity.length() == 0 && ZIP_PTN.matcher(data.strApt).matches()) {
      data.strCity = data.strApt;
      data.strApt = "";
    }

    if (NC_CITY_SET.contains(data.strCity.toUpperCase())) data.strState = "NC";

    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new String[]{

      // Grayson County
      // Towns
      "FRIES",
      "INDEPENDENCE",
      "TROUTDALE",

      // Unincorporated communities
      "BAYWOOD",
      "CARSONVILLE",
      "COMERS ROCK",
      "ELK CREEK",
      "FAIRVIEW",
      "FLAT RIDGE",
      "GRANT",
      "MOUTH OF WILSON",
      "RUGBY",
      "VOLNEY",
      "WHITETOP",

      // Carroll County
      // Town
      "HILLSVILLE",

      // Census-designated places
      "CANA",
      "FANCY GAP",
      "WOODLAWN",

      // Other unincorporated communities
      "AUSTINVILLE",
      "DUGSPUR",
      "LAMBSBURG",
      "LAUREL FORK",
      "SYLVATUS",

      // Floyd County
      "INDIAN VALLEY",

      // Indendent city
      "GALAX",

      // ALleghany County, NC
      "SPARTA",

      // Wythe County
      "AUSTINVILLE",
      "IVANHOE",

      // North Carolina??
      "WINSTON",
      "27030"
  };

  private static final Set<String> NC_CITY_SET = new HashSet<String>(Arrays.asList(
      "SPARTA",
      "WINSTON",
      "27030"
  )) ;
}
