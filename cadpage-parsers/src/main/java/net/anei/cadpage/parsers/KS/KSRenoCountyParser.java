package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Reno County, KS
 */
public class KSRenoCountyParser extends DispatchH05Parser {

  public KSRenoCountyParser() {
    super("RENO COUNTY", "KS",
          "( SELECT/1 ID1 ADDRCITY1 PLACE CALL UNIT! INFO/N+ " +
          "| CALL2 ADDRCITY2 NAME_PHONE? DATETIME2! INFO_BLK/Z+? UNIT2! TIMES+? ID FINAL END )");
  }

  @Override
  public String getFilter() {
    return "Dispatch@renolec.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean keepLeadBreak() {
    return true;
  }

  private static final Pattern DELIM = Pattern.compile(",?\n");
  @Override
  public boolean parseHtmlMsg(String subject, String body, Data data) {

    if (subject.equals("Page Notification")) {
      setSelectValue("1");
      return parseFields(DELIM.split(body), data);
    } else {
      setSelectValue("2");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("\\d{4}-\\d{8}\\b.*|", true);
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT2")) return new UnitField("(?:\\b(?:[A-Z]+\\d+|DC-\\d+|\\S+ ?NOTIFY)\\b[, ]*)+", true);
    if (name.equals("FINAL")) return new SkipField("Final", true);
    return super.getField(name);
  }

  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      int pt = data.strCity.indexOf(',');
      if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
    }
  }

  private static final Pattern CALL2_HDR_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d +(.*)");

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL2_HDR_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1);
      int len = Math.min(field.length()/2, 10);
      int pt = field.indexOf(field.substring(0, len), len);
      if (pt >= 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_CITY2_DELIM = Pattern.compile(" *, *");

  private class MyAddressCity2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String[] parts = ADDR_CITY2_DELIM.split(field, -1);
      if (parts.length < 3) abort();
      parseAddress(parts[0].replace('@', '&'), data);
      data.strCity = parts[1];
      data.strApt = append(data.strApt, "-", parts[2]);
      for (int ndx = 3; ndx < parts.length; ndx++)
      data.strCross = append(data.strCross, " / ", parts[ndx]);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT X";
    }
  }

  private class MyNamePhoneField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) {
        data.strPhone = field.substring(pt);
        field = field.substring(0, pt);
      }
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }

  }
}
