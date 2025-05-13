package net.anei.cadpage.parsers.NM;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class NMSanJuanCountyBParser extends DispatchH05Parser {

  public NMSanJuanCountyBParser() {
    super("SAN JUAN COUNTY", "NM",
          "( DATETIME CALL ADDRCITY2 X ( GPS | PLACE GPS | ) INFO_BLK/Z+? UNIT/Z ID! CFS:SKIP! Alerts:ALERT! " +
          "| ID DATETIME ADDRCITY X ( GPS | PLACE GPS! | ) ( UNIT/Z ID2 INFO_BLK/Z+? UNIT2 " +
                                                           "| INFO_BLK+ https:SKIP! UNIT! SRC! " +
                                                           ") " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "sjccaadmin@sjcounty911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY2")) return new AddressCityField("LOC\\b *(.*)", true);
    if (name.equals("X")) return new CrossField("Cross\\b *(.*)", true);
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ID2")) return new MyId2Field();
    if (name.equals("UNIT2")) return new MyUnit2Field();
    return super.getField(name);
  }

  private class MyGPSField extends GPSField {
    public MyGPSField() {
      super("[-+]?\\d{2,3}\\.\\d{4,}\\b,? *[-+]?\\d{2,3}\\.\\d{4,}", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (!field.contains(",")) field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }

  private class MyId2Field extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(data.strCallId);
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+");
  private class MyUnit2Field extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return isLastField() && UNIT_PTN.matcher(field).matches();
    }
  }

}
