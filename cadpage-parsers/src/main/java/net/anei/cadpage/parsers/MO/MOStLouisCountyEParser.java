package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStLouisCountyEParser extends FieldProgramParser {

  public MOStLouisCountyEParser() {
    super(CITY_LIST, "ST LOUIS COUNTY", "MO", "Call_Received_Time:DATE_TIME_CALL! ADDR/S! UNIT_PLACE! Description:INFO+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("_Dispatch")) return false;

    body = body.replace("Description:", "\nDescription:");

    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_CALL")) return new MyDateTimeCallField();
    if (name.equals("UNIT_PLACE")) return new MyUnitPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_CALL_PATTERN = Pattern.compile(" *(\\d{1,2}/\\d{1,2}/\\d{4}) *(\\d{2}:\\d{2}:\\d{2}) *(.*?)");

  private class MyDateTimeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = DATE_TIME_CALL_PATTERN.matcher(field);
      if (!mat.matches()) abort();
      data.strDate = mat.group(1);
      data.strTime = mat.group(2);
      data.strCall = mat.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME CALL";
    }
  }

  private static final Pattern UNIT_PLACE_PATTERN = Pattern.compile("((?:[A-Z\\-]{0,10}\\d{1,10}[ \\-]?)+) ?(?:\\*{2}CHECK BLDG NAME\\*{4})? *(.*?)?");

  private class MyUnitPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = UNIT_PLACE_PATTERN.matcher(field);
      if (mat.matches()) {
        data.strUnit = mat.group(1).trim();
        data.strPlace = getOptGroup(mat.group(2));
      } else data.strUnit = field;
    }

    @Override
    public String getFieldNames() {
      return "UNIT PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\[\\d{1,2}/\\d{1,2}/\\d{4} \\d{2}:\\d{2}:\\d{2} [A-Z]+\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        super.parse(line, data);
      }
    }
  }

  private static final String[] CITY_LIST = new String[] { 
    "AFFTON", 
    "BRENTWOOD",
    "CLAYTON", 
    "CREVE COEUR", 
    "LADUE", 
    "MAPLEWOOD", 
    "OLIVETTE", 
    "OVERLAND", 
    "SHREWSBURY", 
    "ST LOUIS COUNTY", 
    "RICHMOND HEIGHTS", 
    "UNIVERSITY CITY", 
    "WEBSTER GROVES" };
}
