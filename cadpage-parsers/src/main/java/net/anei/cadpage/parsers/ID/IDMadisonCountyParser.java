package net.anei.cadpage.parsers.ID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDMadisonCountyParser extends FieldProgramParser {

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

  public IDMadisonCountyParser() {
    super(CITY_LIST, "MADISON COUNTY", "ID",
          "ID CALL ADDR/S INFO UNIT DATETIME! NAME PHONE GPS1 GPS2");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("New CAD Alert")) return false;
    body = body.replace("//", "/").replaceAll(" {2,}", " ");
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d{1,2}/\\d{1,2}/\\d{4} +\\d{1,2}:\\d{2}:\\d{2} +[AP]M",   DATE_TIME_FMT, true);
    if (name.equals("ID")) return new IdField("\\d{6}", true);
    return super.getField(name);
  }

  private static final Pattern MAP_PATTERN
    = Pattern.compile("(.*)\\bMAP(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = MAP_PATTERN.matcher(field);
      if (m.matches()) {
        data.strMap = m.group(2).trim();
        field = m.group(1).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" MAP";
    }
  }

  private static final String[] CITY_LIST = {
    // Adjacent counties
    "FREMONT COUNTY",
    "TETON COUNTY",
    "BONNEVILLE COUNTY",
    "JEFFERSON COUNTY",

    // Cities
    "NEWDALE",
    "REXTON",
    "REXBURG",
    "SUGAR CITY",
    "THORNTON",
    "ST ANTHONY",
    "RIGBY",
    "LEWISVILLE",
    "RIRIE",
    "MENAN",
    "TERRETON",
    "HAMER",
    "MONTEVIEW"
  };
}
