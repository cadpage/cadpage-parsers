package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


public class KSSedgwickCountyParser extends DispatchH05Parser {

  public KSSedgwickCountyParser() {
    super("SEDGWICK COUNTY", "KS",
          "PLACE:PLACE! ADDR:ADDRCITYAPT! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY! INFO_BLK/N+");
  }

  @Override
  public String getFilter() {
    return "911_notify@sedgwick.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.equals("HWY")) return true;
    return super.isNotExtraApt(apt);
  }

}
