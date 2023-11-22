package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCSumterCountyAParser extends FieldProgramParser {

  public SCSumterCountyAParser() {
    super("SUMTER COUNTY", "SC",
          "ID ADDRCITYST PLACE APT X EMPTY BOX EMPTY EMPTY EMPTY INFO EMPTY MAP CALL NAME PHONE UNIT INFO2 SKIP ID2/L! END");
  }

  @Override
  public String getFilter() {
    return "zuercher@sumtersc.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Message From Zuercher")) return false;
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{10}", true);
    if (name.equals("ID2")) return new MyId2Field();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|UNIT) *(.*)", Pattern.CASE_INSENSITIVE);

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z0-9,]+");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      if (!UNIT_PTN.matcher(field).matches()) abort();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO2_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - *");

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO2_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(field, "\n\n", data.strSupp);
    }
  }

  private class MyId2Field extends IdField {
    public MyId2Field() {
      super("EMS\\d{9}|None", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
