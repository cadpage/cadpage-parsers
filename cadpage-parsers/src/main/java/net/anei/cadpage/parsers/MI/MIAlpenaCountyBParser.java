package net.anei.cadpage.parsers.MI;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIAlpenaCountyBParser extends HtmlProgramParser {

  public MIAlpenaCountyBParser() {
    super("ALPENA COUNTY", "MI",
          "General_Call_Information%EMPTY! Unit_Assigned:UNIT! Incident_Number:ID! Pickup_Address:ADDRCITYST! Requested_Time:LINFO! " +
              "Emergency_Call_Information%EMPTY! Complaint:CALL! ProQa:INFO! Comments:INFO/N! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "sa@logis.dk";
  }

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    List<String> newFlds = new ArrayList<>();
    for (String line : flds) {
      for (String fld : line.split("\n")) {
        newFlds.add(fld.trim());
      }
    }
    return super.parseFields(newFlds.toArray(new String[0]), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PTN = Pattern.compile("([^\\(]*)\\((.*)\\) *(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|UNIT) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      String addr = match.group(1).trim();
      String extra = match.group(2).trim();
      String apt = match.group(3);

      match = ADDR_PTN.matcher(extra);
      if (match.matches()) {
        data.strPlace = addr;
        addr = match.group(1);
        apt = append(match.group(3), "-", apt);
      }
      super.parse(addr, data);
      if (!apt.isEmpty()) {
        match = APT_PTN.matcher(apt);
        if (match.matches()) apt = match.group(1);
        data.strApt = append(data.strApt, "-", apt);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR CITY ST APT";
    }
  }
}
