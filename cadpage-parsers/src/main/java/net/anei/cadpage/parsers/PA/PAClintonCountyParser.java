package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 Clinton County, PA
 */
public class PAClintonCountyParser extends DispatchH05Parser {

  public PAClintonCountyParser() {
    super("CLINTON COUNTY", "PA",
          "Final%EMPTY? ( CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY/S6! CROSS_ST:X! ID:SKIP! PRI:PRI! " +
                       "| DATETIME CALL PLACE ADDRCITY/S6! BOX CROSS_ST:X! " +
                       ") DATE:DATETIME! MAP:SKIP! GPS? UNIT:UNIT! Nature_of_Call:INFO? ( INFO:EMPTY! | *INFO:EMPTY! ) INFO_BLK/Z+? ID! TIMES+ CONFIDENTIALITY_NOTICE:SKIP");
  }

  @Override
  public String getFilter() {
    return "no-reply@clintoncountypa.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseFields(String[] flds, Data data) {
    for (int ii = 1; ii<flds.length; ii++) {
      String fld = flds[ii].trim();
      if (fld.startsWith(".")) {
        flds[ii] = stripFieldEnd(fld.substring(1).trim(), ".");
      }
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("BOX")) return new BoxField("(?:Box|BOX) +(\\S+)", true);
    if (name.equals("GPS")) return new GPSField("https://www.google.com.*query=(.*)", true);
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern ID_NOT_ASSIGNED = Pattern.compile("(?:, *)?\\[Incident not yet created \\d+\\]");
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return false;
      if (!field.startsWith("[") || !field.endsWith("]")) return false;
      field = ID_NOT_ASSIGNED.matcher(field).replaceAll("");
      data.strCallId = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}