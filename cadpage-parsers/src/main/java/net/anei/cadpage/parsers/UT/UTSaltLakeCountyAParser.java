package net.anei.cadpage.parsers.UT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class UTSaltLakeCountyAParser extends FieldProgramParser {

  public UTSaltLakeCountyAParser() {
    super("SALT LAKE COUNTY", "UT", "ID! DATETIME! CALL! ( PLACE ADDR/Z APT! X/Z? SRC! | ADDR/Z APT! X/Z? SRC! | PLACE ADDR/Z X/Z SRC! | ADDR/Z SRC! | ADDR/Z X SRC! | PLACE ADDR/Z SRC! ) CODE INFO+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "fsa@slcfire.com,alerts@slcgov.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile(".*(?:ALERT|String Match) - +(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strUnit = match.group(1);
    } else {
      return false;
    }

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{2,3}\\d+", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("APT")) return new AptField("Unit +(.*)", true);
    if (name.equals("X")) return new CrossField("\\d+ [NSEW](?: - \\d+ [NSEW])?", true);
    if (name.equals("SRC")) return new SourceField("[CDSU]F|JW|SS", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
    }
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "10603 S WASATCH BLVD",               "+40.558541,-111.799469"
  });
}