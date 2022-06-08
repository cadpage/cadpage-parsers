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
          "( SELECT/1 CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! CROSS_ST:X! ID:SKIP! PRI:PRI! " +
          "| EMPTY Final%EMPTY? DATETIME CALL PLACE ADDRCITY! BOX CROSS_ST:X! " +
          ") DATE:DATETIME! MAP:SKIP! UNIT:UNIT! Nature_of_Call:INFO? INFO:EMPTY! INFO_BLK/Z+? ID! TIMES+");
  }

  @Override
  public String getFilter() {
    return "@ClintonCountyPA.com,@ClintonCountyPA.gov";
  }

  private static final Pattern INFO_TIME_PTN = Pattern.compile("\n(\\d\\d:\\d\\d:\\d\\d) {8,}");
  private static final Pattern DELIM = Pattern.compile("\\.*\n\\.*(?:(?<!\n)\\*)?");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<")) {
      setSelectValue("1");
      return super.parseHtmlMsg(subject, body, data);
    } else {
      setSelectValue("2");
      body = INFO_TIME_PTN.matcher(body).replaceAll("\n$1\n");
      return parseFields(DELIM.split(body), data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("Box +(\\S+)", true);
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
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