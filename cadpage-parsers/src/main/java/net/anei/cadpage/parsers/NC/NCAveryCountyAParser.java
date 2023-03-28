package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCAveryCountyAParser extends DispatchSouthernParser {

  public NCAveryCountyAParser() {
    super(CITY_LIST, "AVERY COUNTY", "NC",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME );
    setCallPtn("\\S+");
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([^-]+)--(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      if (data.strCode.isEmpty()) data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    data.strCall = data.strCall.replace('-', ' ').trim();
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "BANNER ELK",
      "BEECH MOUNTAIN",
      "CROSSNORE",
      "ELK PARK",
      "NEWLAND",
      "SEVEN DEVILS",

      // Villages
      "GRANDFATHER",
      "SUGAR MOUNTAIN",

      // Townships
      "ALTAMONT TWP",
      "BANNER ELK TWP",
      "BEECH MOUNTAIN TWP",
      "CAREYS FLAT TWP",
      "CRANBERRY TWP",
      "ELK PARK TWP",
      "FRANK TWP",
      "HEATON TWP",
      "HUGHES TWP",
      "INGALLS TWP",
      "LINVILLE TWP",
      "MINNEAPOLIS TWP",
      "MONTEZUMA TWP",
      "NEWLAND TWP",
      "PINEOLA TWP",
      "PLUMTREE TWP",
      "PYATTE TWP",
      "ROARING CREEK TWP",

      /// Unincorporated communities
      "ALTAMONT",
      "CRANBERRY",
      "FRANK",
      "GRAGG",
      "HEATON",
      "INGALLS",
      "LINVILLE",
      "LINVILLE FALLS",
      "MINNEAPOLIS",
      "MONTEZUMA",
      "PINEOLA",
      "PLUMTREE",
      "ROARING CREEK",
      "THREE MILE",
      "VALE",

      // Mitchell County
      "SPRUCE PINE"
  };

}
