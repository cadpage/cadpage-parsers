package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA76Parser extends FieldProgramParser {

  public DispatchA76Parser(String defCity, String defState) {
    super(defCity, defState,
          "( SRC_CALL | SRC CALL ) ADDRCITY! ( XST:X! ID? DATETIME? NOTES:INFO/N+ UNITS:UNIT END" +
                                            "| ID ( XST:X! NOTES:INFO/N+ UNITS:UNIT END " +
                                                 "| INFO/N+? X END ) )"
    );
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\| "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC_CALL")) return new BaseSourceCallField();
    if (name.equals("SRC")) return new SourceField("([- A-Z0-9]+):?", true);
    if (name.equals("ADDRCITY"))  return new BaseAddressCityField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("ID")) return new IdField("\\$?([A-Z]*\\d{2}-\\d{4,6})", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern SRC_CALL_PTN = Pattern.compile("([-A-Z0-9 _]+): +(\\S.*)");
  private class BaseSourceCallField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = SRC_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strSource = match.group(1).trim();
      data.strCall = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL";
    }
  }

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " -");
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      String zip = null;
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }

      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (ADDR_ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) city = zip;
      data.strCity = city;

      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!field.contains("//"))  return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATETIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATETIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
