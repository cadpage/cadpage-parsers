package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORWashingtonCountyDParser extends FieldProgramParser {

  public ORWashingtonCountyDParser() {
    super("WASHINGTON COUNTY", "OR", 
          "CALL! ADDR! UNITS:UNIT! INC:SKIP! DAREA:MAP! MAP:MAP! RD:BOX! BEAT/STATION:BOX!");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public String getFilter() {
    return "TibITSAlertingSystem@wccca.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    data.strCallId = subject;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }

  private static Pattern CALL_CODE = Pattern.compile("(.+) \\((.+)\\)");
  public class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      //CALL (CODE)
      Matcher mat = CALL_CODE.matcher(field);
      if (!mat.matches()) abort();
      data.strCall = mat.group(1).trim();
      data.strCode = mat.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CODE";
    }
  }

  private static Pattern GPS_TRAIL = Pattern.compile("<a href=\"http://maps\\.apple\\.com/\\?q=(-?\\d+(?:\\.\\d+)?)%2c(-?\\d+(?:\\.\\d+)?)\" > (.*)</a>");
  private static Pattern ST_X = Pattern.compile("([A-Z]+) \\((.*)\\)");
  public class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      //parse GPS
      Matcher mat = GPS_TRAIL.matcher(field);
      if (!mat.matches()) abort();
      setGPSLoc(mat.group(1) + ',' + mat.group(2), data);

      //ADDR, CITY,
      String[] fields = mat.group(3).split(", ");
      if (fields.length != 3) abort();
      parseAddress(fields[0].trim(), data);
      data.strCity = fields[1].trim();

      //ST (X)
      mat = ST_X.matcher(fields[2].trim());
      if (!mat.matches()) abort();
      data.strState = mat.group(1);
      data.strCross = mat.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames() + " CITY ST X";
    }
  }

  public class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      //prepend with a -
      data.strBox = append(field, "-", data.strBox);
    }
  }

}
