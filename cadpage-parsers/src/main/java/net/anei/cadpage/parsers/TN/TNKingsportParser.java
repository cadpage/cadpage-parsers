package net.anei.cadpage.parsers.TN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNKingsportParser extends FieldProgramParser {

  public TNKingsportParser() {
    super("KINGSPORT", "TN",
          "CALL PLACE ADDRCITYST UNIT X GPS1 GPS2 ID NAME PHONE SKIP DATETIME! INFO APT END");
  }

  @Override
  public String getFilter() {
    return "zuercher-noreply@kingsporttn.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("*") && body.endsWith("*")) {
      body = body.substring(1, body.length()-1).trim();
      return parseFields(body.split("\\*"), data);
    }
    if (body.startsWith(":") && body.endsWith(":")) {
      body = body.substring(1, body.length()-1).trim();
      return parseFields(splitFields(body), data);
    }
    return false;
  }

  private static final Pattern DELIM = Pattern.compile("(?<!\\b\\d\\d):|:(?!\\d\\d\\b)");

  private String[] splitFields(String body) {
    Matcher match = INFO_BRK_PTN.matcher(body);
    if (!match.find()) return DELIM.split(body);
    int pt = match.start();
    List<String> flds = new ArrayList<>(Arrays.asList(DELIM.split(body.substring(0, pt).trim())));
    flds.add(body.substring(pt).trim());
    return flds.toArray(new String[0]);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{4}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6}|None");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
