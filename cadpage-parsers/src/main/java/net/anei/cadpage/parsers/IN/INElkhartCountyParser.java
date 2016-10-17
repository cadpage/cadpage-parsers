package net.anei.cadpage.parsers.IN;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INElkhartCountyParser extends FieldProgramParser {

  private static Pattern DATE_TIME_PTN = Pattern.compile("(.*?) (\\d{1,2}/\\d{1,2}/\\d{4}) (\\d{1,2}:\\d{2}:\\d{2} [AP]M)", Pattern.DOTALL);
  private static SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss a");

  public INElkhartCountyParser() {
    super("ELKHART COUNTY", "IN", "CALL:CALL! ADDR:ADDR! UNIT:UNIT! CITY:CITY! ST:ST! INFO:INFO!");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher mat = DATE_TIME_PTN.matcher(body);
    if (!mat.matches()) return false;
    body = mat.group(1).trim();
    data.strDate = mat.group(2);
    setTime(TIME_FMT, mat.group(3), data);
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new UnitField("(.*?),?"); // remove trailing commas
    if (name.equals("ST")) return new StateField("(?:IN)?(.*)"); // exclude value of "IN"
    return super.getField(name);
  }

  private static Pattern CROSS_INFO = Pattern.compile("(.*?)Cross streets: (.*?)");

  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      // parse cross streets from info
      Matcher mat = CROSS_INFO.matcher(field);
      if (mat.matches()) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, mat.group(2).replace("//", "/"), data);
        field = append(mat.group(1), " ", getLeft());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "X INFO";
    }
  }
}
