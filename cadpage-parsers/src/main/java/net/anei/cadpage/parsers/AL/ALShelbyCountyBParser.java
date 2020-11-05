package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class ALShelbyCountyBParser extends FieldProgramParser {

  public ALShelbyCountyBParser() {
    super(CITY_CODES, "SHELBY COUNTY", "AL",
          "CALL:CALL! ADDR:ADDR/S! CITY:CITY! ID:ID! PRI:PRI! UNIT:UNIT! INFO:INFO! INFO/N+ CROSS:X! LATLONG:GPS SentTime:DATETIME END");
  }

  @Override
  public String getFilter() {
    return "arns@shelby911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = stripFieldStart(subject, "RE:");
    if (!subject.startsWith("Event-")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern MA_PTN = Pattern.compile("[A-Z][A-Z0-9]{3} ([A-Z]{3}): @(?:[A-Z]{3,6}:)? *(.*)");
  private static final Pattern COLON_DIR_PTN = Pattern.compile(":([NSEW]B)\\b");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|STE|RM) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_SUFFIX_PTN = Pattern.compile("(.*?) +[A-Z][A-Z0-9]{3}$");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      if (field.startsWith("Error:")) {
        data.strAddress = field;
        return;
      }

      // Check for mutual aid format
      Matcher match = MA_PTN.matcher(field);
      boolean ma = match.matches();
      if (ma) {
        data.strCity = convertCodes(match.group(1), CITY_CODES);
        field = match.group(2);
      }

      field = COLON_DIR_PTN.matcher(field).replaceAll(" $1");

      Parser p = new Parser(field);
      data.strPlace = stripFieldStart(p.getLastOptional(":"), "@");
      data.strApt = p.getLastOptional(',');
      match = APT_PTN.matcher(data.strApt);
      if (match.matches()) data.strApt = match.group(1);
      field = p.get();
      super.parse(field, data);
      if (!ma && !data.strCity.isEmpty()) {
        match = ADDR_SUFFIX_PTN.matcher(data.strAddress);
        if (match.matches()) data.strAddress = match.group(1);
      }
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strCity.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Error:Missing Element")) return;
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }


  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("// UNIT:")) data.msgType = MsgType.RUN_REPORT;
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALA", "ALABASTER",
      "BES", "BESSEMER",
      "BFD", "BRIERFIELD",
      "BHM", "BIRMINGHAM",
      "BIB", "BRIERFIELD",
      "CAL", "CALERA",
      "CHL", "CHELSEA",
      "COL", "COLUMBIANA",
      "HEL", "HELENA",
      "HOV", "HOOVER",
      "HRP", "HARPERSVILLE",
      "IRN", "IRONDALE",
      "JEF", "JEFFERSON COUNTY",
      "LEE", "LEEDS",
      "MAY", "MAYLENE",
      "MON", "MONTEVALLO",
      "PEH", "PELHAM",
      "PEL", "PELHAM",
      "SAG", "SAGINAW",
      "SHE", "SHELBY",
      "STE", "STERRETT",
      "VAN", "VANDIVER",
      "VES", "VESTAVIA HILLS",
      "VIN", "VINCENT",
      "WES", "WESTOVER",
      "WIL", "WILSONVILLE"
  });
}
