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
          "( SRC_CALL | SRC CALL ) ADDRCITYST! ( XST:X! ID? DATETIME? NOTES:INFO/N+ UNITS:UNIT END" +
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
    if (name.equals("ADDRCITYST"))  return new BaseAddressCityStateField();
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

  private static final Pattern APT_PTN = Pattern.compile("APT *(.*)|\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);

  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      String place = null;
      int pt = field.indexOf(" - ");
      int pt2 = field.lastIndexOf(',');
      if (pt2 >= 0 && pt2 > pt) pt = -1;
      if (pt >= 0) {
        place = field.substring(pt+3).trim();
        field = field.substring(0, pt);
      } else {
        field = stripFieldEnd(field, " -");
      }

      super.parse(field, data);

      if (place != null) {
        Matcher match = APT_PTN.matcher(place);
        if (match.matches()) {
          String tmp = match.group(1);
          if (tmp != null) place = tmp;
          data.strApt = append(data.strApt, "-", place);
        }
        else {
          data.strPlace = place;
        }
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE?";
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
