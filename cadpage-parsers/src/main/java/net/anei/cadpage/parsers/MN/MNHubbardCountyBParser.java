package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class MNHubbardCountyBParser extends DispatchProphoenixParser {

  public MNHubbardCountyBParser() {
    super(null, CITY_LIST, "HUBBARD COUNTY", "MN");
  }

  @Override
  public String getFilter() {
    return "NoReply@co.hubbard.mn.us";
  }

  @Override
  protected boolean parseMsg(String body, MsgInfo.Data data) {
    int pt = body.indexOf("\n\n--");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!super.parseMsg(body, data)) return false;
    pt = data.strSupp.indexOf("Names Added :");
    if (pt >= 0) data.strSupp = data.strSupp.substring(0,pt).trim();
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "AKELEY",
      "LAPORTE",
      "NEVIS",
      "PARK RAPIDS",

      // Census-designated places
      "HUBBARD",
      "LAKE GEORGE",

      // Unincorporated communities[6]
      "BADOURA",
      "BECIDA",
      "BENEDICT",
      "CHAMBERLAIN",
      "DORSET",
      "EMMAVILLE",
      "KABEKONA",
      "NARY",

      // Townships
      "AKELEY TWP",
      "ARAGO TWP",
      "BADOURA TWP",
      "CLAY TWP",
      "CLOVER TWP",
      "CROW WING LAKE TWP",
      "FARDEN TWP",
      "FERN TWP",
      "GUTHRIE TWP",
      "HART LAKE TWP",
      "HELGA TWP",
      "HENDRICKSON TWP",
      "HENRIETTA TWP",
      "HUBBARD TWP",
      "LAKE ALICE TWP",
      "LAKE EMMA TWP",
      "LAKE GEORGE TWP",
      "LAKE HATTIE TWP",
      "LAKEPORT TWP",
      "MANTRAP TWP",
      "NEVIS TWP",
      "ROCKWOOD TWP",
      "SCHOOLCRAFT TWP",
      "STEAMBOAT RIVER TWP",
      "STRAIGHT RIVER TWP",
      "THORPE TWP",
      "TODD TWP",
      "WHITE OAK TWP"
  };

}
