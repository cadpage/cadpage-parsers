package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class TXNavarroCountyParser extends DispatchSouthernParser {

  public TXNavarroCountyParser() {
    super(CITY_LIST, "NAVARRO COUNTY", "TX",
          "ADDR/SXP ( ID | X EMPTY EMPTY EMPTY ID ) TIME CALL! CALL/SDS INFO+");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private Pattern DIR_COUNTY_PTN = Pattern.compile("([NS][EW])(COUNTY)");
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = DIR_COUNTY_PTN.matcher(field).replaceAll("$1 $2");
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{

    "NAVARRO COUNTY",

    "ANGUS",
    "BARRY",
    "BLOOMING GROVE",
    "CORSICANA",
    "CHATFIELD",
    "DAWSON",
    "EMHOUSE",
    "EMMETT",
    "EUREKA",
    "FROST",
    "GOODLOW",
    "KERENS",
    "MILDRED",
    "MONTFORT",
    "MUSTANG",
    "NAVARRO",
    "OAK VALLEY",
    "PISGAH",
    "POWELL",
    "PURDON",
    "PURSLEY",
    "RETREAT",
    "RICE",
    "RICHLAND",
    "RURAL SHADE",
    "STREETMAN"
  };





}
