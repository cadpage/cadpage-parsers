package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Lafayette Parish, LA
*/

public class LALafayetteParishParser extends FieldProgramParser {

  public LALafayetteParishParser() {
    super(CITY_CODES, "LAFAYETTE PARISH","LA",
          "Event_Date:DATETIME! Unit:UNIT! CAD:ID! Address:ADDR! Intersection:X! Jurisdiction:CITY? Event_Type:CODE_CALL! Report:ID! Remarks:INFO? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadalert@lafayettela.gov,alerts@lpcdops-lafla.gov,alerts@tailorbuilt.app";
  }

  private static final Pattern TRAIL_HASH_PTN = Pattern.compile("(?<=Unit|CAD|Report)#");
  private static final Pattern DELIM = Pattern.compile("\n|\\s+(?=(?:Unit|CAD|Intersection|Jurisdiction):)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("System Alert")) return false;
    body = TRAIL_HASH_PTN.matcher(body).replaceAll(":");
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("\\((.*?)\\) *(.*)");

  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1).trim();
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    sAddress = TW_PTN.matcher(sAddress).replaceAll("THRUWAY");
    if (cross) {
      Matcher match = CROSS_HOUSE_PTN.matcher(sAddress);
      if (match.matches()) sAddress = match.group(1);
    }
    return super.adjustMapAddress(sAddress, cross);
  }
  private static final Pattern TW_PTN = Pattern.compile("\\bTW\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CROSS_HOUSE_PTN = Pattern.compile("\\d+ +(.*)");

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "LAF PARISH",   "LAFAYETTE PARISH"
  });
}
