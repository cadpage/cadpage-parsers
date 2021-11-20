package net.anei.cadpage.parsers.FL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLBayCountyParser extends FieldProgramParser {

  public FLBayCountyParser() {
    this("BAY COUNTY", "FL");
  }

  FLBayCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "UNIT? ADDR! CITY? MAP? CALL INPROGRESS? DATETIME! ID");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@BAYSO.ORG";
  }

  @Override
  public String getAliasCode() {
    return "FLBayCounty";
  }

  private static Pattern DELIMITER = Pattern.compile(" *, *");
  private static Pattern UNIT = Pattern.compile("\\d{2}[A-Z0-9]?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (UNIT.matcher(subject).matches()) data.strUnit = subject;
    return parseFields(DELIMITER.split(body), data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new AddressField("!?(.*?)");
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("MAP")) return new MapField("ZONE .+", true);
    if (name.equals("INPROGRESS")) return new SkipField("Inprogress !", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d+", true);
    return super.getField(name);
  }


  // Need some special logic to handle the optional AM/PM indicator
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d\\d(?:\\d\\d)?) (\\d{1,2}:\\d{2}:\\d{2}) *(AM|PM)?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      String aa = match.group(3);
      if (aa == null) {
        data.strTime = time;
      } else {
        setTime(TIME_FMT, time + ' ' + aa, data);
      }
    }
  }

  private class MyUnitField extends UnitField {

    public MyUnitField() {
      super("\\d{2,3}", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static Pattern HIGHWAY = Pattern.compile("(\\d+) [NESW]( (?:HIGHWAY|HWY) \\d+)");
  @Override
  public String adjustMapAddress(String address) {
    Matcher mat = HIGHWAY.matcher(address);
    //Remove [NSEW] from 1234 E HWY 123 formatted addresses. (slightly improves mappable address quantity)
    if (mat.matches()) address = mat.group(1) + mat.group(2);
    return address;
  }

  private static final String[] CITY_LIST = new String[]{
    "ST JOE BCH"
  };

}
