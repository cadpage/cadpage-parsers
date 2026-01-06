package net.anei.cadpage.parsers.MO;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOOsageCountyBParser extends FieldProgramParser {

  public MOOsageCountyBParser() {
    super("OSAGE COUNTY", "MO",
          "Address:ADDRCITYST/SP! Lat/Long:GPS! Phone_Number:PHONE! Call_Details:INFO! Cross_Streets:X! Nearest_intersection:SKIP! Additional_Location_Details/Notes:NOTES! Call_Date:DATETIME! CFS_#:ID! Agency_#'s:SKIP!");
    setupCities(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "noreplyosage911@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!parseSubject(subject, data)) return false;
    return super.parseMsg(body, data);
  }

  private static final Pattern SRC_ID_PTN = Pattern.compile("([A-Z]{3,5}) - ([A-Z0-9]{2,4})\\b");
  private boolean parseSubject(String subject, Data data) {
    Set<String> srcList = new HashSet<>();
    while (true) {
      Matcher match = SRC_ID_PTN.matcher(subject);
      if (!match.lookingAt()) return false;
      String src = match.group(1);
      if (srcList.add(src)) data.strSource = append(data.strSource, ",", src);
      data.strUnit = append(data.strUnit, ",", match.group(2));
      subject = subject.substring(match.end());
      if (!subject.startsWith(";")) break;
      subject = subject.substring(1).trim();
    }
    data.strCall = subject.trim();
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC UNIT CALL " + super.getProgram();
  }

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    for (int ndx = 0; ndx < flds.length; ndx++) {
      flds[ndx] = stripFieldEnd(flds[ndx], "None");
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("NOTES")) return new MyNotesField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("/", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d - *| +(?=\\d{1,2}\\.)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyNotesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "LNN", "LINN"
  });
}
