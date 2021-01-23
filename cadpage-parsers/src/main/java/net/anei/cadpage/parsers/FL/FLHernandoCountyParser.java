package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;


public class FLHernandoCountyParser extends DispatchH01Parser {
  public FLHernandoCountyParser() {
    super(CITY_LIST, "HERNANDO COUNTY", "FL",
          "( MARK! Workstation:SKIP! Print_Time:SKIP! User:SKIP! Location:ADDR/S! Response_Type:CALL! Zone_Name:MAP! Priority_Name:PRI Creation_Time:SKIP! Sequence_Number:ID! Status_Name:SKIP! Status_Time:DATETIME! Handling_Unit:UNIT! Agency:SRC! NOTES+ " +
          "| STATUS CALL ADDR MAP UNIT! )");
  }

  @Override
  public String getFilter() {
    return "@hernandosheriff.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Notification") && !subject.equals("Notification")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    if (fields.length == 0) return false;
    if (fields.length <= 2) fields = fields[0].split("\n");
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField("Response Email Report lite", true);
    if (name.equals("PRI")) return new PriorityField("(\\d) Priority \\d", true);
    if (name.equals("STATUS")) return new SkipField("Dispatch|Responding");
    return super.getField(name);
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BROOKSVILLE",
      "RIDGE MANOR",

      // Census-designated places
      "ARIPEKA",
      "BAYPORT",
      "BROOKRIDGE",
      "GARDEN GROVE",
      "HERNANDO BEACH",
      "HIGH POINT",
      "HILL 'N DALE",
      "ISTACHATTA",
      "LAKE LINDSEY",
      "MASARYKTOWN",
      "NOBLETON",
      "NORTH BROOKSVILLE",
      "NORTH WEEKI WACHEE",
      "PINE ISLAND",
      "RIDGE MANOR",
      "SOUTH BROOKSVILLE",
      "SPRING HILL",
      "SPRING LAKE",
      "TIMBER PINES",
      "WEEKI WACHEE",
      "WEEKI WACHEE GARDENS",
      "WISCON",

      // Other unincorporated communities
      "ROLLING ACRES",
      "ROYAL HIGHLANDS",

      // Pasco County
      "DADE CITY"
  };
}