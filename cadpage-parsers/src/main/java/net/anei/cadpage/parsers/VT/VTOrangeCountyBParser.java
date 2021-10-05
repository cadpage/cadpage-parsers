package net.anei.cadpage.parsers.VT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class VTOrangeCountyBParser extends DispatchA32Parser {

  public VTOrangeCountyBParser() {
    super(CITY_LIST, "ORANGE COUNTY", "VT");
  }

  @Override
  public String getFilter() {
    return "hanoverdispatch@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("_")) return false;
    if (! super.parseMsg("Page", body, data)) return false;
    if (NH_CITY_SET.contains(data.strCity)) data.strState = "NH";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "BRADFORD",
      "BRAINTREE",
      "BROOKFIELD",
      "CHELSEA",
      "CORINTH",
      "FAIRLEE",
      "NEWBURY",
      "ORANGE",
      "RANDOLPH",
      "STRAFFORD",
      "THETFORD",
      "TOPSHAM",
      "TUNBRIDGE",
      "VERSHIRE",
      "WASHINGTON",
      "WEST FAIRLEE",
      "WILLIAMSTOWN",

      // Villages
      "NEWBURY",
      "WELLS RIVER",

      // Census-designated places
      "BRADFORD",
      "CHELSEA",
      "FAIRLEE",
      "RANDOLPH",
      "WILLIAMSTOWN",

      // Unincorporated community
      "POST MILLS",

      // Grant County, NH
      "ORFORD"
  };

  private static final Set<String> NH_CITY_SET = new HashSet<>(Arrays.asList("ORFORD"));
}
