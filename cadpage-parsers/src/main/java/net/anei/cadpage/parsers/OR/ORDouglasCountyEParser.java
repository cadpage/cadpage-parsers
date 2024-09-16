package net.anei.cadpage.parsers.OR;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ORDouglasCountyEParser extends DispatchH05Parser {

  public ORDouglasCountyEParser() {
    super("DOUGLAS COUNTY", "OR",
          "Nature:CALL! Dispatched:UNIT! CFS_#:ID? Date_&_Time:DATETIME! Location:ADDRCITY! Place_Name:PLACE! Cross_Streets:X! " +
              "Lat/Lon:GPS! Map_Link:SKIP! Narrative:INFO! INFO_BLK+ Alerts:EMPTY! ALERT+");
  }

  @Override
  public String getFilter() {
    return "dccad-NoReply2@douglascountyor.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SPLIT_PTN = Pattern.compile("/ (?=(?:Dispatched|CFS #|Date & Time|Place Name|Cross Streets|Map Link):)");

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    List<String> flds2 = new ArrayList<>();
    for (String fld : flds) {
      for (String part : SPLIT_PTN.split(fld)) {
        flds2.add(part);
      }
    }

    return super.parseFields(flds2.toArray(new String[0]), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  private static final Pattern ALERT_JUNK_PTN = Pattern.compile("Location|Fire|Police|Police/Fire|Gate Code|- *\\d *-");
  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (ALERT_JUNK_PTN.matcher(field).matches()) return;
      field = stripFieldStart(field, "Converted Record ");
      data.strAlert = append(data.strAlert, "\n", field);
    }
  }
}
